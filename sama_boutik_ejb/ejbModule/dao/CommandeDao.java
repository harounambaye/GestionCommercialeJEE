package dao;

import entity.Commande;
import entity.Personne;
import java.util.List;

public interface CommandeDao {
    void save(Commande commande);
    Commande findById(Long id);
    List<Commande> findByPersonne(Personne personne);
    List<Commande> findAll();
    void update(Commande commande);
    String genererCodeCommande();
    Commande findByCode(String codeCommande);
    
    /**
     * Force la synchronisation avec la base de données
     * Nécessaire pour éviter les erreurs "unmanaged object"
     */
    void flush();
}