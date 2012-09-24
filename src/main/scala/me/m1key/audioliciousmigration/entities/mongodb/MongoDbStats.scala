package me.m1key.audioliciousmigration.entities.mongodb;
import com.google.code.morphia.annotations.Embedded

@Embedded
class MongoDbStats(val libraryUuid: String, var percentage: Int) {

	  // No-args constructor to be used by Morphia.
	  def this() {
	    this("libraryUuid to be set", 0)
	  }

}
