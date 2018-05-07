package models

import play.api.libs.json._

case class Order(id: Long, basket: Int, date: String, value: Double)

object Order {
  implicit val zamowienieFormat = Json.format[Zamowienie]
}