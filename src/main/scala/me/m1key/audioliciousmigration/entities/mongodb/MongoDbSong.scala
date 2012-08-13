package me.m1key.audioliciousmigration.entities.mongodb
import com.google.code.morphia.annotations.Id
import com.google.code.morphia.annotations.Entity
import org.bson.types.ObjectId

@Entity
class MongoDbSong {

  @Id
  var id: ObjectId = _

}