package dao;

import entity.Catalogue;
import entity.Famille;
import java.util.List;

public interface FamilleDao {

    List<Famille> listerParCatalogue(Long catalogueId);

    Famille trouverParId(Long id);

    void delete(Long id);   // ✅ méthode pour suppression

    void save(Famille f);   // ✅ méthode pour sauvegarde
    
    void modifier(Famille f); // ✅ méthode pour modification
    
    //NOUVELLES METHODES POUR GESTION FAMILLE
    
    List<Famille> findAll();
    Famille findById(Long id);
    void update(Famille famille);
    List<Famille> findByCatalogue(Catalogue catalogue);
}
