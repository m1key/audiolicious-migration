package me.m1key.audioliciousmigration.to

class SongTo(val songUuid: String, val songName: String, val songGenre: String, val albumName: String, val songArtistName: String, val artistName: String,
  val playCount: Int, val skipCount: Int, val percentage: Int, val songYear: Int) {
}