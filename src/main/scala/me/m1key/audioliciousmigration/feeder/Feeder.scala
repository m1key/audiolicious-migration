package me.m1key.audioliciousmigration.feeder

import me.m1key.audiolicious.domain.entities.Artist

trait Feeder {
  
  def feed(artist: Artist): Unit

}