package me.m1key.audioliciousmigration.entities.mongodb
import com.google.code.morphia.annotations.Embedded
import java.util.HashSet
import java.util.Set

@Embedded
class MongoDbArtist(val name: String) {

  @Embedded
  val albums: Set[MongoDbAlbum] = new HashSet[MongoDbAlbum]

  def addAlbum(album: MongoDbAlbum): Unit = {
    albums.add(album)
  }

  override def equals(that: Any) = {
    that match {
      case that: MongoDbArtist => that.name == this.name
      case _ => false
    }
  }

  override def hashCode(): Int = name.hashCode()

}