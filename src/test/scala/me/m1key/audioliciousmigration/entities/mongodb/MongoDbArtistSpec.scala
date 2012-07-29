package me.m1key.audioliciousmigration.entities.mongodb
import org.specs._
import org.specs.runner._
import org.junit.runner.RunWith

@RunWith(classOf[JUnitSuiteRunner])
class MongoDbArtistSpec extends Specification with JUnit {

  "Same object" should {
    "equal itself" in {
      val artist = new MongoDbArtist("Metallica")
      artist mustEqual artist
    }
  }

  "Different objects with same name" should {
    "equal each other" in {
      val artist = new MongoDbArtist("Metallica")
      val artist2 = new MongoDbArtist("Metallica")
      artist mustEqual artist2
      artist2 mustEqual artist
    }
  }

  "Different objects with different name" should {
    "not equal each other" in {
      val artist = new MongoDbArtist("Monster Magnet")
      val artist2 = new MongoDbArtist("Metallica")
      artist must not(beEqual(artist2))
      artist2 must not(beEqual(artist))
    }
  }

}