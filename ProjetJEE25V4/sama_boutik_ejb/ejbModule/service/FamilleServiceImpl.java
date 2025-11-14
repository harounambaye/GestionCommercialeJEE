package service;

import dao.FamilleDao;
import entity.Famille;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class FamilleServiceImpl implements FamilleService {

    @Inject
    private FamilleDao familleDao;

    @Override
    public List<Famille> listerParCatalogue(Long catalogueId) {
        return familleDao.listerParCatalogue(catalogueId);
    }

    @Override
    public Famille trouverParId(Long id) {
        return familleDao.trouverParId(id);
    }

    @Override
    public void supprimer(Long id) {
        familleDao.delete(id);
    }

    @Override
    public void sauvegarder(Famille f) {
        familleDao.save(f);
    }
    
    @Override
    public void modifier(Famille f) {
        familleDao.modifier(f);
    }
}
