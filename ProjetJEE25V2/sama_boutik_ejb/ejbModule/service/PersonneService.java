package service;

import entity.Personne;
import java.util.List;
import jakarta.ejb.Local;

@Local
public interface PersonneService {
    void ajouterUtilisateur(Personne personne);
    List<Personne> listerUtilisateurs();
    void mettreAJour(Personne personne);

}
