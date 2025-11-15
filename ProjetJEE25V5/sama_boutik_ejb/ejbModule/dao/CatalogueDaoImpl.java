package dao;

import entity.Catalogue;
import entity.Famille;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;

@Stateless
public class CatalogueDaoImpl implements CatalogueDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Catalogue> listerTous() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Catalogue> cq = cb.createQuery(Catalogue.class);
        cq.from(Catalogue.class);
        return em.createQuery(cq).getResultList();
    }

    @Override
    public Catalogue trouverParId(Long id) {
        return em.find(Catalogue.class, id);
    }

    @Override
    public void delete(Long id) {
        Catalogue c = em.find(Catalogue.class, id);
        if (c != null) em.remove(c);
    }
    
    @Override
    public void save(Catalogue c) {
        if (c.getId() == null) {
            em.persist(c);
        } else {
            em.merge(c);
        }
    }

    @Override
    public void modifier(Catalogue c) {
        List<Famille> familles = c.getFamilles(); // sauvegarde temporaire
        c.setFamilles(null);                     // détache les familles
        em.merge(c);                             // merge le catalogue seul
        c.setFamilles(familles);                // restore pour l'affichage si nécessaire
    }
    
    //NOUVELLES METHODES ICI
    /*
    @Override
    public void save(Catalogue catalogue) {
        em.persist(catalogue);
    }*/

    @Override
    public List<Catalogue> findAll() {
        return em.createQuery("SELECT c FROM Catalogue c ORDER BY c.nom", Catalogue.class)
                 .getResultList();
    }

    @Override
    public Catalogue findById(Long id) {
        return em.find(Catalogue.class, id);
    }

    @Override
    public void update(Catalogue catalogue) {
        em.merge(catalogue);
    }


    @Override
    public List<Catalogue> findActifs() {
        return em.createQuery("SELECT c FROM Catalogue c WHERE c.etat = true ORDER BY c.nom", Catalogue.class)
                 .getResultList();
    }

}
