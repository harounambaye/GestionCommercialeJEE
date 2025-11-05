package dao;

import entity.ActiviteCommerciale;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

@Stateless
public class ActiviteCommercialeDaoImpl implements ActiviteCommercialeDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(ActiviteCommerciale activite) {
        em.persist(activite);
    }

    @Override
    public ActiviteCommerciale findById(Long id) {
        return em.find(ActiviteCommerciale.class, id);
    }

    @Override
    public List<ActiviteCommerciale> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ActiviteCommerciale> cq = cb.createQuery(ActiviteCommerciale.class);
        Root<ActiviteCommerciale> root = cq.from(ActiviteCommerciale.class);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public ActiviteCommerciale update(ActiviteCommerciale activite) {
        return em.merge(activite);
    }

    @Override
    public void delete(Long id) {
        ActiviteCommerciale a = em.find(ActiviteCommerciale.class, id);
        if (a != null) {
            em.remove(a);
        }
    }

}
