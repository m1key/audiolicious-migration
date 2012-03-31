package me.m1key.audioliciousmigration.importer

import me.m1key.audioliciousmigration.AudioliciousImporter
import com.google.inject.Inject
import me.m1key.audioliciousmigration.feeder.Feeder

private[audioliciousmigration] class RelativeDataImporter @Inject() (private val feeder: Feeder) extends AudioliciousImporter {

  def importLibrary(libraryUuid: String): Unit = {
    println("Importing library [%s]...".format(libraryUuid));
    // TODO load library
    println("Library [%s] imported.".format(libraryUuid));
  }

}