package me.m1key.audioliciousmigration.repository
import me.m1key.audiolicious.domain.entities.Library

trait LibraryRepository {

  def getLibrary(libraryUuid: String): Option[Library]
  def getLatestLibrary: Option[Library]

}