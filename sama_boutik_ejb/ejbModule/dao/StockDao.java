package dao;

import entity.EntiteCommerciale;
import entity.Stock;
import java.util.List;

public interface StockDao {
    void save(Stock stock);
    List<Stock> findAll();
    Stock findById(Long id);
    void update(Stock stock);
    void delete(Long id);
    List<Stock> findByYear(int year, int page, int size);
    long countByYear(int year);
	List<Stock> findByEntiteAndYear(EntiteCommerciale entite, int anneeStock, int pageStock, int tAILLE_PAGE);
	long countByEntiteAndYear(EntiteCommerciale entite, int anneeStock);
    
}