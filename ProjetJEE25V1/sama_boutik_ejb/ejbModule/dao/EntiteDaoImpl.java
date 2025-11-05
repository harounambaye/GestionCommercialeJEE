package dao;

import entity.Entite;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

@Stateless
public class EntiteDaoImpl implements EntiteDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(Entite entite) { em.persist(entite); }

    @Override
    public List<Entite> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Entite> cq = cb.createQuery(Entite.class);
        Root<Entite> root = cq.from(Entite.class);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }


    @Override
    public Entite findById(Long id) { return em.find(Entite.class, id); }

    @Override
    public void update(Entite entite) { em.merge(entite); }

    @Override
    public void delete(Long id) {
        Entite e = em.find(Entite.class, id);
        if (e != null) em.remove(e);
    }
}
