package me.m1key.audioliciousmigration.persistence.mongodb

import com.google.code.morphia.Datastore

trait MorphiaMongoDbPersistenceProvider {
  
  def initialise(): Unit
  def getDatastore(): Datastore

}