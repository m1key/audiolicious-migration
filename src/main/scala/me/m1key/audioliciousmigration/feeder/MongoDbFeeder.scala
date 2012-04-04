package me.m1key.audioliciousmigration.feeder

import me.m1key.audiolicious.domain.entities.Library
import com.google.inject.Inject
import me.m1key.audioliciousmigration.exporter.Exporter
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbLibrary

private[audioliciousmigration] class MongoDbFeeder @Inject() (private val exporter: Exporter) extends Feeder {

  def feed(library: Library): Unit = {
    val mongoDbLibrary = new MongoDbLibrary
    mongoDbLibrary.uuid = library.getUuid()
  }

}