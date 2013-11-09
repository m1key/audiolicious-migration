package me.m1key.audioliciousmigration.mining
import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import com.mongodb.Mongo
import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import java.text.NumberFormat
import java.text.ParsePosition
import java.util.Locale

class SongPerArtistMining @Inject() (private val persistenceProvider: MorphiaMongoDbPersistenceProvider) {

  private val query = "db.MongoDbSong.group("+
	"{key: {artistName: true},"+
	"initial: {totalSongs: 0},"+
	"reduce: function(obj, prev) { prev.totalSongs++}"+
	"})";
  
  private val formatter = NumberFormat.getInstance(Locale.ENGLISH)


  def mine(maxResults: Int): Option[List[(String, Int)]] = {
    val mongo = persistenceProvider.getMongo
    val result = mongo.getDB("audiolicious").eval(query)
    result match {
      case list: BasicDBList => return Some(processAndRetrieveResults(list, maxResults))
      case _ => println("Error while obtaining stats. Result of unknown type [%s].".format(result))
      	return None;
    }
  }
  
  private def processAndRetrieveResults(list: BasicDBList, maxResults: Int): List[(String, Int)] = {
    return processResults(list).sortWith(compareSecondValueInteger).slice(0, maxResults)
  }

  def compareSecondValueInteger(e1: (String, Int), e2: (String, Int)) = e1._2 > e2._2
  
  private def processResults(list: BasicDBList): List[(String, Int)] = {
    var results : List[(String, Int)] = List()
    for (i <- 0 until list.size()) {
      val item = list.get(i)
      item match {
        case dbObject: BasicDBObject => results ::= processResult(dbObject)
        case _ => println("Error while obtaining stats. Result item of unknown type [%s].".format(item.getClass()))
      }
    }
    return results
  }
  
  private def processResult(dbObject: BasicDBObject): (String, Int) = {
    val artistName = dbObject.get("artistName").toString()
    val totalSongs = parseDouble(dbObject.get("totalSongs").toString(), formatter).intValue()
    return (artistName, totalSongs)
  }

  // http://stackoverflow.com/a/9542323
  def parseDouble(s: String, nf: NumberFormat) = {
    val pp = new ParsePosition(0)
    val d = nf.parse(s, pp)
    if (pp.getErrorIndex == -1) d.doubleValue else 0
  }


}