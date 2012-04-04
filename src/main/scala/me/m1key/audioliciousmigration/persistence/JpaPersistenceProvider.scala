package me.m1key.audioliciousmigration.persistence
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

class JpaPersistenceProvider extends AudioliciousPersistenceProvider {

  private var factory: EntityManagerFactory = _
  private var entityManager: EntityManager = _

  @Override
  def initialise: Unit = {
    factory = Persistence.createEntityManagerFactory("audioliciousPu")
    entityManager = factory.createEntityManager();

  }

  @Override
  def getEntityManager: EntityManager = {
    return entityManager
  }

  @Override
  def close: Unit = {
    entityManager.close();
    factory.close();
  }

}