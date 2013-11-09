package me.m1key.audioliciousmigration.mining

import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import com.mongodb.Mongo
import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import java.text.NumberFormat
import java.text.ParsePosition
import java.util.Locale


class HighestPlayCountContrastAlbumMining @Inject() (private val persistenceProvider: MorphiaMongoDbPersistenceProvider) {

  // This is a hack.
  // It won't allow two statements in the reduce call.
  // Therefore the totalAlbums++ one has to be used in the if statement.
  private val query = "db.MongoDbSong.group(" +
	"{key: {artistName: 1, albumName: 1}," +
	"initial: {maxPlayed: 0, minPlayed: 2147483647, contrast: 0}," +
	"reduce: function(obj, prev) {obj.statsList.forEach(function(item){if(item.libraryUuid == '%s' && " +
	"  ((item.playCount > prev.maxPlayed && (prev.maxPlayed = item.playCount)) && (item.playCount < prev.minPlayed && (prev.minPlayed = item.playCount)))){}})}," +
	"finalize: function(prev) {prev.contrast = (prev.maxPlayed*prev.maxPlayed) - (prev.minPlayed*prev.minPlayed)}" +
  "})";
  
  private val formatter = NumberFormat.getInstance(Locale.ENGLISH)


  def mine(maxResults: Int, libraryUuid: String): Option[List[(String, String, Double)]] = {
    val mongo = persistenceProvider.getMongo
    val result = mongo.getDB("audiolicious").eval(query.format(libraryUuid))
    result match {
      case list: BasicDBList => return Some(processAndRetrieveResults(list, maxResults))
      case _ => println("Error while obtaining stats. Result of unknown type [%s].".format(result))
      	return None;
    }
  }
  
  private def processAndRetrieveResults(list: BasicDBList, maxResults: Int): List[(String, String, Double)] = {
    return processResults(list).sortWith(compareThirdValueInteger).slice(0, maxResults)
  }

  def compareThirdValueInteger(e1: (String, String, Double), e2: (String, String, Double)) = e1._3 > e2._3
  
  private def processResults(list: BasicDBList): List[(String, String, Double)] = {
    var results : List[(String, String, Double)] = List()
    for (i <- 0 until list.size()) {
      val item = list.get(i)
      item match {
        case dbObject: BasicDBObject => results ::= processResult(dbObject)
        case _ => println("Error while obtaining stats. Result item of unknown type [%s].".format(item.getClass()))
      }
    }
    return results
  }
  
  private def processResult(dbObject: BasicDBObject): (String, String, Double) = {
    val artistName = dbObject.get("artistName").toString()
    val albumName = dbObject.get("albumName").toString()
    val contrast = parseDouble(dbObject.get("contrast").toString(), formatter)
    return (artistName, albumName, contrast)
  }

  // http://stackoverflow.com/a/9542323
  def parseDouble(s: String, nf: NumberFormat) = {
    val pp = new ParsePosition(0)
    val d = nf.parse(s, pp)
    if (pp.getErrorIndex == -1) d.doubleValue else 0
  }

}