package me.m1key.audioliciousmigration.repository

import org.specs._
import org.specs.runner._
import org.junit.runner.RunWith
import me.m1key.audioliciousmigration.persistence.JpaPersistenceProvider
import me.m1key.audiolicious.domain.entities.Library
import java.util.Date
import javax.persistence.Persistence

@RunWith(classOf[JUnitSuiteRunner])
class PersistenceLibraryRepositorySpecIT extends Specification with JUnit {

  val factory = Persistence.createEntityManagerFactory("audioliciousPuTest")
  val entityManager = factory.createEntityManager();
  val jpaPersistenceProvider = new TestJpaPersistenceProvider(entityManager, factory)
  jpaPersistenceProvider.initialise
  val repository = new PersistenceLibraryRepository(jpaPersistenceProvider)

  doBeforeSpec {
    deleteLibraries
  }

  "Fetching latest library with no libraries" should {
    var library: Library = null

    doFirst {
      println("Preparing test 1...")
      deleteLibraries
      println("Test prepared. Libraries: %d".format(librariesCount))
    }

    "return None." in {
      entityManager.getTransaction().begin()
      repository.getLatestLibrary() mustBe None
      entityManager.getTransaction().commit()
    }

    doLast {
      println("Cleaning up...")
      deleteLibraries
      println("Cleaned up. Libraries: %d".format(librariesCount))
    }
  }

  "Fetching latest library with only one library" should {
    var library: Library = null

    doFirst {
      println("Preparing test 1...")
      deleteLibraries
      library = insertLibrary
      println("Test prepared. Libraries: %d".format(librariesCount))
    }

    "equal the only library." in {
      entityManager.getTransaction().begin()
      val latestLibrary = repository.getLatestLibrary().get
      entityManager.getTransaction().commit()
      latestLibrary mustBe library
    }

    doLast {
      println("Cleaning up...")
      deleteLibraries
      println("Cleaned up. Libraries: %d".format(librariesCount))
    }
  }

  "Fetching latest library with more than one library" should {
    setSequential()
    var olderLibrary: Library = null
    var anotherOlderLibrary: Library = null
    var newerLibrary: Library = null

    doFirst {
      println("Preparing test 2...")
      deleteLibraries
      olderLibrary = insertLibrary
      Thread.sleep(1000)
      anotherOlderLibrary = insertLibrary
      Thread.sleep(1000)
      newerLibrary = insertLibrary
      println("Test prepared. Libraries: %d".format(librariesCount))
    }

    "equal the newer library." in {
      entityManager.getTransaction().begin()
      val latestLibrary = repository.getLatestLibrary().get
      entityManager.getTransaction().commit()
      println("	Latest library: [%s]".format(latestLibrary))
      latestLibrary mustBe newerLibrary
    }

    "not equal an older library 1." in {
      entityManager.getTransaction().begin()
      val latestLibrary = repository.getLatestLibrary().get
      entityManager.getTransaction().commit()
      latestLibrary mustNotBe olderLibrary
    }

    "not equal an older library 2." in {
      entityManager.getTransaction().begin()
      val latestLibrary = repository.getLatestLibrary().get
      entityManager.getTransaction().commit()
      latestLibrary mustNotBe anotherOlderLibrary
    }

    doLast {
      println("Cleaning up...")
      deleteLibraries
      println("Cleaned up. Libraries: %d".format(librariesCount))
    }
  }

  "Fetching library by UUID with no libraries" should {
    doFirst {
      println("Preparing test 1...")
      deleteLibraries
      println("Test prepared. Libraries: %d".format(librariesCount))
    }

    "return None." in {
      entityManager.getTransaction().begin()
      repository.getLibrary("x") mustBe None
      entityManager.getTransaction().commit()
    }

    doLast {
      println("Cleaning up...")
      deleteLibraries
      println("Cleaned up. Libraries: %d".format(librariesCount))
    }
  }

  "Fetching library by UUID with three libraries" should {
    setSequential()
    var olderLibrary: Library = null
    var anotherOlderLibrary: Library = null
    var newerLibrary: Library = null

    doFirst {
      println("Preparing test 3...")
      deleteLibraries
      olderLibrary = insertLibrary
      Thread.sleep(1000)
      anotherOlderLibrary = insertLibrary
      Thread.sleep(1000)
      newerLibrary = insertLibrary
      println("Test prepared. Libraries: %d".format(librariesCount))
    }

    "return correct 1st library." in {
      entityManager.getTransaction().begin()
      val library = repository.getLibrary(olderLibrary.getUuid).get
      entityManager.getTransaction().commit()
      library mustBe olderLibrary
    }

    "return correct 2nd library." in {
      entityManager.getTransaction().begin()
      val library = repository.getLibrary(anotherOlderLibrary.getUuid).get
      entityManager.getTransaction().commit()
      library mustBe anotherOlderLibrary
    }

    "return correct 3rd library." in {
      entityManager.getTransaction().begin()
      val library = repository.getLibrary(newerLibrary.getUuid).get
      entityManager.getTransaction().commit()
      library mustBe newerLibrary
    }

    doLast {
      println("Cleaning up...")
      deleteLibraries
      println("Cleaned up. Libraries: %d".format(librariesCount))
    }
  }

  doAfterSpec {
    deleteLibraries
  }

  private def deleteLibraries = {
    println("	Deleting libraries...")
    entityManager.getTransaction().begin()
    entityManager.createQuery("DELETE FROM Stat").executeUpdate()
    entityManager.createQuery("DELETE FROM Library").executeUpdate()
    entityManager.getTransaction().commit()
    println("	Libraries deleted.")
  }

  private def insertLibrary: Library = {
    println("	Inserting library...")
    entityManager.getTransaction().begin()
    val library = new Library(new Date())
    entityManager.getTransaction().commit()
    entityManager.persist(library)
    println("	Library inserted.")
    return library
  }

  private def librariesCount: Int = {
    entityManager.getTransaction().begin()
    val count = entityManager.createQuery("SELECT l FROM Library l", classOf[Library]).getResultList().size()
    entityManager.getTransaction().commit()
    return count
  }

}