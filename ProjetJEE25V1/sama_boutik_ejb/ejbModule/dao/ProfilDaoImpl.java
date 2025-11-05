package dao;

import entity.Profil;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

@Stateless
public class ProfilDaoImpl implements ProfilDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(Profil profil) {
        em.persist(profil);
    }

    @Override
    public List<Profil> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Profil> cq = cb.createQuery(Profil.class);
        Root<Profil> root = cq.from(Profil.class);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }

    @Override
    public Profil findById(Long id) {
        return em.find(Profil.class, id);
    }

    @Override
    public void update(Profil profil) {
        em.merge(profil);
    }

    @Override
    public void delete(Long id) {
        Profil p = em.find(Profil.class, id);
        if (p != null) em.remove(p);
    }
}
