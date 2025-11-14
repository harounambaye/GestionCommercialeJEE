// =============================================================
// Implémentation EmployeDaoImpl
// =============================================================
package dao;

import entity.Employe;
import entity.EntiteCommerciale;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class EmployeDaoImpl implements EmployeDao {
    
    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;
    
    @Override
    public void save(Employe employe) {
        em.persist(employe);
        System.out.println("💾 Employé sauvegardé : ID=" + employe.getId());
    }
    
    @Override
    public void update(Employe employe) {
        em.merge(employe);
        System.out.println("🔄 Employé mis à jour : ID=" + employe.getId());
    }
    
    @Override
    public Employe findById(Long id) {
        return em.find(Employe.class, id);
    }
    
    @Override
    public List<Employe> findByEntiteCommerciale(EntiteCommerciale entite) {
        System.out.println("🔍 Recherche employés pour entité : " + entite.getNom());
        
        List<Employe> employes = em.createQuery(
            "SELECT e FROM Employe e WHERE e.idEntiteCommerciale = :entiteId ORDER BY e.createdAt DESC", 
            Employe.class)
            .setParameter("entiteId", entite.getId())
            .getResultList();
        
        System.out.println("✅ " + employes.size() + " employés trouvés");
        return employes;
    }
    
    @Override
    public List<Employe> findActifsByEntite(EntiteCommerciale entite) {
        return em.createQuery(
            "SELECT e FROM Employe e WHERE e.idEntiteCommerciale = :entiteId AND e.actif = true ORDER BY e.createdAt DESC", 
            Employe.class)
            .setParameter("entiteId", entite.getId())
            .getResultList();
    }
    
    @Override
    public Employe findByPersonneId(Long personneId) {
        List<Employe> results = em.createQuery(
            "SELECT e FROM Employe e WHERE e.idPersonne = :personneId", 
            Employe.class)
            .setParameter("personneId", personneId)
            .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
    
    @Override
    public long countByEntite(EntiteCommerciale entite) {
        return em.createQuery(
            "SELECT COUNT(e) FROM Employe e WHERE e.idEntiteCommerciale = :entiteId", 
            Long.class)
            .setParameter("entiteId", entite.getId())
            .getSingleResult();
    }
}