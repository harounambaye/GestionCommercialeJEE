// CatalogueService.java
package service;

import entity.Catalogue;
import java.util.List;

public interface CatalogueService {
    List<Catalogue> listerTous();
    Catalogue trouverParId(Long id);
	void supprimer(Long id);
	void sauvegarder(Catalogue c);  // ✅ Nouvelle méthode
    void modifier(Catalogue c);     // ✅ Nouvelle méthode
}
