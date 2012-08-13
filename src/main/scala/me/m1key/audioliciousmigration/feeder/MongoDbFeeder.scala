package me.m1key.audioliciousmigration.feeder

import me.m1key.audiolicious.domain.entities.Library
import com.google.inject.Inject
import me.m1key.audioliciousmigration.exporter.MongoDbExporter
import me.m1key.audioliciousmigration.repository.SongRepository

private[audioliciousmigration] class MongoDbFeeder @Inject() (private val exporter: MongoDbExporter, private val songRepository: SongRepository) extends Feeder {

  def feed(library: Library): Unit = {
    val songs = songRepository.getAllSongsWithStatsByLibraryUuid(library.getUuid());
    println("Obtained [%d] songs.".format(songs.size))
    for (song <- songs) {
      // TODO create and export MongoDbSong.
      //mongoDbLibrary.addArtist(new MongoDbArtist(song.artistName)).addAlbum(new MongoDbAlbum(song.albumName))
      //exporter.export(mongoDbSong)
    }
  }

}