// CatalogueDao.java
package dao;

import entity.Catalogue;
import java.util.List;

public interface CatalogueDao {
    List<Catalogue> listerTous();
    Catalogue trouverParId(Long id);
	void delete(Long id);
	void save(Catalogue c);      // ✅ Nouvelle méthode
    void modifier(Catalogue c);  // ✅ Nouvelle méthode
    
    //NOUVELLES METHODES POUR GESTION CATALOGUE
    
    List<Catalogue> findAll();
    Catalogue findById(Long id);
    void update(Catalogue catalogue);
    List<Catalogue> findActifs();
}
