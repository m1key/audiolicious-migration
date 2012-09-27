package me.m1key.audioliciousmigration.mining

import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import com.mongodb.BasicDBObject
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbSong
import scala.collection.JavaConversions._

class SongPlayCountMining @Inject() (private val persistenceProvider: MorphiaMongoDbPersistenceProvider) {

  def mine(limit: Int, libraryUuid: String): List[MongoDbSong] = {
    val query = persistenceProvider.getDatastore().createQuery(classOf[MongoDbSong]).order("-statsList.playCount").filter("statsList.libraryUuid", libraryUuid).limit(limit);
    return iterableAsScalaIterable(query.fetch()).toList
  }

}