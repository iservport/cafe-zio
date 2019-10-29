package capegemini.service

import capegemini.Model.Item
import zio.{RIO, Task}

trait CafeService {
  val cafeService: CafeService.Service[Any]
}
object CafeService {
  trait Service[R] {
    def getBill(itemsTask: Task[Set[Item]]): RIO[R, BigDecimal]
    def getCharge(itemsTask: Task[Set[Item]]): RIO[R, BigDecimal]
  }
}

