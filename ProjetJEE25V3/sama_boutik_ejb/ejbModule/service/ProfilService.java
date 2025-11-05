package service;

import entity.Profil;
import java.util.List;
import jakarta.ejb.Local;

@Local
public interface ProfilService {
    void creerProfil(Profil profil);
    List<Profil> listerProfils();
    Profil trouverParId(Long id);
    void modifierProfil(Profil profil);
    void supprimerProfil(Long id);
    
}
