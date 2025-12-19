package fr.diginamic.hello.repositories;

import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class VilleDao {

  @PersistenceContext
  private EntityManager em;

  public Ville insert(Ville ville) {
    em.persist(ville);
    return ville;
  }

  public Ville update(Ville ville) {
    return em.merge(ville);
  }

  public void delete(Ville ville) {
    if (!em.contains(ville)) {
      ville = em.merge(ville);
    }
    em.remove(ville);
  }

  public Ville findById(int id) {
    return em.find(Ville.class, id);
  }

  public List<Ville> findAll() {
    TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v", Ville.class);
    return query.getResultList();
  }

  public boolean existsByNomAndDepartement(String nom, Departement departement) {
    TypedQuery<Long> query = em.createQuery(
      "SELECT COUNT(v) FROM Ville v WHERE v.nom = :nom AND v.departement = :dept", Long.class
    );
    query.setParameter("nom", nom);
    query.setParameter("dept", departement);
    Long count = query.getSingleResult();
    return count > 0;
  }

  public List<Ville> findByNomStartingWith(String prefix) {
    TypedQuery<Ville> query = em.createQuery(
      "SELECT v FROM Ville v WHERE v.nom LIKE :prefix", Ville.class
    );
    query.setParameter("prefix", prefix + "%");
    return query.getResultList();
  }

  public List<Ville> findByPopulationBetween(int min, Integer max) {
    String jpql = "SELECT v FROM Ville v WHERE v.population >= :min";
    if (max != null) {
      jpql += " AND v.population <= :max";
    }
    TypedQuery<Ville> query = em.createQuery(jpql, Ville.class);
    query.setParameter("min", min);
    if (max != null) query.setParameter("max", max);
    return query.getResultList();
  }

  public List<Ville> findByDepartementOrderByPopulationDesc(Departement dep) {
    String jpql = "SELECT v FROM Ville v WHERE v.departement = :dep ORDER BY v.population DESC";
    TypedQuery<Ville> query = em.createQuery(jpql, Ville.class);
    query.setParameter("dep", dep);
    return query.getResultList();
  }

  public List<Ville> findByDepartement(Departement dep) {
    String jpql = "SELECT v FROM Ville v WHERE v.departement = :dep";
    TypedQuery<Ville> query = em.createQuery(jpql, Ville.class);
    query.setParameter("dep", dep);
    return query.getResultList();
  }
}
