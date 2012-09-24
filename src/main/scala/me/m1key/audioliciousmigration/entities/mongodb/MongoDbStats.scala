package me.m1key.audioliciousmigration.entities.mongodb;
import com.google.code.morphia.annotations.Embedded

@Embedded
class MongoDbStats(var libraryUuid: String) {

	  // No-args constructor to be used by Morphia.
	  def this() {
	    this("libraryUuid to be set")
	  }

}
