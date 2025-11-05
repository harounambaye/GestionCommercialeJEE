package service;

import dao.UtilisateurDao;
import entity.Personne;
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
}
