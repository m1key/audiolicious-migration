package me.m1key.audioliciousmigration.entities.mongodb
import com.google.code.morphia.annotations.Id
import com.google.code.morphia.annotations.Entity
import org.bson.types.ObjectId
import com.google.code.morphia.annotations.Indexes
import com.google.code.morphia.annotations.Index

@Entity
@Indexes(Array(new Index(value = "name, albumName", unique = true)))
class MongoDbSong(val name: String, val albumName: String) {

  @Id
  var id: ObjectId = _
  
  override def equals(that: Any) = {
    that match {
      case that: MongoDbSong => that.name == this.name &&
    		  that.albumName == this.albumName
      case _ => false
    }
  }

  override def hashCode(): Int = name.hashCode()

}