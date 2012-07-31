package me.m1key.audioliciousmigration.persistence.mongodb

import com.google.code.morphia.Datastore
import com.mongodb.Mongo
import com.google.code.morphia.Morphia
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbLibrary

class ProductionMorphiaMongoDbPersistenceProvider extends MorphiaMongoDbPersistenceProvider {

  private var datastore: Datastore = null

  def initialise(): Unit = {
    val mongo = new Mongo("localhost", 27017)
    datastore = new Morphia().map(classOf[MongoDbLibrary]).createDatastore(mongo, "audiolicious")
    datastore.ensureIndexes()
  }

  def getDatastore(): Datastore = {
    return datastore
  }

}