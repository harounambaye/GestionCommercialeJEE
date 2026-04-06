package service;

import dao.ActiviteDao;
import dao.EntiteDao;
import dao.UtilisateurDao;
import entity.ActiviteCommerciale;
import entity.EntiteCommerciale;
import entity.Personne;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class EntiteServiceImpl implements EntiteService {

    @EJB private EntiteDao entiteDao;
    @EJB private ActiviteDao activiteDao;
    @EJB private UtilisateurDao utilisateurDao;
    
    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;
    
    @Override
    public List<Personne> listerGestionnaires() {
        return entiteDao.findGestionnaires();
    }

    @Override
    public void ajouterEntite(EntiteCommerciale e, Long activiteId, Long gestionnaireId) {
        ActiviteCommerciale a = activiteDao.findById(activiteId);
        Personne g = utilisateurDao.findById(gestionnaireId);
        if (a != null && g != null) {
            e.setActivite(a);
            e.setGestionnaire(g);
            entiteDao.save(e);
        }
    }

    @Override
    public void modifierEntite(EntiteCommerciale e) {
        entiteDao.update(e);
    }

    @Override
    public void supprimerEntite(Long id) {
        entiteDao.delete(id);
    }
    
    @Override
    public EntiteCommerciale trouverParId(Long id) {
        EntiteCommerciale e = entiteDao.findById(id);
        if (e != null)
            System.out.println("🔍 Entité trouvée pour suppression : " + e.getNom());
        else
            System.out.println("⚠️ Entité introuvable avec ID : " + id);
        return e;
    }


    @Override
    public List<EntiteCommerciale> listerEntitesParActivite(Long activiteId) {
        return entiteDao.findByActivite(activiteId);
    }

    @Override
    public List<EntiteCommerciale> listerToutes() {
        return entiteDao.findAll();
    }
    
    @Override
    public EntiteCommerciale trouverParGestionnaire(Long gestionnaireId) {
        List<EntiteCommerciale> liste = entiteDao.findByGestionnaire(gestionnaireId);
        if (liste != null && !liste.isEmpty()) {
            return liste.get(0); // si un gestionnaire n’a qu’une seule entité
        }
        return null;
    }
    
    
    @Override
    public List<EntiteCommerciale> trouverToutesParGestionnaire(Long gestionnaireId) {
        return em.createQuery(
            "SELECT e FROM EntiteCommerciale e WHERE e.gestionnaire.id = :gestionnaireId AND e.statut = true ORDER BY e.nom",
            EntiteCommerciale.class
        )
        .setParameter("gestionnaireId", gestionnaireId)
        .getResultList();
    }
    
 // ✅ NOUVELLE MÉTHODE : Trouver l'entité commerciale d'un employé
    @Override
    public EntiteCommerciale trouverParEmploye(Long personneId) {
        try {
            System.out.println("🔍 Recherche entité pour employé ID: " + personneId);
            
            // Requête JPQL via la table Employe
            EntiteCommerciale entite = em.createQuery(
                "SELECT emp.entiteCommerciale FROM Employe emp " +
                "WHERE emp.personne.id = :personneId " +
                "AND emp.actif = true",
                EntiteCommerciale.class)
                .setParameter("personneId", personneId)
                .getSingleResult();
            
            System.out.println("✅ Entité trouvée: " + entite.getNom());
            return entite;
            
        } catch (NoResultException e) {
            System.err.println("⚠️ Aucune entité trouvée pour l'employé ID: " + personneId);
            return null;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur trouverParEmploye: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
