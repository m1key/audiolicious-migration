package me.m1key.audioliciousmigration.mining
import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import com.mongodb.Mongo
import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import java.text.NumberFormat
import java.text.ParsePosition
import java.util.Locale

class MostPlayedAlbumsMining @Inject() (private val persistenceProvider: MorphiaMongoDbPersistenceProvider) {

  // This is a hack.
  // It won't allow two statements in the reduce call.
  // Therefore the totalAlbums++ one has to be used in the if statement.
  private val query = "db.MongoDbSong.group(" +
	"{key: {artistName: 1, albumName: 1}," +
	"initial: {totalPlays: 0}," +
	"reduce: function(obj, prev) {obj.statsList.forEach(function(entry){if(entry.libraryUuid == '%s'){ prev.totalPlays += entry.playCount}})}" +
  "})";
  
  private val formatter = NumberFormat.getInstance(Locale.ENGLISH)


  def mine(maxResults: Int, libraryUuid: String): Option[List[(String, String, Int)]] = {
    val mongo = persistenceProvider.getMongo
    val result = mongo.getDB("audiolicious").eval(query.format(libraryUuid))
    result match {
      case list: BasicDBList => return Some(processAndRetrieveResults(list, maxResults))
      case _ => println("Error while obtaining stats. Result of unknown type [%s].".format(result))
      	return None;
    }
  }
  
  private def processAndRetrieveResults(list: BasicDBList, maxResults: Int): List[(String, String, Int)] = {
    return processResults(list).sortWith(compareThirdValueInteger).slice(0, maxResults)
  }

  def compareThirdValueInteger(e1: (String, String, Int), e2: (String, String, Int)) = e1._3 > e2._3
  
  private def processResults(list: BasicDBList): List[(String, String, Int)] = {
    var results : List[(String, String, Int)] = List()
    for (i <- 0 until list.size()) {
      val item = list.get(i)
      item match {
        case dbObject: BasicDBObject => results ::= processResult(dbObject)
        case _ => println("Error while obtaining stats. Result item of unknown type [%s].".format(item.getClass()))
      }
    }
    return results
  }
  
  private def processResult(dbObject: BasicDBObject): (String, String, Int) = {
    val artistName = dbObject.get("artistName").toString()
    val albumName = dbObject.get("albumName").toString()
    val totalPlays = parseDouble(dbObject.get("totalPlays").toString(), formatter).intValue()
    return (artistName, albumName, totalPlays)
  }

  // http://stackoverflow.com/a/9542323
  def parseDouble(s: String, nf: NumberFormat) = {
    val pp = new ParsePosition(0)
    val d = nf.parse(s, pp)
    if (pp.getErrorIndex == -1) d.doubleValue else 0
  }


}