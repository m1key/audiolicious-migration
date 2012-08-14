package me.m1key.audioliciousmigration.mining
import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import com.mongodb.Mongo
import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import java.text.NumberFormat
import java.text.ParsePosition
import java.util.Locale

class SongPerYearMining @Inject() (private val persistenceProvider: MorphiaMongoDbPersistenceProvider) {

  private val query = "db.MongoDbSong.group("+
	"{key: {year: true},"+
	"initial: {totalSongs: 0},"+
	"reduce: function(obj, prev) { prev.totalSongs++;}"+
	"})";
  
  private val formatter = NumberFormat.getInstance(Locale.ENGLISH)


  def mineTop(maxResults: Int): Option[List[(String, Int)]] = {
    val mongo = persistenceProvider.getMongo
    val result = mongo.getDB("audiolicious").eval(query)
    result match {
      case list: BasicDBList => return Some(processAndRetrieveResults(list, maxResults, compareSecondValueIntegerTopBottom))
      case _ => println("Error while obtaining stats. Result of unknown type [%s].".format(result))
      	return None;
    }
  }
  
  def mineBottom(maxResults: Int): Option[List[(String, Int)]] = {
    val mongo = persistenceProvider.getMongo
    val result = mongo.getDB("audiolicious").eval(query)
    result match {
      case list: BasicDBList => return Some(processAndRetrieveResults(list, maxResults, compareSecondValueIntegerBottomTop))
      case _ => println("Error while obtaining stats. Result of unknown type [%s].".format(result.getClass()))
      	return None;
    }
  }
  
  private def processAndRetrieveResults(list: BasicDBList, maxResults: Int, compare: ((String, Int), (String, Int)) => Boolean): List[(String, Int)] = {
    return processResults(list).sortWith(compare).slice(0, maxResults)
  }

  def compareSecondValueIntegerTopBottom(e1: (String, Int), e2: (String, Int)) = e1._2 > e2._2
  def compareSecondValueIntegerBottomTop(e1: (String, Int), e2: (String, Int)) = e1._2 < e2._2
  
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
    val year = "" + parseDouble(dbObject.get("year").toString(), formatter).intValue()
    val totalSongs = parseDouble(dbObject.get("totalSongs").toString(), formatter).intValue()
    return (year, totalSongs)
  }

  // http://stackoverflow.com/a/9542323
  def parseDouble(s: String, nf: NumberFormat) = {
    val pp = new ParsePosition(0)
    val d = nf.parse(s, pp)
    if (pp.getErrorIndex == -1) d.doubleValue else 0
  }


}