package me.m1key.audioliciousmigration.feeder

import me.m1key.audiolicious.domain.entities.Artist
import com.google.inject.Inject
import me.m1key.audioliciousmigration.exporter.Exporter

private[audioliciousmigration] class DefaultFeeder @Inject() (private val exporter: Exporter) extends Feeder {

  def feed(artist: Artist): Unit = {}

}