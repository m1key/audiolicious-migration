package me.m1key.audioliciousmigration.feeder

import me.m1key.audiolicious.domain.entities.Library

trait Feeder {

  def feed(library: Library): Unit

}