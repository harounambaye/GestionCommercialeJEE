package service;

import entity.ActiviteCommerciale;
import java.util.List;

public interface ActiviteCommercialeService {
    void creerActivite(ActiviteCommerciale activite);
    List<ActiviteCommerciale> listerActivites();
    ActiviteCommerciale trouverParId(Long id);
    
    ActiviteCommerciale modifierActivite(ActiviteCommerciale activite);
    void supprimerActivite(Long id);
}
