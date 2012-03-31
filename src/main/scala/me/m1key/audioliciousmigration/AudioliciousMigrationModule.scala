package me.m1key.audioliciousmigration
import com.google.inject.AbstractModule
import com.google.inject.Provides
import me.m1key.audioliciousmigration.importer.RelativeDataImporter
import me.m1key.audioliciousmigration.feeder.Feeder
import me.m1key.audioliciousmigration.feeder.DefaultFeeder
import me.m1key.audioliciousmigration.exporter.Exporter
import me.m1key.audioliciousmigration.exporter.MongoDbExporter

class AudioliciousMigrationModule extends AbstractModule {

  @Override
  protected def configure() {
    bind(classOf[AudioliciousImporter]).to(classOf[RelativeDataImporter])
    bind(classOf[Feeder]).to(classOf[DefaultFeeder])
    bind(classOf[Exporter]).to(classOf[MongoDbExporter])
  }

} 