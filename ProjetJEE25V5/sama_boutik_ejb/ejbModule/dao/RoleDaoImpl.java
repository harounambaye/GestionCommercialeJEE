package dao;

import entity.Personne;
import entity.Permission;
import entity.Role;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(Role role) {
        em.persist(role);
    }

    @Override
    public void delete(Role role) {
        em.remove(em.merge(role));
    }

    @Override
    public Role findByPersonneAndPermission(Personne p, Permission perm) {
        List<Role> list = em.createQuery("SELECT r FROM Role r WHERE r.personne = :p AND r.permission = :perm", Role.class)
                .setParameter("p", p)
                .setParameter("perm", perm)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Role> findByPersonne(Personne p) {
        return em.createQuery("SELECT r FROM Role r WHERE r.personne = :p", Role.class)
                .setParameter("p", p)
                .getResultList();
    }
}
