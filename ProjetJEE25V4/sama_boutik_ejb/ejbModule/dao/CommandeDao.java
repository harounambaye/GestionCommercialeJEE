package dao;

import entity.Commande;
import java.util.List;

public interface CommandeDao {
    void save(Commande commande);
    List<Commande> findAll();
    Commande findById(Long id);
    void update(Commande commande);
    void delete(Long id);
    List<Commande> findByStatut(String statut);
    String generateCodeCommande();
}