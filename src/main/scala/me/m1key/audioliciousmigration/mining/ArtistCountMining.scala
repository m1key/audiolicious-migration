package me.m1key.audioliciousmigration.mining
import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import com.mongodb.BasicDBObject

class ArtistCountMining @Inject() (private val persistenceProvider: MorphiaMongoDbPersistenceProvider) {

  private val query = "db.MongoDbSong.distinct(\"artistName\").length";

  def mine(): Option[Int] = {
    val mongo = persistenceProvider.getMongo
    val result = mongo.getDB("audiolicious").eval(query)
    result match {
      case double: java.lang.Double => return Some(double.intValue())
      case _ =>
        println("Error while obtaining stats. Result of unknown type [%s].".format(result.getClass()))
        return None;
    }
  }

}