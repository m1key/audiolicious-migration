package me.m1key.audioliciousmigration.repository
import org.specs.Specification
import org.specs.runner.JUnit
import org.junit.runner.RunWith
import org.specs.runner.JUnitSuiteRunner
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbSong
import com.mongodb.MongoException

@RunWith(classOf[JUnitSuiteRunner])
class MorphiaMongoDbRepositorySpecIT extends Specification with JUnit {

  val morphiaPersistenceProvider = new TestMorphiaMongoDbPersistenceProvider
  morphiaPersistenceProvider.initialise
  val repository = new MorphiaMongoDbRepository(morphiaPersistenceProvider)
  val datastore = morphiaPersistenceProvider.getDatastore()

  doBeforeSpec {
    deleteSongs
  }

  "Creating song" should {
    doFirst {
      println("Preparing test 1...")
      deleteSongs
      println("Test prepared. Songs: %d".format(songsCount))
    }

    "increase the number of rows by one." in {
      songsCount mustBe 0
      val song = new MongoDbSong
      song.name = "Heathen Child"
      song.albumName = "Grinderman 2"
      repository.save(song)
      songsCount mustBe 1
    }

    doLast {
      println("Cleaning up...")
      deleteSongs
      println("Cleaned up. Songs: %d".format(songsCount))
    }
  }

  "Creating two songs" should {
    doBefore {
      println("Preparing test 2...")
      deleteSongs
      println("Test prepared. Songs: %d".format(songsCount))
    }

    "increase the number of rows by two." in {
      songsCount mustBe 0
      val song1 = new MongoDbSong
      song1.name = "Heathen Child"
      song1.albumName = "Grinderman 2"
      repository.save(song1)
      val song2 = new MongoDbSong
      song2.name = "Worm Tamer"
      song2.albumName = "Grinderman 2"
      repository.save(song2)
      songsCount mustBe 2
    }

    "fail if duplicate song is inserted." in {
      songsCount mustBe 0
      val song1 = new MongoDbSong
      song1.name = "When My Baby Comes"
      song1.albumName = "Grinderman 2"
      repository.save(song1)
      val song2 = new MongoDbSong
      song2.name = "When My Baby Comes"
      song2.albumName = "Grinderman 2"
      song1 mustEqual song2
      repository.save(song2) must throwA[MongoException]
    }

    "insert two songs with same title, different albums." in {
      songsCount mustBe 0
      val song1 = new MongoDbSong
      song1.name = "When My Baby Comes"
      song1.albumName = "Grinderman 2"
      repository.save(song1)
      val song2 = new MongoDbSong
      song2.name = "When My Baby Comes"
      song2.albumName = "Grinderman II"
      song1 must not(beEqual(song2))
      repository.save(song2)
      songsCount mustBe 2
    }

    doLast {
      println("Cleaning up...")
      deleteSongs
      println("Cleaned up. Songs: %d".format(songsCount))
    }
  }

  private def deleteSongs = {
    println("	Deleting songs...")
    datastore.delete(datastore.createQuery(classOf[MongoDbSong]))
    println("	Songs deleted.")
  }

  private def songsCount: Int = {
    return datastore.createQuery(classOf[MongoDbSong]).countAll().toInt
  }

}