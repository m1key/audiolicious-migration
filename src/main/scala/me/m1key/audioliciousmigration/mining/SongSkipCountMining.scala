package me.m1key.audioliciousmigration.mining

import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import com.mongodb.BasicDBObject
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbSong
import scala.collection.JavaConversions._

class SongSkipCountMining @Inject() (private val persistenceProvider: MorphiaMongoDbPersistenceProvider) {

  def mine(limit: Int): List[MongoDbSong] = {
    val query = persistenceProvider.getDatastore().createQuery(classOf[MongoDbSong]).order("-statsList.skipCount").limit(limit);
    return asScalaIterable(query.fetch()).toList
  }

}