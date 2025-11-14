package dao;

import entity.EntiteCommerciale;
import entity.Personne;

import java.util.List;

public interface EntiteDao {
    void save(EntiteCommerciale e);
    EntiteCommerciale findById(Long id);
    void update(EntiteCommerciale e);
    void delete(Long id);
    List<EntiteCommerciale> findByActivite(Long activiteId);
    List<EntiteCommerciale> findAll();
    List<Personne> findGestionnaires();
    void detacherGestionnaireInactif(Personne personne);
	List<EntiteCommerciale> findByGestionnaire(Long gestionnaireId);
    
    

}
