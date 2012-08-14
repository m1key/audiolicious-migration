package me.m1key.audioliciousmigration
import com.google.inject.AbstractModule
import com.google.inject.Provides
import me.m1key.audioliciousmigration.importer.RelativeDataImporter
import me.m1key.audioliciousmigration.feeder.Feeder
import me.m1key.audioliciousmigration.feeder.MongoDbFeeder
import me.m1key.audioliciousmigration.exporter.MongoDbExporter
import me.m1key.audioliciousmigration.persistence.AudioliciousPersistenceProvider
import me.m1key.audioliciousmigration.persistence.JpaPersistenceProvider
import com.google.inject.Scopes
import me.m1key.audioliciousmigration.repository.LibraryRepository
import me.m1key.audioliciousmigration.repository.PersistenceLibraryRepository
import me.m1key.audioliciousmigration.persistence.mongodb.MorphiaMongoDbPersistenceProvider
import me.m1key.audioliciousmigration.repository.MorphiaMongoDbRepository
import me.m1key.audioliciousmigration.repository.SongRepository
import me.m1key.audioliciousmigration.repository.PersistenceSongRepository
import me.m1key.audioliciousmigration.persistence.mongodb.ProductionMorphiaMongoDbPersistenceProvider
import me.m1key.audioliciousmigration.mining.SongPerArtistMining
import me.m1key.audioliciousmigration.mining.SongPerYearMining
import me.m1key.audioliciousmigration.mining.AlbumsPerArtistMining
import me.m1key.audioliciousmigration.mining.AlbumsPerGenreMining

class AudioliciousMigrationModule extends AbstractModule {

  @Override
  protected def configure() {
    bind(classOf[AudioliciousImporter]).to(classOf[RelativeDataImporter])
    bind(classOf[Feeder]).to(classOf[MongoDbFeeder])

    bind(classOf[AudioliciousPersistenceProvider]).to(classOf[JpaPersistenceProvider]).in(Scopes.SINGLETON)
    bind(classOf[LibraryRepository]).to(classOf[PersistenceLibraryRepository]).in(Scopes.SINGLETON)
    bind(classOf[SongRepository]).to(classOf[PersistenceSongRepository]).in(Scopes.SINGLETON)
    bind(classOf[MorphiaMongoDbPersistenceProvider]).to(classOf[ProductionMorphiaMongoDbPersistenceProvider]) in (Scopes.SINGLETON)
    bind(classOf[MorphiaMongoDbRepository]).in(Scopes.SINGLETON)
    bind(classOf[MongoDbExporter]).in(Scopes.SINGLETON)
    
    bind(classOf[SongPerArtistMining]).in(Scopes.SINGLETON)
    bind(classOf[SongPerYearMining]).in(Scopes.SINGLETON)
    bind(classOf[AlbumsPerArtistMining]).in(Scopes.SINGLETON)
    bind(classOf[AlbumsPerGenreMining]).in(Scopes.SINGLETON)
  }

}