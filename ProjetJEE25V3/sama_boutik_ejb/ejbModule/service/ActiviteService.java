package service;

import entity.ActiviteCommerciale;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface ActiviteService {
    void ajouterActivite(ActiviteCommerciale activite);
    void modifierActivite(ActiviteCommerciale activite);
    void supprimerActivite(Long id);
    ActiviteCommerciale trouverParId(Long id);
    List<ActiviteCommerciale> listerActivites();
}
