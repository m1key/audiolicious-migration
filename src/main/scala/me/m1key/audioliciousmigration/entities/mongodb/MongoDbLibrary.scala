package me.m1key.audioliciousmigration.entities.mongodb

import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Id
import org.bson.types.ObjectId
import java.util.HashSet
import java.util.Set

@Entity
class MongoDbLibrary {
  @Id
  var id: ObjectId = _

  var uuid: String = _
  
  var artists: Set[String] = new HashSet[String]
  
  def addArtist(artistName: String): Unit = {
    artists.add(artistName)
  }
}