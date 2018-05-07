package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import models.ProductRepository
import scala.concurrent.{ExecutionContext, Future}

/**
  * A repository for people.
  *
  * @param dbConfigProvider The Play db config provider. Play will inject this for you.
  */
@Singleton
class BasketRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import profile.api._

  /**
    * Here we define the table. It will have a name of people
    */
  import productRepository.ProductTable

  private class BaskerTable(tag: Tag) extends Table[Basket]tag, "basket") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    /** The age column */
    def amount = column[Int]("amount")

    def product = column[Int]("product")

    def product_fk = foreignKey("prod_fk",prod, prod)(_.id)
    /**
      * This is the tables default "projection".
      *
      * It defines how the columns are converted to and from the Person object.
      *
      * In this case, we are simply passing the id, name and page parameters to the Person case classes
      * apply and unapply methods.
      */
    def * = (id, amount, product) <> ((Basket.apply _).tupled, Basket.unapply)
    //def * = (id, name) <> ((Category.apply _).tupled, Category.unapply)
  }

  /**
    * The starting point for all queries on the people table.
    */


  import productRepository.ProductTable

  private val basket = TableQuery[BasketTable]

  private val prod = TableQuery[ProductTable]


  /**
    * Create a person with the given name and age.
    *
    * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
    * id for that person.
    */
  def create(product: Int, amount: Int): Future[Basket] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (basket.map(p => (p.product, p.amount))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning basket.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into {case ((product, amount), id) => Basket(id, product, amount)}
      // And finally, insert the person into the database
      ) += (product, amount)
  }

  /**
    * List all the people in the database.
    */
  def list(): Future[Seq[Basket]] = db.run {
    basket.result
  }
}