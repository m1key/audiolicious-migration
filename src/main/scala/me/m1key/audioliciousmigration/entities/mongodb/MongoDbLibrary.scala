package me.m1key.audioliciousmigration.entities.mongodb

import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Id
import org.bson.types.ObjectId

@Entity
class MongoDbLibrary {
  @Id
  var id: ObjectId = _

  var uuid: String = _
}