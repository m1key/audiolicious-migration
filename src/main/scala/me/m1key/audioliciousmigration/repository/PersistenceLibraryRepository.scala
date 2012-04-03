package me.m1key.audioliciousmigration.repository

import me.m1key.audiolicious.domain.entities.Library
import com.google.inject.Inject
import me.m1key.audioliciousmigration.persistence.PersistenceProvider
import javax.persistence.EntityManager

class PersistenceLibraryRepository @Inject() (private val persistenceProvider: PersistenceProvider) extends LibraryRepository {

  @Override
  def getLibrary(libraryUuid: String): Option[Library] = {
    val entityManager = persistenceProvider.getEntityManager

    if (libraryExists(entityManager, libraryUuid)) {
      val library: Library = entityManager.createQuery("SELECT l FROM Library l WHERE l.uuid = :uuid", classOf[Library]).setParameter("uuid", libraryUuid).getSingleResult()
      return Some(library)
    } else {
      return None
    }
  }

  @Override
  def getLatestLibrary(): Option[Library] = {
    val entityManager = persistenceProvider.getEntityManager

    if (anyLibrariesExist(entityManager)) {
      val library: Library = entityManager.createQuery("SELECT l FROM Library l ORDER BY l.dateAdded DESC", classOf[Library]).setMaxResults(1).getSingleResult()
      return Some(library)
    } else {
      return None
    }
  }

  private def anyLibrariesExist(entityManager: EntityManager): Boolean = {
    val sql = "SELECT l FROM Library l"
    val query = entityManager.createQuery(sql);
    query.setMaxResults(1);
    val result = query.getResultList()

    return result.size() > 0
  }

  private def libraryExists(entityManager: EntityManager, libraryUuid: String): Boolean = {
    val sql = "SELECT l FROM Library l WHERE l.uuid = :uuid"
    val query = entityManager.createQuery(sql).setParameter("uuid", libraryUuid);
    query.setMaxResults(1);
    val result = query.getResultList()

    return result.size() > 0
  }

}