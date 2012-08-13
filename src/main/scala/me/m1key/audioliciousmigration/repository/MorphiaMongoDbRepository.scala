package me.m1key.audioliciousmigration.repository

import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import scalaj.collection.Imports._

class MorphiaMongoDbRepository @Inject() (private val persistenceProvider: MorphiaMongoDbPersistenceProvider) {

  def save(): Unit = {
    val datastore = persistenceProvider.getDatastore()
    // TODO save song
//    datastore.save(List(library).asJava)
  }

}