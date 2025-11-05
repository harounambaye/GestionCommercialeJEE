package service;

import entity.EntiteCommerciale;
import entity.Personne;

import java.util.List;
import jakarta.ejb.Local;

@Local
public interface EntiteService {
    void ajouterEntite(EntiteCommerciale e, Long activiteId, Long gestionnaireId);
    void modifierEntite(EntiteCommerciale e);
    void supprimerEntite(Long id);
    EntiteCommerciale trouverParId(Long id);
    List<EntiteCommerciale> listerEntitesParActivite(Long activiteId);
    List<EntiteCommerciale> listerToutes();
    List<Personne> listerGestionnaires();

}
