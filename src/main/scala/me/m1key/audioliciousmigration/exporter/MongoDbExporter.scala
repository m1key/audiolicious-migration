package me.m1key.audioliciousmigration.exporter

import com.google.inject.Inject
import me.m1key.audioliciousmigration.repository.MorphiaMongoDbRepository
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbSong

class MongoDbExporter @Inject() (private val repository: MorphiaMongoDbRepository) {

  def export(song: MongoDbSong): Unit = {
    repository.save(song);
  }

}