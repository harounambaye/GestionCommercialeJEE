package dao;

import entity.Catalogue;
import entity.Famille;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

@Stateless
public class FamilleDaoImpl implements FamilleDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Famille> listerParCatalogue(Long catalogueId) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Famille> cq = cb.createQuery(Famille.class);
            Root<Famille> root = cq.from(Famille.class);
            
            // Joindre le catalogue pour éviter les lazy loading issues
            root.fetch("catalogue");
            
            cq.select(root)
              .where(cb.equal(root.get("catalogue").get("id"), catalogueId));
            
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des familles : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Famille trouverParId(Long id) {
        return em.find(Famille.class, id);
    }

    @Override
    public void delete(Long id) {
        Famille f = em.find(Famille.class, id);
        if (f != null) {
            em.remove(f);
        }
    }

    @Override
    public void save(Famille f) {
        if (f.getId() == null) {
            em.persist(f);
        } else {
            em.merge(f);
        }
    }

    @Override
    public void modifier(Famille f) {
        em.merge(f);
    }
    
    //NOUVELLES METHODES POUR GESTION FAMILLE
    

    @Override
    public List<Famille> findAll() {
        return em.createQuery("SELECT f FROM Famille f ORDER BY f.nom", Famille.class)
                 .getResultList();
    }

    @Override
    public Famille findById(Long id) {
        return em.find(Famille.class, id);
    }

    @Override
    public void update(Famille famille) {
        em.merge(famille);
    }

    @Override
    public List<Famille> findByCatalogue(Catalogue catalogue) {
        return em.createQuery("SELECT f FROM Famille f WHERE f.catalogue = :cat AND f.etat = true ORDER BY f.nom", Famille.class)
                 .setParameter("cat", catalogue)
                 .getResultList();
    }
}