package dao;

import entity.EntiteCommerciale;
import entity.Personne;
import entity.Profil;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class EntiteDaoImpl implements EntiteDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(EntiteCommerciale e) {
        em.persist(e);
    }

    @Override
    public EntiteCommerciale findById(Long id) {
        return em.find(EntiteCommerciale.class, id);
    }

    @Override
    public void update(EntiteCommerciale e) {
        em.merge(e);
    }

    @Override
    public void delete(Long id) {
        EntiteCommerciale e = em.find(EntiteCommerciale.class, id);
        if (e != null) {
            em.remove(em.merge(e)); // merge avant remove pour s’assurer qu’elle est managée
            System.out.println("✅ Suppression effective en BDD : " + e.getNom());
        } else {
            System.out.println("⚠️ Entité introuvable en BDD pour ID : " + id);
        }
    }


    @Override
    public List<EntiteCommerciale> findByActivite(Long activiteId) {
        TypedQuery<EntiteCommerciale> q = em.createQuery(
            "SELECT e FROM EntiteCommerciale e WHERE e.activite.id = :id ORDER BY e.nom ASC",
            EntiteCommerciale.class
        );
        q.setParameter("id", activiteId);
        return q.getResultList();
    }

    @Override
    public List<EntiteCommerciale> findAll() {
        return em.createQuery("SELECT e FROM EntiteCommerciale e ORDER BY e.nom", EntiteCommerciale.class)
                 .getResultList();
    }
    
 // ✅ Liste des gestionnaires (profil “Gestionnaire” = id 2)
    @Override
    public List<Personne> findGestionnaires() {
        Profil profilGestionnaire = em.find(Profil.class, 2L); // ID du profil “Gestionnaire”
        return em.createQuery(
                "SELECT pp.personne FROM PersonneProfil pp WHERE pp.profil = :p",
                Personne.class)
                .setParameter("p", profilGestionnaire)
                .getResultList();
    }
    
	 @Override
	 public void detacherGestionnaireInactif(Personne personne) {
	     if (personne == null) return;
	
	     int updated = em.createQuery("""
	         UPDATE EntiteCommerciale e
	         SET e.gestionnaire = NULL
	         WHERE e.gestionnaire = :p
	     """)
	     .setParameter("p", personne)
	     .executeUpdate();
	
	     System.out.println("⚠️ " + updated + " entités commerciales détachées du gestionnaire inactif : " 
	                        + personne.getNom());
	 }

}
