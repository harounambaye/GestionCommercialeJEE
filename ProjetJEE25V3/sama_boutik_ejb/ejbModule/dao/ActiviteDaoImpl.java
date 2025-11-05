package dao;

import entity.ActiviteCommerciale;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ActiviteDaoImpl implements ActiviteDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(ActiviteCommerciale a) {
        em.persist(a);
    }

    @Override
    public void update(ActiviteCommerciale a) {
        ActiviteCommerciale existante = em.find(ActiviteCommerciale.class, a.getId());
        if (existante != null) {
            existante.setNom(a.getNom());
            existante.setDescription(a.getDescription());
            existante.setStatut(a.getStatut());
            // 🔹 On ne touche pas à existante.getEntites() ici !
        }
    }

    @Override
    public void delete(Long id) {
        ActiviteCommerciale a = em.find(ActiviteCommerciale.class, id);
        if (a != null) em.remove(a);
    }

    @Override
    public List<ActiviteCommerciale> findAll() {
        return em.createQuery("SELECT a FROM ActiviteCommerciale a ORDER BY a.nom", ActiviteCommerciale.class)
                 .getResultList();
    }

    @Override
    public ActiviteCommerciale findById(Long id) {
        return em.find(ActiviteCommerciale.class, id);
    }
    
 // ✅ Vérifie s’il existe une entité liée sans charger la collection
    @Override
    public long countEntitesLiees(Long activiteId) {
        return em.createQuery(
                "SELECT COUNT(e) FROM EntiteCommerciale e WHERE e.activite.id = :id", Long.class)
                .setParameter("id", activiteId)
                .getSingleResult();
    }
}
