package service;

import dao.ProfilDao;
import entity.Profil;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class ProfilServiceImpl implements ProfilService {

    @EJB
    private ProfilDao profilDao;

    @Override
    public void creerProfil(Profil profil) {
        profilDao.save(profil);
    }

    @Override
    public List<Profil> listerProfils() {
        return profilDao.findAll();
    }

    @Override
    public Profil trouverParId(Long id) {
        return profilDao.findById(id);
    }

    @Override
    public void modifierProfil(Profil profil) {
        profilDao.update(profil);
    }

    @Override
    public void supprimerProfil(Long id) {
        profilDao.delete(id);
    }
}
