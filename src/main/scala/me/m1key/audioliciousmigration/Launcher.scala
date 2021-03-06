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
import me.m1key.audioliciousmigration.mining.AlbumCountMining
import me.m1key.audioliciousmigration.mining.SongPlayCountMining
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbSong
import me.m1key.audioliciousmigration.mining.SongSkipCountMining
import me.m1key.audioliciousmigration.mining.MostPlayedAlbumsMining
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbStats
import me.m1key.audioliciousmigration.mining.MostPlayedAlbumsPerSongMining
import me.m1key.audioliciousmigration.mining.HighestRatedAlbumMining
import me.m1key.audioliciousmigration.mining.LowestRatedAlbumMining
import me.m1key.audioliciousmigration.mining.HighestRatedGenreMining
import me.m1key.audioliciousmigration.mining.HighestRatedArtistMining
import me.m1key.audioliciousmigration.mining.LowestRatedArtistMining
import me.m1key.audioliciousmigration.mining.HighestPlayCountContrastAlbumMining
import me.m1key.audioliciousmigration.mining.LowestPlayCountContrastAlbumMining
import me.m1key.audioliciousmigration.mining.LowestPlayCountContrastAlbumMining

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
    val albumCountMining = injector.getInstance(classOf[AlbumCountMining])
    val songCountMining = injector.getInstance(classOf[SongCountMining])
    val songPlayCountMining = injector.getInstance(classOf[SongPlayCountMining])
    val songSkipCountMining = injector.getInstance(classOf[SongSkipCountMining])
    val mostPlayedAlbumsMining = injector.getInstance(classOf[MostPlayedAlbumsMining])
    val mostPlayedAlbumsPerSongMining = injector.getInstance(classOf[MostPlayedAlbumsPerSongMining])
    val highestRatedAlbumMining = injector.getInstance(classOf[HighestRatedAlbumMining])
    val lowestRatedAlbumMining = injector.getInstance(classOf[LowestRatedAlbumMining])
    val highestRatedGenreMining = injector.getInstance(classOf[HighestRatedGenreMining])
    val highestRatedArtistMining = injector.getInstance(classOf[HighestRatedArtistMining])
    val lowestRatedArtistMining = injector.getInstance(classOf[LowestRatedArtistMining])
    val highestPlayCountContrastAlbumMining = injector.getInstance(classOf[HighestPlayCountContrastAlbumMining])
    val lowestPlayCountContrastAlbumMining = injector.getInstance(classOf[LowestPlayCountContrastAlbumMining])

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
      case s: Some[_] => artistCount = "" + s.get
      case None => artistCount = "?"
    }
    var albumCount = ""
    albumCountMining.mine() match {
      case s: Some[_] => albumCount = "" + s.get
      case None => albumCount = "?"
    }
    var songCount = ""
    songCountMining.mine() match {
      case s: Some[_] => songCount = "" + s.get
      case None => songCount = "?"
    }
    println("Complete library has [%s] artists, [%s] albums, [%s] songs.".format(artistCount, albumCount, songCount))
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
    println("Top songs by play count:")
    printlnSongs(songPlayCountMining.mine(10, library.getUuid), library.getUuid, playCount)
    println("Top songs by skip count:")
    printlnSongs(songSkipCountMining.mine(10, library.getUuid), library.getUuid, skipCount)
    println("Most played albums:")
    println(mostPlayedAlbumsMining.mine(10, library.getUuid))
    println("Most played albums, average per song:")
    println(mostPlayedAlbumsPerSongMining.mine(10, library.getUuid))
    println("Highest rated albums:")
    println(highestRatedAlbumMining.mine(10, library.getUuid))
    println("Lowest rated albums:")
    println(lowestRatedAlbumMining.mine(10, library.getUuid))
    println("Highest rated genres:")
    println(highestRatedGenreMining.mine(library.getUuid))
    println("Highest rated artists:")
    println(highestRatedArtistMining.mine(10, library.getUuid))
    println("Lowest rated artists:")
    println(lowestRatedArtistMining.mine(10, library.getUuid))
    println("Highest play count contrast albums:")
    println(highestPlayCountContrastAlbumMining.mine(10, library.getUuid))
    println("Lowest play count contrast albums:")
    println(lowestPlayCountContrastAlbumMining.mine(10, library.getUuid))

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

  def skipCount(stats: MongoDbStats) = "" + stats.skipCount
  def playCount(stats: MongoDbStats) = "" + stats.playCount

  def printlnSongs(songs: List[MongoDbSong], libraryUuid: String, f: (MongoDbStats) => String): Unit = {
    songs.indices.foreach(i => print("%d. %s (%s, %s).  ".format(i + 1, songs(i).name, f(songs(i).getStatsForLibraryUuid(libraryUuid).get), songs(i).songArtistName)))
    println
  }

}