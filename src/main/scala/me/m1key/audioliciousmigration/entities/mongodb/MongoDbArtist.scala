package me.m1key.audioliciousmigration.entities.mongodb
import com.google.code.morphia.annotations.Embedded

@Embedded
class MongoDbArtist(val name: String) {

  override def equals(that: Any) = {
    that match {
      case that: MongoDbArtist => that.name == this.name
      case _ => false
    }
  }

  override def hashCode(): Int = name.hashCode()

}