package me.m1key.audioliciousmigration
import com.google.inject.Guice
import me.m1key.audioliciousmigration.persistence.PersistenceProvider
import me.m1key.audiolicious.domain.entities.Library
import com.google.inject.Injector
import me.m1key.audioliciousmigration.repository.LibraryRepository

object Launcher {

  def main(args: Array[String]): Unit = {
    println("Audiolicious Importer")

    val injector = Guice.createInjector(new AudioliciousMigrationModule)
    val importer = injector.getInstance(classOf[AudioliciousImporter])
    val persistenceProvider = injector.getInstance(classOf[PersistenceProvider])
    val libraryRepository = injector.getInstance(classOf[LibraryRepository])

    persistenceProvider.initialise
    val entityManager = persistenceProvider.getEntityManager
    entityManager.getTransaction().begin()

    val library = resolveLibrary(libraryRepository, args)
    println("Migrating library [%s].".format(library.getUuid()))
    importer.importLibrary(library.getUuid)

    entityManager.getTransaction().commit()
    persistenceProvider.close

    println("Bye.")
  }

  def resolveLibrary(libraryRepository: LibraryRepository, args: Array[String]): Library = {
    if (args.length > 0) {
      val libraryUuid = args(0)
      println("Library UUID specified. Obtaining library [%s]...".format(libraryUuid))
      return libraryRepository.getLibrary(libraryUuid)
    } else {
      println("Library UUID not specified. Obtaining most recent library...")
      return libraryRepository.getLatestLibrary
    }
  }

}