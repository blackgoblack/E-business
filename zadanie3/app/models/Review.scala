package models

import play.api.libs.json._

case class Review(id: Long, product: Long, description: String)

object Review {
  implicit val review = Json.format[Review]
}
