package me.m1key.audioliciousmigration.importer

import me.m1key.audioliciousmigration.AudioliciousImporter
import me.m1key.audioliciousmigration.repository.LibraryRepository
import com.google.inject.Inject
import me.m1key.audioliciousmigration.feeder.Feeder

private[audioliciousmigration] class RelativeDataImporter @Inject() (private val feeder: Feeder, private val libraryRepository: LibraryRepository) extends AudioliciousImporter {

  def importLibrary(libraryUuid: String): Unit = {
    println("Importing library [%s]...".format(libraryUuid));
    libraryRepository.getLatestLibrary match {
      case Some(library) => println("Library [%s] imported.".format(libraryUuid));
      	feeder.feed(library)
      case None => throw new RuntimeException("Library [%s] not found.".format(libraryUuid))
    }
  }

}