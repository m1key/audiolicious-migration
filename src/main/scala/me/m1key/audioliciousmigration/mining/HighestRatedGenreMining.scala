package me.m1key.audioliciousmigration.mining

import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import com.mongodb.Mongo
import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import java.text.NumberFormat
import java.text.ParsePosition
import java.util.Locale


class HighestRatedGenreMining @Inject() (private val persistenceProvider: MorphiaMongoDbPersistenceProvider) {

  // This is a hack.
  // It won't allow two statements in the reduce call.
  // Therefore the totalAlbums++ one has to be used in the if statement.
  private val query = "db.MongoDbSong.group(" +
	"{key: {genre: 1}," +
	"initial: {totalRatings: 0, totalSongs: 0, totalRatingPerGenre: 0}," +
	"reduce: function(obj, prev) {obj.statsList.forEach(function(item){if(item.libraryUuid == '%s' && ++prev.totalSongs){ prev.totalRatings += item.percentage}})}," +
	"finalize: function(prev) {prev.totalRatingPerGenre = prev.totalRatings / prev.totalSongs}" +
  "})";
  
  private val formatter = NumberFormat.getInstance(Locale.ENGLISH)


  def mine(libraryUuid: String): Option[List[(String, Double)]] = {
    val mongo = persistenceProvider.getMongo
    val result = mongo.getDB("audiolicious").eval(query.format(libraryUuid))
    result match {
      case list: BasicDBList => return Some(processAndRetrieveResults(list))
      case _ => println("Error while obtaining stats. Result of unknown type [%s].".format(result))
      	return None;
    }
  }
  
  private def processAndRetrieveResults(list: BasicDBList): List[(String, Double)] = {
    return processResults(list).sortWith(compareSecondValueInteger)
  }

  def compareSecondValueInteger(e1: (String, Double), e2: (String, Double)) = e1._2 > e2._2
  
  private def processResults(list: BasicDBList): List[(String, Double)] = {
    var results : List[(String, Double)] = List()
    for (i <- 0 until list.size()) {
      val item = list.get(i)
      item match {
        case dbObject: BasicDBObject => results ::= processResult(dbObject)
        case _ => println("Error while obtaining stats. Result item of unknown type [%s].".format(item.getClass()))
      }
    }
    return results
  }
  
  private def processResult(dbObject: BasicDBObject): (String, Double) = {
    val genre = dbObject.get("genre").toString()
    val totalPlays = parseDouble(dbObject.get("totalRatingPerGenre").toString(), formatter)
    return (genre, totalPlays)
  }

  // http://stackoverflow.com/a/9542323
  def parseDouble(s: String, nf: NumberFormat) = {
    val pp = new ParsePosition(0)
    val d = nf.parse(s, pp)
    if (pp.getErrorIndex == -1) d.doubleValue else 0
  }

}
