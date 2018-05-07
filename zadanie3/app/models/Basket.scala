package models

import play.api.libs.json._

case class Basket(id: Long, product: Long, amount: Int)

object Basket {
  implicit val basket = Json.format[Basket]
}
