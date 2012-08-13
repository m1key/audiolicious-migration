package me.m1key.audioliciousmigration.repository
import org.specs.Specification
import org.specs.runner.JUnit
import org.junit.runner.RunWith
import org.specs.runner.JUnitSuiteRunner

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
      // TODO song
//      val library = new MongoDbLibrary
//      library.uuid = "Test UUID"
//      repository.save(library)
//      librariesCount mustBe 1
    }

    doLast {
      println("Cleaning up...")
      deleteSongs
      println("Cleaned up. Songs: %d".format(songsCount))
    }
  }

  private def deleteSongs = {
    println("	Deleting songs...")
    // TODO song
//    datastore.delete(datastore.createQuery(classOf[MongoDbLibrary]))
    println("	Songs deleted.")
  }

  private def songsCount: Int = {
    // TODO song
    return 0
//    return datastore.createQuery(classOf[MongoDbLibrary]).countAll().toInt
  }

}