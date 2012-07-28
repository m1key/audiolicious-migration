package me.m1key.audioliciousmigration.repository

import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.AudioliciousPersistenceProvider
import me.m1key.audioliciousmigration.to.SongTo
import java.util.ArrayList
import scala.collection.mutable.MutableList

class PersistenceSongRepository @Inject() (private val persistenceProvider: AudioliciousPersistenceProvider) extends SongRepository {

  def getAllSongsWithStatsByLibraryUuid(libraryUuid: String): Collection[SongTo] = {
    val entityManager = persistenceProvider.getEntityManager
    val songs = entityManager.createNativeQuery("select st.PLAY_COUNT, st.PERCENTAGE, st.SKIP_COUNT, st.SONG_UUID, " +
      "so.NAME, so.GENRE, so.YEAR, so.ARTIST_NAME, al.NAME, ar.NAME " +
      "from STATS st left join LIBRARIES l ON st.library_id = l.library_id " +
      "left join SONGS so on st.SONG_UUID = so.UUID " +
      "left join ALBUMS al on so.ALBUM_ID = al.ALBUM_ID " +
      "left join ARTISTS ar on al.ARTIST_ID = ar.ARTIST_ID " +
      "WHERE l.uuid = ?").setParameter(1, libraryUuid).getResultList()
    val songTos = new MutableList[SongTo]

    val it = songs.iterator
    while (it.hasNext()) {
      val cols: Array[Object] = it.next().asInstanceOf[Array[Object]]
      val playCount = Integer.parseInt(cols(0).toString())
      val percentage = Integer.parseInt(cols(1).toString())
      val skipCount = Integer.parseInt(cols(2).toString())
      val songUuid = cols(3).toString()
      val songName = cols(4).toString()
      val songGenre = cols(5).toString()
      val songYear = Integer.parseInt(cols(6).toString())
      val songArtistName = cols(7).toString()
      val albumName = cols(8).toString()
      val artistName = cols(9).toString()
      songTos += new SongTo(songUuid, songName, songGenre, albumName, songArtistName, artistName, playCount, skipCount, percentage, songYear)
    }

    return songTos
  }

}