package capegemini

import capegemini.Model._
import capegemini.repository.CafeRepository
import capegemini.service.CafeService

import scala.io.StdIn

object Application extends App {

  val repository = CafeRepository(makeMenu)
  val cafeService = CafeService()

  runner
  println("Thank you!")

  def runner = {
    var quit = false
    val names = repository.all
    val msg = names.map(_.name).mkString("[ ", " | ", " ]")
    do {
      println(s"Enter comma separated items ${msg} or quit:")
      val keys = StdIn.readLine().split(",").map(_.trim)
      quit = keys.headOption.getOrElse("quit")=="quit"
      if (!quit) {
        val items = repository.some(keys)
        val price = cafeService.getBill(items)
        val charge = cafeService.getCharge(items)
        println(s"Your price is ${price}. Your charge is ${charge}")
      }
    } while (!quit)
  }

  def makeMenu = Set(
     Item(name="Cola",            state=Cold, category=Drink, price=0.50 )
    ,Item(name="Coffee",          state=Hot,  category=Drink, price=1.00 )
    ,Item(name="Cheese Sandwich", state=Cold, category=Food,  price=2.00 )
    ,Item(name="Steak Sandwich",  state=Hot,  category=Food,  price=4.50 )
  )

}
