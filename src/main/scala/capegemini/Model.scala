package capegemini

object Model {

  case class Item(name: String, state: ItemState, category: ItemCategory, price: BigDecimal)

  sealed trait ItemState
  case object Cold extends ItemState
  case object Hot  extends ItemState

  sealed trait ItemCategory
  case object Drink extends ItemCategory
  case object Food  extends ItemCategory

}
