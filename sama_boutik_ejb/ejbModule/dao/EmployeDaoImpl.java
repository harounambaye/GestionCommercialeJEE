package dao;

import entity.Employe;
import entity.EntiteCommerciale;
import entity.Personne;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.util.List;

@Stateless
public class EmployeDaoImpl implements EmployeDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(Employe employe) {
        em.persist(employe);
        System.out.println("✅ Employé enregistré : " + employe.getPersonne().getNom());
    }

    @Override
    public List<Employe> findAll() {
        return em.createQuery(
            "SELECT e FROM Employe e ORDER BY e.personne.nom, e.personne.prenom", 
            Employe.class
        ).getResultList();
    }

    @Override
    public Employe findById(Long id) {
        return em.find(Employe.class, id);
    }

    @Override
    public List<Employe> findByEntiteCommerciale(EntiteCommerciale entite) {
        return em.createQuery(
            "SELECT e FROM Employe e " +
            "WHERE e.entiteCommerciale = :entite " +
            "ORDER BY e.personne.nom, e.personne.prenom", 
            Employe.class
        )
        .setParameter("entite", entite)
        .getResultList();
    }

    @Override
    public List<Employe> findActifsByEntite(EntiteCommerciale entite) {
        return em.createQuery(
            "SELECT e FROM Employe e " +
            "WHERE e.entiteCommerciale = :entite AND e.actif = true " +
            "ORDER BY e.personne.nom, e.personne.prenom", 
            Employe.class
        )
        .setParameter("entite", entite)
        .getResultList();
    }

    @Override
    public Employe findByPersonne(Personne personne) {
        try {
            return em.createQuery(
                "SELECT e FROM Employe e WHERE e.personne = :personne", 
                Employe.class
            )
            .setParameter("personne", personne)
            .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Employe> findByEntiteAndAnnee(EntiteCommerciale entite, String annee) {
        return em.createQuery(
            "SELECT e FROM Employe e " +
            "WHERE e.entiteCommerciale = :entite " +
            "AND e.salairemois LIKE :annee " +
            "ORDER BY e.personne.nom", 
            Employe.class
        )
        .setParameter("entite", entite)
        .setParameter("annee", annee + "%")
        .getResultList();
    }

    @Override
    public List<Employe> findByEntiteAndTypeContrat(EntiteCommerciale entite, String typeContrat) {
        return em.createQuery(
            "SELECT e FROM Employe e " +
            "WHERE e.entiteCommerciale = :entite " +
            "AND e.typecontrat = :type " +
            "ORDER BY e.personne.nom", 
            Employe.class
        )
        .setParameter("entite", entite)
        .setParameter("type", typeContrat)
        .getResultList();
    }

    @Override
    public List<Employe> findByEntiteAndStatutPaye(EntiteCommerciale entite, String statutPaye) {
        return em.createQuery(
            "SELECT e FROM Employe e " +
            "WHERE e.entiteCommerciale = :entite " +
            "AND e.paye = :statut " +
            "ORDER BY e.personne.nom", 
            Employe.class
        )
        .setParameter("entite", entite)
        .setParameter("statut", statutPaye)
        .getResultList();
    }

    @Override
    public void update(Employe employe) {
        em.merge(employe);
        System.out.println("✅ Employé mis à jour : " + employe.getPersonne().getNom());
    }

    @Override
    public void delete(Long id) {
        Employe employe = em.find(Employe.class, id);
        if (employe != null) {
            em.remove(employe);
            System.out.println("🗑️ Employé supprimé physiquement : ID " + id);
        }
    }

    @Override
    public void desactiver(Long id) {
        Employe employe = em.find(Employe.class, id);
        if (employe != null) {
            employe.setActif(false);
            em.merge(employe);
            System.out.println("❌ Employé désactivé : " + employe.getPersonne().getNom());
        }
    }

    @Override
    public void activer(Long id) {
        Employe employe = em.find(Employe.class, id);
        if (employe != null) {
            employe.setActif(true);
            em.merge(employe);
            System.out.println("✅ Employé activé : " + employe.getPersonne().getNom());
        }
    }

    @Override
    public boolean isEmployeDansEntite(Personne personne, EntiteCommerciale entite) {
        Long count = em.createQuery(
            "SELECT COUNT(e) FROM Employe e " +
            "WHERE e.personne = :personne AND e.entiteCommerciale = :entite", 
            Long.class
        )
        .setParameter("personne", personne)
        .setParameter("entite", entite)
        .getSingleResult();
        
        return count > 0;
    }
}