package dao;

import entity.Produit;
import entity.EntiteCommerciale;
import entity.Famille;
import java.util.List;

public interface ProduitDao {
    void save(Produit produit);
    List<Produit> findAll();
    Produit findById(Long id);
    void update(Produit produit);
    void delete(Long id);
    List<Produit> findByFamille(Famille famille, int page, int size);
    long countByFamille(Famille famille);
    String generateReference(String familleCode);
    // 👉 Nouvelles méthodes à ajouter
    //List<Produit> findByEntite(EntiteCommerciale entite);
    List<Produit> findByFamilleAndEntite(Famille famille, EntiteCommerciale entite, int page, int size);
    long countByFamilleAndEntite(Famille famille, EntiteCommerciale entite);
    List<Produit> findByEntite(EntiteCommerciale entite);
 // Dans ProduitDao.java
 // Ajoutez cette méthode dans ProduitDao interface
    List<Produit> findBySearchTermAndEntite(String searchTerm, EntiteCommerciale entite);

    
}