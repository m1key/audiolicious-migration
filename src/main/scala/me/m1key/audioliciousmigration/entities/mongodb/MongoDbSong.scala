package me.m1key.audioliciousmigration.entities.mongodb
import com.google.code.morphia.annotations.Id
import com.google.code.morphia.annotations.Entity
import org.bson.types.ObjectId
import com.google.code.morphia.annotations.Indexes
import com.google.code.morphia.annotations.Index

@Entity
@Indexes(Array(new Index(value = "name, albumName", unique = true)))
class MongoDbSong() {

  @Id
  var id: ObjectId = _
  
  // This cannot be a constructor field, because the index is then not created.
  var name: String = _
  
  var albumName: String = _
  
  override def equals(that: Any) = {
    that match {
      case that: MongoDbSong => that.name == this.name &&
    		  that.albumName == this.albumName
      case _ => false
    }
  }

  override def hashCode(): Int = name.hashCode()

}