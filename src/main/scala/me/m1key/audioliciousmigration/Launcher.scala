package me.m1key.audioliciousmigration
import com.google.inject.Guice
import me.m1key.audioliciousmigration.persistence.PersistenceProvider

object Launcher {

  def main(args: Array[String]): Unit = {
    println("Audiolicious Importer")
    
    val injector = Guice.createInjector(new AudioliciousMigrationModule)
    val importer = injector.getInstance(classOf[AudioliciousImporter])
    val persistenceProvider = injector.getInstance(classOf[PersistenceProvider])
    
    persistenceProvider.initialise
    val libraryUuid = resolveLibraryUuid(args)
    println("Migrating library [%s].".format(libraryUuid))
    
    importer.importLibrary("UUID")
    persistenceProvider.close
    
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