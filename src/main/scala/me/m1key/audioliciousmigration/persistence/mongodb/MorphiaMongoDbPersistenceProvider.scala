package me.m1key.audioliciousmigration.persistence.mongodb

import com.google.code.morphia.Datastore
import com.mongodb.Mongo

trait MorphiaMongoDbPersistenceProvider {
  
  def initialise(): Unit
  def getDatastore(): Datastore
  def getMongo(): Mongo

}