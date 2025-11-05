package dao;

import entity.Permission;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

@Stateless
public class PermissionDaoImpl implements PermissionDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public List<Permission> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Permission> cq = cb.createQuery(Permission.class);
        Root<Permission> root = cq.from(Permission.class);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }

    @Override
    public Permission findById(Long id) {
        return em.find(Permission.class, id);
    }

    @Override
    public void update(Permission permission) {
        em.merge(permission);
    }
}
