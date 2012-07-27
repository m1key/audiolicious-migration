package me.m1key.audioliciousmigration.exporter

import me.m1key.audioliciousmigration.entities.mongodb.MongoDbLibrary
import com.google.inject.Inject
import me.m1key.audioliciousmigration.repository.MorphiaMongoDbRepository

class MongoDbExporter @Inject() (private val repository: MorphiaMongoDbRepository) {

  def export(library: MongoDbLibrary): Unit = {
    println("Exporting library [%s]...".format(library.uuid));
    repository.save(library);
    println("Exported library [%s].".format(library.uuid));
  }

}