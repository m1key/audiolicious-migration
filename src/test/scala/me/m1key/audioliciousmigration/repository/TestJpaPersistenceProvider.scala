package me.m1key.audioliciousmigration.repository

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import me.m1key.audioliciousmigration.persistence.AudioliciousPersistenceProvider

class TestJpaPersistenceProvider(var entityManager: EntityManager, var factory: EntityManagerFactory) extends AudioliciousPersistenceProvider {

  @Override
  def initialise: Unit = {
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