package me.m1key.audioliciousmigration.exporter

import com.google.inject.Inject
import me.m1key.audioliciousmigration.repository.MorphiaMongoDbRepository

class MongoDbExporter @Inject() (private val repository: MorphiaMongoDbRepository) {

  // TODO accept song
  def export(): Unit = {
    // TODO save song
//    repository.save(library);
  }

}