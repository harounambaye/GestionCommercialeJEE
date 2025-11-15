package service;

import entity.Personne;
import entity.Profil;

import java.util.List;
import jakarta.ejb.Local;

@Local
public interface UtilisateurService {
    List<Personne> listerUtilisateurs();
    List<Personne> listerUtilisateursParProfil(Long profilId);
    List<Personne> listerUtilisateursParProfilViaPersonneProfil(Long idProfil);
	void mettreAJour(Personne p);
	Profil trouverProfilParPersonne(Long personneId); 
	List<Personne> rechercherUtilisateurs(String terme);//Champ de recherche



}
