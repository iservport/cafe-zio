package capegemini.service

import capegemini.Model._
import zio.{RIO, Task, ZIO}

import scala.math.BigDecimal.RoundingMode

trait CafeServiceLive extends CafeService {

  val cafeService: CafeService.Service[Any] = new CafeService.Service[Any] {

    def getBill(itemsTask: Task[Set[Item]]): RIO[Any, BigDecimal] = itemsTask.map(_.map(_.price).sum)

    def getCharge(itemsTask: Task[Set[Item]]): RIO[Any, BigDecimal] = {
      case class ByCategory(entry: (ItemCategory, Set[Item])) {
        val isHot: Boolean = entry._2.exists(_.state==Hot)
      }
      for {
        items      <- itemsTask
        groups     =  items
                        .groupBy(_.category)
                        .transform[ByCategory](ByCategory(_, _))
        rate       =  groups.keySet.toList match {
                        case cat :: Nil if cat == Drink => 0.0f
                        case _ if groups.get(Food).exists(_.isHot) => 0.20f
                        case _ => 0.10f
                      }
        chargeMax  <- getBill(itemsTask).flatMap { b =>
                        ZIO.succeed {
                          val charge = (b * BigDecimal(rate)).setScale(2, RoundingMode.HALF_UP)
                          if (charge > BigDecimal(20)) BigDecimal(20.00) else charge
                        }
                      }
      } yield chargeMax

    }

  }

}
