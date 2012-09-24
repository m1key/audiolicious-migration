package me.m1key.audioliciousmigration.entities.mongodb
import com.google.code.morphia.annotations.Id
import com.google.code.morphia.annotations.Entity
import org.bson.types.ObjectId
import com.google.code.morphia.annotations.Indexes
import com.google.code.morphia.annotations.Index
import com.google.code.morphia.annotations.Embedded
import java.util.ArrayList

@Entity
@Indexes(Array(new Index(value = "name, albumName, artistName, songKey", unique = true)))
class MongoDbSong(val name: String, val albumName: String, val artistName: String, val songKey: String) {

  // No-args constructor to be used by Morphia.
  def this() {
    this("name to be set", "albumName to be set", "artistName to be set", "songKey to be set")
  }

  @Id
  var id: ObjectId = _

  var genre: String = _
  var year: Int = _
  var songArtistName: String = _

  @Embedded
  var statsList: java.util.List[MongoDbStats] = new ArrayList[MongoDbStats]

  def containsStatsForLibraryUuid(libraryUuid: String): Boolean = {
    getStatsForLibraryUuid(libraryUuid) match {
      case Some(_) => return true
      case None => return false
    }
  }

  def getStatsForLibraryUuid(libraryUuid: String): Option[MongoDbStats] = {
    for (i <- 0 to statsList.size() - 1) {
      if (statsList.get(i).libraryUuid == libraryUuid) {
        return Some(statsList.get(i))
      }
    }

    return None
  }

  def addOrEditStats(libraryUuid: String, percentage: Int): Unit = {
    if (containsStatsForLibraryUuid(libraryUuid)) {
      getStatsForLibraryUuid(libraryUuid) match {
        case Some(x) => x.percentage = percentage
        case None => throw new RuntimeException("Stats for library [%s] not found in song [%s]. This is a bug.".format(libraryUuid, name))
      }
    } else {
      statsList.add(new MongoDbStats(libraryUuid, percentage))
    }
  }

  override def equals(that: Any) = {
    that match {
      case that: MongoDbSong => that.name == this.name &&
        that.albumName == this.albumName && that.artistName == this.artistName &&
        that.songKey == this.songKey
      case _ => false
    }
  }

  override def hashCode(): Int = name.hashCode()

}