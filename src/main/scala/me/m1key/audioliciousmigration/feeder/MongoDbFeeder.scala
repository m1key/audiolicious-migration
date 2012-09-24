package me.m1key.audioliciousmigration.feeder

import me.m1key.audiolicious.domain.entities.Library
import com.google.inject.Inject
import me.m1key.audioliciousmigration.exporter.MongoDbExporter
import me.m1key.audioliciousmigration.repository.SongRepository
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbSong

private[audioliciousmigration] class MongoDbFeeder @Inject() (private val exporter: MongoDbExporter, private val songRepository: SongRepository) extends Feeder {

  def feed(library: Library): Unit = {
    val songs = songRepository.getAllSongsWithStatsByLibraryUuid(library.getUuid());
    println("Obtained [%d] songs.".format(songs.size))
    for (song <- songs) {
      val mongoDbSong = new MongoDbSong(song.songName, song.albumName, song.artistName, song.songKey)
      mongoDbSong.genre = song.songGenre
      mongoDbSong.year = song.songYear
      mongoDbSong.songArtistName = song.songArtistName
      
      mongoDbSong.addOrEditStats(library.getUuid(), song.percentage, song.playCount)
      
      exporter.export(mongoDbSong)
    }
  }

}