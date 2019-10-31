package capegemini.service

import capegemini.Model.{Drink, Food, Hot, Item, ItemCategory}

import scala.math.BigDecimal.RoundingMode

case class CafeService() {

  def getBill(items: Set[Item]): BigDecimal = items.map(_.price).sum

  def getCharge(items: Set[Item]): BigDecimal = {
    case class ByCategory(entry: (ItemCategory, Set[Item])) {
      val isHot: Boolean = entry._2.exists(_.state == Hot)
    }
    val groups = items.groupBy(_.category).transform[ByCategory](ByCategory(_, _))
    val rate = groups.keySet.toList match {
      case cat :: Nil if cat == Drink => 0.0f
      case _ if groups.get(Food).exists(_.isHot) => 0.20f
      case _ => 0.10f
    }
    val charge = (getBill(items) * BigDecimal(rate)).setScale(2, RoundingMode.HALF_UP)
    if (charge > BigDecimal(20)) BigDecimal(20.00) else charge
  }

}