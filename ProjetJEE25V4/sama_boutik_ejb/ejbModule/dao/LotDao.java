package dao;

import entity.Lot;
import entity.Produit;
import entity.Stock;
import java.util.List;

public interface LotDao {
    void save(Lot lot);
    List<Lot> findAll();
    Lot findById(Long id);
    void update(Lot lot);
    void delete(Long id);
    List<Lot> findByStock(Stock stock);
    List<Lot> findByStockAndYear(Stock stock, int year);
    long countByProduit(Produit produit); // ✅ Modifié : compte via les articles
    String generateCodeLot();
    Lot findByIdWithArticles(Long id); // ✅ Ajoutez cette méthode
	/**
	 * Méthode alternative sans FETCH JOIN pour éviter l'erreur OpenJPA
	 */
	List<Lot> findByStockAndYearSimple(Stock stock, int year);
}