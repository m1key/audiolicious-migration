package me.m1key.audioliciousmigration.entities.mongodb
import com.google.code.morphia.annotations.Id
import com.google.code.morphia.annotations.Entity
import org.bson.types.ObjectId
import com.google.code.morphia.annotations.Indexes
import com.google.code.morphia.annotations.Index

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