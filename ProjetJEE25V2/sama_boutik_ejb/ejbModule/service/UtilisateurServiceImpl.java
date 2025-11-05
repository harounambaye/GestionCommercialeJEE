package service;

import dao.UtilisateurDao;
import entity.Personne;
import entity.Profil;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class UtilisateurServiceImpl implements UtilisateurService {

    @EJB
    private UtilisateurDao utilisateurDao;

    @Override
    public List<Personne> listerUtilisateurs() {
        return utilisateurDao.findAll();
    }

    @Override
    public List<Personne> listerUtilisateursParProfil(Long profilId) {
        if (profilId == null) return utilisateurDao.findAll();
        return utilisateurDao.findByProfil(profilId);
    }
    
    @Override
    public List<Personne> listerUtilisateursParProfilViaPersonneProfil(Long idProfil) {
        return utilisateurDao.findByProfilViaPersonneProfil(idProfil);
    }
    
    @Override
    public void mettreAJour(Personne p) {
        utilisateurDao.update(p);
    }
    
    //Ajoutée récemment
    @Override
    public Profil trouverProfilParPersonne(Long personneId) {
        return utilisateurDao.findProfilByPersonne(personneId);
    }

    //Champ de recherche
    @Override
    public List<Personne> rechercherUtilisateurs(String terme) {
        return utilisateurDao.rechercherUtilisateurs(terme);
    }


}
