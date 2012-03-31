package me.m1key.audioliciousmigration
import com.google.inject.Guice

object Launcher {

  def main(args: Array[String]): Unit = {
    println("Audiolicious Importer")
    
    val injector = Guice.createInjector(new AudioliciousMigrationModule)
    val importer = injector.getInstance(classOf[AudioliciousImporter])
    importer.importLibrary("UUID")
    
    println("Bye.")
  }

}