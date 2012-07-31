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

  "Library with two artist and some albums each" should {
    "have two artists and correct number of albums" in {
      val library = new MongoDbLibrary
      library.addArtist(new MongoDbArtist("Marilyn Manson")).addAlbum(new MongoDbAlbum("Mechanical Animals"))
      library.addArtist(new MongoDbArtist("Marilyn Manson")).addAlbum(new MongoDbAlbum("Holy Wood"))
      library.addArtist(new MongoDbArtist("Monster Magnet")).addAlbum(new MongoDbAlbum("Spine Of God"))
      library.artists.size() mustBe 2
      
      var mansonChecked = false
      var monsterChecked = false
      val iterator = library.artists.iterator()
      while (iterator.hasNext()) {
        val artist = iterator.next()
        if (artist.name == "Marilyn Manson") {
          mansonChecked = true
          println(artist.albums.iterator().next().name)
          artist.albums.size() mustBe 2
        } else if (artist.name == "Monster Magnet") {
          monsterChecked = true
          artist.albums.size() mustBe 1
        }
      }
      mansonChecked mustBe true
      monsterChecked mustBe true
    }
  }

}