package service;

import entity.Personne;
import java.util.List;
import jakarta.ejb.Local;

@Local
public interface UtilisateurService {
    List<Personne> listerUtilisateurs();
    List<Personne> listerUtilisateursParProfil(Long profilId);
}
