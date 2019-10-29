package capegemini.repository

import capegemini.Model.Item
import zio.{Ref, Task}

trait MenuRepositoryLive extends MenuRepository {

  val ref: Ref[Set[Item]]

  val menuRepository: MenuRepository.Service[Any] = new MenuRepository.Service[Any] {
    def all: Task[Set[Item]] = ref.get

    def some(keys: Array[String]): Task[Set[Item]] = for {
      all      <- ref.get
      filtered <- Task.succeed(all.filter(a => keys.contains(a.name)))
    } yield filtered

  }

}
