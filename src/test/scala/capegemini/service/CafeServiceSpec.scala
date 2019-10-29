package capegemini.service

import capegemini.Application.makeMenu
import capegemini.Model.{Cold, Drink, Food, Hot, Item}
import capegemini.repository.{MenuRepository, MenuRepositoryLive}
import capegemini.{Application, Model}
import zio.{Ref, ZIO}
import zio.test.Assertion.equalTo
import zio.test.{DefaultRunnableSpec, assert, suite, testM}

object CafeServiceSpec extends DefaultRunnableSpec(
  suite("CafeService")(
    testM("Standard Bill") {
      for {
        menu     <- Ref.make(makeMenu)
        program  <- ZIO.runtime.provide(new MenuRepositoryLive with CafeServiceLive {
                      val ref: Ref[Set[Model.Item]] = menu
                    })
        service  =  new CafeServiceLive {}
        output   =  program.unsafeRun {
                      service.cafeService.getBill(menu.get)
                    }
      } yield assert(output, equalTo(BigDecimal(8.0)))
    },
    testM("Service Charge, case 0%") {
      for {
        menu     <- Ref.make{
          Set(
             Item(name="Cola",            state=Cold, category=Drink, price=0.50 )
            ,Item(name="Coffee",          state=Hot,  category=Drink, price=1.00 )
          )
        }
        program  <- ZIO.runtime.provide(new MenuRepositoryLive with CafeServiceLive {
          val ref: Ref[Set[Model.Item]] = menu
        })
        service  =  new CafeServiceLive {}
        output   =  program.unsafeRun {
          service.cafeService.getCharge(menu.get)
        }
      } yield assert(output, equalTo(BigDecimal(0)))
    },
    testM("Service Charge, case 10%") {
      for {
        menu     <- Ref.make{
          Set(
             Item(name="Cola",            state=Cold, category=Drink, price=0.50 )
            ,Item(name="Cheese Sandwich", state=Cold, category=Food,  price=2.00 )
          )
        }
        program  <- ZIO.runtime.provide(new MenuRepositoryLive with CafeServiceLive {
          val ref: Ref[Set[Model.Item]] = menu
        })
        service  =  new CafeServiceLive {}
        output   =  program.unsafeRun {
          service.cafeService.getCharge(menu.get)
        }
      } yield assert(output, equalTo(BigDecimal(0.25)))
    },
    testM("Service Charge, case 20%") {
      for {
        menu     <- Ref.make{
          Set(
             Item(name="Cola",            state=Cold, category=Drink, price=0.50 )
            ,Item(name="Cheese Sandwich", state=Cold, category=Food,  price=2.00 )
            ,Item(name="Steak Sandwich",  state=Hot,  category=Food,  price=4.50 )
          )
        }
        program  <- ZIO.runtime.provide(new MenuRepositoryLive with CafeServiceLive {
          val ref: Ref[Set[Model.Item]] = menu
        })
        service  =  new CafeServiceLive {}
        output   =  program.unsafeRun {
          service.cafeService.getCharge(menu.get)
        }
      } yield assert(output, equalTo(BigDecimal(1.40)))
    },
    testM("Service Charge, case max") {
      for {
        menu     <- Ref.make{
          Set(
             Item(name="Cola",            state=Cold, category=Drink, price=0.50 )
            ,Item(name="Cheese Sandwich", state=Cold, category=Food,  price=2.00 )
            ,Item(name="Steak Sandwich",  state=Hot,  category=Food,  price=100.00 )

          )
        }
        program  <- ZIO.runtime.provide(new MenuRepositoryLive with CafeServiceLive {
          val ref: Ref[Set[Model.Item]] = menu
        })
        service  =  new CafeServiceLive {}
        output   =  program.unsafeRun {
          service.cafeService.getCharge(menu.get)
        }
      } yield assert(output, equalTo(BigDecimal(20.00)))
    }

  )
)
