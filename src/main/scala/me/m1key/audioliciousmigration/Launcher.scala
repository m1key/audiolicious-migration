package me.m1key.audioliciousmigration
import com.google.inject.Guice
import me.m1key.audioliciousmigration.persistence.AudioliciousPersistenceProvider
import me.m1key.audiolicious.domain.entities.Library
import com.google.inject.Injector
import me.m1key.audioliciousmigration.repository.LibraryRepository
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import me.m1key.audioliciousmigration.mining.SongPerArtistMining
import me.m1key.audioliciousmigration.mining.SongPerYearMining
import me.m1key.audioliciousmigration.mining.AlbumsPerArtistMining
import me.m1key.audioliciousmigration.mining.AlbumsPerGenreMining
import me.m1key.audioliciousmigration.mining.ArtistCountMining
import me.m1key.audioliciousmigration.mining.SongCountMining

object Launcher {

  def main(args: Array[String]): Unit = {
    println("Audiolicious Importer")

    val injector = Guice.createInjector(new AudioliciousMigrationModule)
    val importer = injector.getInstance(classOf[AudioliciousImporter])
    val persistenceProvider = injector.getInstance(classOf[AudioliciousPersistenceProvider])
    val libraryRepository = injector.getInstance(classOf[LibraryRepository])
    val mongoDbPersistence = injector.getInstance(classOf[MorphiaMongoDbPersistenceProvider])
    
    val songsPerArtistMining = injector.getInstance(classOf[SongPerArtistMining])
    val songsPerYearMining = injector.getInstance(classOf[SongPerYearMining])
    val albumsPerArtistMining = injector.getInstance(classOf[AlbumsPerArtistMining])
    val albumsPerGenreMining = injector.getInstance(classOf[AlbumsPerGenreMining])
    val artistCountMining = injector.getInstance(classOf[ArtistCountMining])
    val songCountMining = injector.getInstance(classOf[SongCountMining])

    persistenceProvider.initialise
    val entityManager = persistenceProvider.getEntityManager
    entityManager.getTransaction().begin

    mongoDbPersistence.initialise

    val library = resolveLibrary(libraryRepository, args)
    println("Migrating library [%s].".format(library.getUuid()))
    importer.importLibrary(library.getUuid)

    entityManager.getTransaction().commit
    persistenceProvider.close
    
    println("Library imported.")
    
    var artistCount = ""
    artistCountMining.mine() match {
      case s: Some[Int] => artistCount = "" + s.get
      case None => artistCount = "?"
    }
    var songCount = ""
    songCountMining.mine() match {
      case s: Some[Int] => songCount = "" + s.get
      case None => songCount = "?"
    }
    println("Complete library has [%s] artists, [%s] songs.".format(artistCount, songCount))
    println("Top artists by songs count:")
    println(songsPerArtistMining.mine(10))
    println("Top artists by album count:")
    println(albumsPerArtistMining.mine(10))
    println("Top years by songs count:")
    println(songsPerYearMining.mineTop(10))
    println("Bottom years by songs count:")
    println(songsPerYearMining.mineBottom(10))
    println("Albums by genre count:")
    println(albumsPerGenreMining.mine())

    println("Bye.")
  }

  def resolveLibrary(libraryRepository: LibraryRepository, args: Array[String]): Library = {
    if (args.length > 0) {
      val libraryUuid = args(0)
      println("Library UUID specified. Obtaining library [%s]...".format(libraryUuid))
      libraryRepository.getLibrary(libraryUuid) match {
        case Some(library) => return library
        case None => throw new RuntimeException("Library [%s] not found.".format(libraryUuid))
      }
    } else {
      println("Library UUID not specified. Obtaining most recent library...")
      libraryRepository.getLatestLibrary match {
        case Some(library) => return library
        case None => throw new RuntimeException("There are no libraries in the database.")
      }
    }
  }

}