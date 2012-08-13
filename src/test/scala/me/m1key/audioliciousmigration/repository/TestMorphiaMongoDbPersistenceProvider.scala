package me.m1key.audioliciousmigration.repository

import com.google.code.morphia.Datastore
import com.mongodb.Mongo
import com.google.code.morphia.Morphia
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider

class TestMorphiaMongoDbPersistenceProvider extends MorphiaMongoDbPersistenceProvider {

  private var datastore: Datastore = null

  def initialise(): Unit = {
    val mongo = new Mongo("localhost", 27017)
//    datastore = new Morphia().map(classOf[MongoDbLibrary]).createDatastore(mongo, "audiolicious_test")
    // TODO create Song datastore
//    datastore.ensureIndexes()
  }

  def getDatastore(): Datastore = {
    return datastore
  }

}