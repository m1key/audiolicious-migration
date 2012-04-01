package me.m1key.audioliciousmigration.persistence
import javax.persistence.EntityManager

trait PersistenceProvider {
  
  def initialise: Unit
  def getEntityManager: EntityManager
  def close: Unit

}