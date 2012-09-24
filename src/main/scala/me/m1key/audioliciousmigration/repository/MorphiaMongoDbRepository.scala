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

    val selectQuery = datastore.createQuery(classOf[MongoDbSong])
      .field("name").equal(song.name).field("albumName").equal(song.albumName)
      .field("artistName").equal(song.artistName).field("songKey").equal(song.songKey);
    val existingSong = selectQuery.get()

    if (existingSong != null) {
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

      val statLibrary = song.statsList.get(0)
      val statLibraryUuid = statLibrary.libraryUuid
      existingSong.addOrEditStats(statLibrary.libraryUuid)
      ops.set("statsList", song.statsList)

      val updatedCount = datastore.update(selectQuery, ops).getUpdatedCount()

      if (updatedCount != 1) {
        println("Warning. Song updated [%d] times while expected once: [%s]".format(updatedCount, song))
      }
    } else {
      datastore.save(List(song).asJava)
    }
  }

}