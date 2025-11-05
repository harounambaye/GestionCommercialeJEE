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

    @Override
    public void save(Permission permission) {
        em.persist(permission);
    }

    @Override
    public void delete(Long id) {
        Permission p = em.find(Permission.class, id);
        if (p != null) em.remove(p);
    }
    
    @Override
    public boolean estUtiliseeDansRole(Permission permission) {
        Long count = em.createQuery(
            "SELECT COUNT(r) FROM Role r WHERE r.permission = :perm", Long.class)
            .setParameter("perm", permission)
            .getSingleResult();
        return count > 0;
    }

}
