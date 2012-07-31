package me.m1key.audioliciousmigration.entities.mongodb

import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Id
import org.bson.types.ObjectId
import java.util.HashSet
import java.util.Set
import com.google.code.morphia.annotations.Embedded

@Entity
class MongoDbLibrary {
  @Id
  var id: ObjectId = _

  var uuid: String = _

  @Embedded
  val artists: Set[MongoDbArtist] = new HashSet[MongoDbArtist]

  def addArtist(artist: MongoDbArtist): MongoDbArtist = {
    val added = artists.add(artist)
    if (added) {
      return artist
    } else {
      val iterator = artists.iterator()
      while (iterator.hasNext()) {
        val currentArtist = iterator.next();
        if (currentArtist == artist) {
          return currentArtist
        }
      }
      throw new RuntimeException("Artist [%s] added but not found...?".format(artist.name))
    }
  }
}