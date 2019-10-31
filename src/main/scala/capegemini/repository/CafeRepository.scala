package capegemini.repository

import capegemini.Model.Item

case class CafeRepository(all: Set[Item]) {

  def some(keys: Array[String]): Set[Item] = all.filter(a => keys.contains(a.name))

}
