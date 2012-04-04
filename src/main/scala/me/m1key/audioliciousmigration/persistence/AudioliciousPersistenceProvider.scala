package me.m1key.audioliciousmigration.persistence
import javax.persistence.EntityManager

trait AudioliciousPersistenceProvider {
  
  def initialise: Unit
  def getEntityManager: EntityManager
  def close: Unit

}