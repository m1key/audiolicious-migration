package me.m1key.audioliciousmigration
import com.google.inject.Guice

object Launcher {

  def main(args: Array[String]): Unit = {
    println("Audiolicious Importer")
    
    val libraryUuid = resolveLibraryUuid(args)
    println("Migrating library [%s].".format(libraryUuid))
    
    val injector = Guice.createInjector(new AudioliciousMigrationModule)
    val importer = injector.getInstance(classOf[AudioliciousImporter])
    importer.importLibrary("UUID")
    
    println("Bye.")
  }
  
  def resolveLibraryUuid(args: Array[String]): String = {
    if (args.length > 0) {
      return args(0)
    } else {
      println("Library UUID not specified. Obtaining most recent library...")
      return "TODO"
    }
  }

}