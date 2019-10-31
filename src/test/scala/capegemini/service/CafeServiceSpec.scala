package capegemini.service

import capegemini.Application
import capegemini.Model.{Cold, Drink, Food, Hot, Item}
import org.scalatest._

class CafeServiceSpec extends FlatSpec {

  val cafeService = CafeService()

  "Standard Bill" should "calculate total of items consumed" in {
    val items = Application.makeMenu
    assert(cafeService.getBill(items) == BigDecimal(8.0))
  }

  "Service Charge" should "be 0.0 if only drinks" in {
    val items =           Set(
      Item(name="Cola",            state=Cold, category=Drink, price=0.50 )
      ,Item(name="Coffee",          state=Hot,  category=Drink, price=1.00 )
    )
    assert(cafeService.getCharge(items) == BigDecimal(0.0))
  }

  it should("be 10% if at least one food is included") in {
    val items =           Set(
      Item(name="Cola",            state=Cold, category=Drink, price=0.50 )
      ,Item(name="Cheese Sandwich", state=Cold, category=Food,  price=2.00 )
    )
    assert(cafeService.getCharge(items) == BigDecimal(0.25))
  }

  it should("be 20% if at least one hot food is included") in {
    val items =           Set(
      Item(name="Cola",            state=Cold, category=Drink, price=0.50 )
      ,Item(name="Cheese Sandwich", state=Cold, category=Food,  price=2.00 )
      ,Item(name="Steak Sandwich",  state=Hot,  category=Food,  price=4.50 )
    )
    assert(cafeService.getCharge(items) == BigDecimal(1.40))
  }

  it should("be never greater than 20") in {
    val items =           Set(
      Item(name="Cola",            state=Cold, category=Drink, price=0.50 )
      ,Item(name="Cheese Sandwich", state=Cold, category=Food,  price=2.00 )
      ,Item(name="Steak Sandwich",  state=Hot,  category=Food,  price=100.00 )
    )
    assert(cafeService.getCharge(items) == BigDecimal(20.0))
  }

}
