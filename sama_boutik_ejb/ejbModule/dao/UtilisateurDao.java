package dao;

import entity.Personne;
import entity.Profil;

import java.util.List;

public interface UtilisateurDao {
    List<Personne> findAll();
    List<Personne> findByProfil(Long profilId);
    List<Personne> findByProfilViaPersonneProfil(Long profilId);
	void update(Personne p);
	Profil findProfilByPersonne(Long personneId); //Ajouté récemment
	List<Personne> rechercherUtilisateurs(String terme); //Champ de recherche
	
	// ✅ Ajout de cette méthode :
    Personne findById(Long id);


}