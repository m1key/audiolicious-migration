package me.m1key.audioliciousmigration.repository
import org.specs.Specification
import org.specs.runner.JUnit
import org.junit.runner.RunWith
import org.specs.runner.JUnitSuiteRunner
import me.m1key.audioliciousmigration.entities.mongodb.MongoDbLibrary

@RunWith(classOf[JUnitSuiteRunner])
class MorphiaMongoDbRepositorySpecIT extends Specification with JUnit {

  val morphiaPersistenceProvider = new TestMorphiaMongoDbPersistenceProvider
  morphiaPersistenceProvider.initialise
  val repository = new MorphiaMongoDbRepository(morphiaPersistenceProvider)
  val datastore = morphiaPersistenceProvider.getDatastore()

  doBeforeSpec {
    deleteLibraries
  }

  "Creating library" should {
    doFirst {
      println("Preparing test 1...")
      deleteLibraries
      println("Test prepared. Libraries: %d".format(librariesCount))
    }

    "increase the number of rows by one." in {
      librariesCount mustBe 0
      val library = new MongoDbLibrary
      library.uuid = "Test UUID"
      repository.save(library)
      librariesCount mustBe 1
    }

    doLast {
      println("Cleaning up...")
      deleteLibraries
      println("Cleaned up. Libraries: %d".format(librariesCount))
    }
  }

  private def deleteLibraries = {
    println("	Deleting libraries...")
    datastore.delete(datastore.createQuery(classOf[MongoDbLibrary]))
    println("	Libraries deleted.")
  }

  private def librariesCount: Int = {
    return datastore.createQuery(classOf[MongoDbLibrary]).countAll().toInt
  }

}