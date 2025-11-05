package service;

import dao.RoleDao;
import entity.Personne;
import entity.Permission;
import entity.Role;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class RoleServiceImpl implements RoleService {

    @EJB private RoleDao roleDao;

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void ajouterRole(Personne p, Permission perm) {
        Role existant = roleDao.findByPersonneAndPermission(p, perm);
        if (existant == null) {
            Role r = new Role();
            r.setPersonne(p);
            r.setPermission(perm);
            r.setEtat(true);
            r.setCreatedAt(LocalDateTime.now());
            r.setUpdatedAt(LocalDateTime.now());
            if (!em.contains(p)) {
                p = em.merge(p); // 🔥 réattache la Personne avant de persister le rôle
            }
            r.setPersonne(p);
            roleDao.save(r);
            
            System.out.println("✅ Ajout permission " + perm.getNom() + " pour " + p.getNom());
        }
    }

    @Override
    public void supprimerRole(Role role) {
        roleDao.delete(role);
    }

    @Override
    public List<Role> listerRolesParPersonne(Personne p) {
        return roleDao.findByPersonne(p);
    }

    @Override
    public Personne trouverPersonneParId(Long id) {
        return em.find(Personne.class, id);
    }
}
