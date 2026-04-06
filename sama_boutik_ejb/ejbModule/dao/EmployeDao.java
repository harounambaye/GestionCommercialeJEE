package dao;

import entity.Employe;
import entity.EntiteCommerciale;
import entity.Personne;
import java.util.List;

public interface EmployeDao {
    
    /**
     * Enregistrer un nouvel employé
     */
    void save(Employe employe);
    
    /**
     * Rechercher tous les employés
     */
    List<Employe> findAll();
    
    /**
     * Rechercher un employé par ID
     */
    Employe findById(Long id);
    
    /**
     * Rechercher les employés d'une entité commerciale
     */
    List<Employe> findByEntiteCommerciale(EntiteCommerciale entite);
    
    /**
     * Rechercher les employés actifs d'une entité
     */
    List<Employe> findActifsByEntite(EntiteCommerciale entite);
    
    /**
     * Rechercher un employé par personne
     */
    Employe findByPersonne(Personne personne);
    
    /**
     * Filtrer les employés par année de salaire
     */
    List<Employe> findByEntiteAndAnnee(EntiteCommerciale entite, String annee);
    
    /**
     * Filtrer par type de contrat
     */
    List<Employe> findByEntiteAndTypeContrat(EntiteCommerciale entite, String typeContrat);
    
    /**
     * Filtrer par statut payé/non payé
     */
    List<Employe> findByEntiteAndStatutPaye(EntiteCommerciale entite, String statutPaye);
    
    /**
     * Mettre à jour un employé
     */
    void update(Employe employe);
    
    /**
     * Supprimer un employé (physiquement)
     */
    void delete(Long id);
    
    /**
     * Désactiver un employé (suppression logique)
     */
    void desactiver(Long id);
    
    /**
     * Activer un employé
     */
    void activer(Long id);
    
    /**
     * Vérifier si une personne est déjà employée dans une entité
     */
    boolean isEmployeDansEntite(Personne personne, EntiteCommerciale entite);
}