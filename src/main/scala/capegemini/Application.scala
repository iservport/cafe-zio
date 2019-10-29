package capegemini

import capegemini.Model._
import capegemini.repository.{MenuRepository, MenuRepositoryLive}
import capegemini.service.{CafeService, CafeServiceLive}
import zio._
import zio.clock.Clock
import zio.console.{Console, getStrLn, putStrLn}

object Application extends App {

  type AppEnvironment = Clock with Console with MenuRepository with CafeService

  type AppTask[A] = RIO[AppEnvironment, A]

  def run(args: List[String]): ZIO[Environment, Nothing, Int] = (for {
    menu          <- Ref.make(makeMenu)
    program       <- runner.provideSome[Environment] { _ =>
                    new Clock.Live with Console.Live with MenuRepositoryLive with CafeServiceLive {
                      val ref: Ref[Set[Model.Item]] = menu
                    }
                  }
    _ <- putStrLn("Thank you!")
  } yield program)
    .foldM ( _ => putStrLn("Failed").as(1), _ => ZIO.succeed(0))

  def runner: ZIO[AppEnvironment, Throwable, String] = {
    val quit = "quit"
    val schedule: ZSchedule[AppEnvironment, String, String] = ZSchedule.doUntilEquals[String](quit)
    (for {
      names  <- ZIO.accessM[MenuRepository](_.menuRepository.all)
      msg    <- ZIO.succeed(names.map(_.name).mkString("[ ", " | ", " ]"))
      _      <- putStrLn(s"Enter comma separated items ${msg} or quit:")
      keys   <- getStrLn.map(_.split(",").map(_.trim)) // sure we have a list of keys
      items  <- ZIO.access[MenuRepository](_.menuRepository.some(keys))
      price  <- ZIO.accessM[CafeService](_.cafeService.getBill(items))
      charge <- ZIO.accessM[CafeService](_.cafeService.getCharge(items))
      _      <- putStrLn(s"Your price is ${price}. Your charge is ${charge}")
    } yield keys.headOption.getOrElse(quit))
      .repeat(schedule)
  }


  def makeMenu = Set(
     Item(name="Cola",            state=Cold, category=Drink, price=0.50 )
    ,Item(name="Coffee",          state=Hot,  category=Drink, price=1.00 )
    ,Item(name="Cheese Sandwich", state=Cold, category=Food,  price=2.00 )
    ,Item(name="Steak Sandwich",  state=Hot,  category=Food,  price=4.50 )
  )

}
