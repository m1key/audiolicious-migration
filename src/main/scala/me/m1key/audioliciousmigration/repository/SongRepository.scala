package me.m1key.audioliciousmigration.repository
import me.m1key.audioliciousmigration.to.SongTo

trait SongRepository {

  def getAllSongsWithStatsByLibraryUuid(libraryUuid: String): Collection[SongTo]

}