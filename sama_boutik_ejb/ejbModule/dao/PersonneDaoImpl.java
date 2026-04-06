package dao;

import entity.Personne;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

@Stateless
public class PersonneDaoImpl implements PersonneDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(Personne personne) {
        em.persist(personne);
    }

    @Override
    public List<Personne> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Personne> cq = cb.createQuery(Personne.class);
        Root<Personne> root = cq.from(Personne.class);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }

    @Override
    public Personne findByEmail(String email) {
        try {
            return em.createQuery("SELECT p FROM Personne p WHERE p.email = :email", Personne.class)
                     .setParameter("email", email)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void update(Personne personne) {
        em.merge(personne);
    }

    @Override
    public void delete(Long id) {
        Personne p = em.find(Personne.class, id);
        if (p != null) em.remove(p);
    }
    
    @Override
    public Personne findById(Long id) {
        return em.find(Personne.class, id);
    }
    
    // ✅ AJOUT : Rechercher uniquement les personnes actives
    @Override
    public List<Personne> findAllActives() {
        try {
            return em.createQuery(
                "SELECT p FROM Personne p WHERE p.actif = true ORDER BY p.nom, p.prenom", 
                Personne.class)
                .getResultList();
        } catch (Exception e) {
            return findAll(); // Fallback si la colonne actif n'existe pas
        }
    }

}
