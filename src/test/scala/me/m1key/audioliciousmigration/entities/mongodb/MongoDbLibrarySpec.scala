package me.m1key.audioliciousmigration.entities.mongodb
import org.specs._
import org.specs.runner._
import org.junit.runner.RunWith

@RunWith(classOf[JUnitSuiteRunner])
class MongoDbLibrarySpec extends Specification with JUnit {

  "Empty library" should {
    "have no artists" in {
      val library = new MongoDbLibrary
      library.artists.size() mustBe 0
    }
  }

  "Library with one artist" should {
    "have one artist" in {
      val library = new MongoDbLibrary
      library.addArtist(new MongoDbArtist("Marilyn Manson"))
      library.artists.size() mustBe 1
    }
    "have one artist when same artist added twice" in {
      val library = new MongoDbLibrary
      library.addArtist(new MongoDbArtist("Marilyn Manson"))
      library.addArtist(new MongoDbArtist("Marilyn Manson"))
      library.artists.size() mustBe 1
    }
  }

  "Library with two artist" should {
    "have two artists" in {
      val library = new MongoDbLibrary
      library.addArtist(new MongoDbArtist("Marilyn Manson"))
      library.addArtist(new MongoDbArtist("Monster Magnet"))
      library.artists.size() mustBe 2
    }
  }

}