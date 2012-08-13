package me.m1key.audioliciousmigration.repository

import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import scalaj.collection.Imports._
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbSong

class MorphiaMongoDbRepository @Inject() (private val persistenceProvider: MorphiaMongoDbPersistenceProvider) {

  def save(song: MongoDbSong): Unit = {
    val datastore = persistenceProvider.getDatastore()
    datastore.save(List(song).asJava)
  }

}