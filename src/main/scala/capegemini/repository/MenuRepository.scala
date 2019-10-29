package capegemini.repository

import capegemini.Model.Item
import zio.Task

trait MenuRepository {
  val menuRepository: MenuRepository.Service[Any]
}
object MenuRepository {

  trait Service[R] {
    def all: Task[Set[Item]]
    def some(keys: Array[String]): Task[Set[Item]]
  }
}