package me.m1key.audioliciousmigration.repository

import me.m1key.audioliciousmigration.entities.mongodb.MongoDbLibrary
import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import scalaj.collection.Imports._

class MorphiaMongoDbRepository @Inject() (private val persistenceProvider: MorphiaMongoDbPersistenceProvider) {

  def save(library: MongoDbLibrary): Unit = {
    val datastore = persistenceProvider.getDatastore()
    datastore.save(List(library).asJava)
  }

}