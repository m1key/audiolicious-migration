package me.m1key.audioliciousmigration.repository

import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import scalaj.collection.Imports._
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbSong
import com.google.code.morphia.query.UpdateOperations
import com.google.code.morphia.query.Query

class MorphiaMongoDbRepository @Inject() (private val persistenceProvider: MorphiaMongoDbPersistenceProvider) {
  

  def save(song: MongoDbSong): Unit = {
    val datastore = persistenceProvider.getDatastore()
    val updateQuery: Query[MongoDbSong] = datastore.createQuery(classOf[MongoDbSong])
    		.field("name").equal(song.name).field("albumName").equal(song.albumName)
    		.field("artistName").equal(song.artistName).field("songKey").equal(song.songKey);
    var ops = datastore.createUpdateOperations(classOf[MongoDbSong])
    if (song.genre != null) {
      ops = ops.set("genre", song.genre)
    }
    if (song.year != 0) {
      ops = ops.set("year", song.year)
    }
    if (song.songArtistName != null) {
      ops = ops.set("songArtistName", song.songArtistName)
    }
    val updatedCount = datastore.update(updateQuery, ops).getUpdatedCount()
    if (updatedCount < 1) {
      datastore.save(List(song).asJava)
    } else if (updatedCount > 2) {
      println("Warning. Song updated more [%d] times: [%s]".format(updatedCount, song))
    }
  }

}