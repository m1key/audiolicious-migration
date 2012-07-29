package me.m1key.audioliciousmigration.feeder

import me.m1key.audiolicious.domain.entities.Library
import com.google.inject.Inject
import me.m1key.audioliciousmigration.exporter.MongoDbExporter
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbLibrary
import me.m1key.audioliciousmigration.repository.SongRepository
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbArtist

private[audioliciousmigration] class MongoDbFeeder @Inject() (private val exporter: MongoDbExporter, private val songRepository: SongRepository) extends Feeder {

  def feed(library: Library): Unit = {
    val mongoDbLibrary = new MongoDbLibrary
    mongoDbLibrary.uuid = library.getUuid()
    println("Created MongoDb library [%s].".format(mongoDbLibrary.uuid));
    val songs = songRepository.getAllSongsWithStatsByLibraryUuid(mongoDbLibrary.uuid);
    println("Obtained [%d] songs.".format(songs.size))
    for (song <- songs) {
      mongoDbLibrary.addArtist(new MongoDbArtist(song.artistName))
    }
    exporter.export(mongoDbLibrary)
  }

}