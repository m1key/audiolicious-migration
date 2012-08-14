package me.m1key.audioliciousmigration.entities.mongodb
import com.google.code.morphia.annotations.Id
import com.google.code.morphia.annotations.Entity
import org.bson.types.ObjectId
import com.google.code.morphia.annotations.Indexed

@Entity
class MongoDbSong() {

  @Id
  var id: ObjectId = _
  
  // This cannot be a constructor field, because the index is then not created.
  @Indexed(unique = true)
  var name: String = _
  
  override def equals(that: Any) = {
    that match {
      case that: MongoDbSong => that.name == this.name
      case _ => false
    }
  }

  override def hashCode(): Int = name.hashCode()

}