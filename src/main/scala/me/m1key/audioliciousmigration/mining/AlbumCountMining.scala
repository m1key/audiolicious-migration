package me.m1key.audioliciousmigration.mining
import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import com.mongodb.BasicDBObject

class AlbumCountMining @Inject() (private val persistenceProvider: MorphiaMongoDbPersistenceProvider) {

  // Again, the if clause is a hack because there cannot be two statements embedded *inside* the if clause.
  private val query = "db.MongoDbSong.group({" +
	"key: {artistName: 1, albumName: 1}," +
	"reduce: function(obj, prev) {" +
		"if (!obj.hasOwnProperty(\"key\") && (prev.artistName = obj.artistName)) {" +
			"prev.albumName = obj.albumName;" +
		"}" +
	"}," +
	"initial: {}" +
	"}).length";

  def mine(): Option[Int] = {
    val mongo = persistenceProvider.getMongo
    val result = mongo.getDB("audiolicious").eval(query)
    result match {
      case double: java.lang.Double => return Some(double.intValue())
      case _ =>
        println("Error while obtaining stats. Result of unknown type [%s].".format(result))
        return None;
    }
  }

}