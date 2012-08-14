package me.m1key.audioliciousmigration.persistence.mongodb

import com.google.code.morphia.Datastore
import com.mongodb.Mongo
import com.google.code.morphia.Morphia
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbSong

class ProductionMorphiaMongoDbPersistenceProvider extends MorphiaMongoDbPersistenceProvider {

  private var datastore: Datastore = null
  private var mongo: Mongo = null

  def initialise(): Unit = {
    mongo = new Mongo("localhost", 27017)
    datastore = new Morphia().map(classOf[MongoDbSong]).createDatastore(mongo, "audiolicious")
    datastore.ensureIndexes()
  }

  def getDatastore(): Datastore = {
    return datastore
  }
  
  def getMongo(): Mongo = {
    return mongo
  }

}