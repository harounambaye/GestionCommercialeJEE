package service;

import entity.PersonneProfil;
import entity.Personne;
import entity.Profil;
import java.util.List;
import jakarta.ejb.Local;

@Local
public interface PersonneProfilService {
    void ajouterProfil(Personne personne, Profil profil);
    List<Profil> listerProfilsParPersonne(Personne personne);
    List<Profil> listerProfilsUtilises();
    void supprimerProfilsDePersonne(Personne personne); // Ajoutée recemment
    List<Personne> listerGestionnaires();


}
