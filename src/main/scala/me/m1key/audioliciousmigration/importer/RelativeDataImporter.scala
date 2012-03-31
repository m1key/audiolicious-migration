package me.m1key.audioliciousmigration.importer

import me.m1key.audioliciousmigration.AudioliciousImporter

class RelativeDataImporter extends AudioliciousImporter {

  def importLibrary(libraryUuid: String): Unit = {
    println("Importing library [%s]...".format(libraryUuid));
    println("Library [%s] imported.".format(libraryUuid));
  }

}