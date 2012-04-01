package me.m1key.audioliciousmigration.repository

import me.m1key.audiolicious.domain.entities.Library
import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.PersistenceProvider
import javax.persistence.EntityManager

class PersistenceLibraryRepository @Inject() (private val persistenceProvider: PersistenceProvider) extends LibraryRepository {

  def getLibrary(libraryUuid: String): Library = {
    val entityManager = persistenceProvider.getEntityManager

    verifyLibrariesExist(entityManager)
    val library: Library = entityManager.createQuery("SELECT l FROM Library l WHERE l.uuid = :uuid", classOf[Library]).setParameter("uuid", libraryUuid).getSingleResult()
    return library
  }

  def getLatestLibrary(): Library = {
    val entityManager = persistenceProvider.getEntityManager

    verifyLibrariesExist(entityManager)
    val library: Library = entityManager.createQuery("SELECT l FROM Library l ORDER BY l.dateAdded DESC", classOf[Library]).setMaxResults(1).getSingleResult()
    return library
  }

  private def verifyLibrariesExist(entityManager: EntityManager): Unit = {
    val sql = "SELECT l FROM Library l"
    val query = entityManager.createQuery(sql);
    query.setMaxResults(1);
    val result = query.getResultList()

    if (result.size() == 0) {
      throw new RuntimeException("There are no libraries in the database.")
    }
  }

}