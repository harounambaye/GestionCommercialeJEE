package service;

import entity.Entite;
import java.util.List;
import jakarta.ejb.Local;

@Local
public interface EntiteService {
    void creerEntite(Entite entite);
    List<Entite> listerEntites();
    Entite trouverParId(Long id);
    void modifierEntite(Entite entite);
    void supprimerEntite(Long id);
}
