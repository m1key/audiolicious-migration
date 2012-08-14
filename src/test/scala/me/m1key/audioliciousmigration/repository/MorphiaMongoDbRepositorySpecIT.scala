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
      val song = new MongoDbSong("Heathen Child", "Grinderman 2", "Grinderman", "12")
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
      val song1 = new MongoDbSong("Heathen Child", "Grinderman 2", "Grinderman", "12")
      repository.save(song1)
      val song2 = new MongoDbSong("Worm Tamer", "Grinderman 2", "Grinderman", "12")
      repository.save(song2)
      songsCount mustBe 2
    }

    "fail if duplicate song is inserted." in {
      songsCount mustBe 0
      val song1 = new MongoDbSong("When My Baby Comes", "Grinderman 2", "Grinderman", "12")
      repository.save(song1)
      val song2 = new MongoDbSong("When My Baby Comes", "Grinderman 2", "Grinderman", "12")
      song1 mustEqual song2
      repository.save(song2) must throwA[MongoException]
    }

    "insert two songs with same title, different albums." in {
      songsCount mustBe 0
      val song1 = new MongoDbSong("When My Baby Comes", "Grinderman 2", "Grinderman", "12")
      repository.save(song1)
      val song2 = new MongoDbSong("When My Baby Comes", "Grinderman II", "Grinderman", "12")
      song1 must not(beEqual(song2))
      repository.save(song2)
      songsCount mustBe 2
    }

    "insert two songs with same title, same albums, different artists." in {
      songsCount mustBe 0
      val song1 = new MongoDbSong("When My Baby Comes", "Grinderman 2", "Grinderman", "12")
      repository.save(song1)
      val song2 = new MongoDbSong("When My Baby Comes", "Grinderman 2", "GrinderMan", "12")
      song1 must not(beEqual(song2))
      repository.save(song2)
      songsCount mustBe 2
    }

    "insert two songs with different song key." in {
      songsCount mustBe 0
      val song1 = new MongoDbSong("When My Baby Comes", "Grinderman 2", "Grinderman", "12")
      repository.save(song1)
      val song2 = new MongoDbSong("When My Baby Comes", "Grinderman 2", "Grinderman", "13")
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