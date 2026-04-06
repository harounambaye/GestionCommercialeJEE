package service;

import dao.CatalogueDao;
import entity.Catalogue;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class CatalogueServiceImpl implements CatalogueService {

    @Inject
    private CatalogueDao catalogueDao;

    @Override
    public List<Catalogue> listerTous() {
        return catalogueDao.listerTous();
    }

    @Override
    public Catalogue trouverParId(Long id) {
        return catalogueDao.trouverParId(id);
    }

    @Override
    public void supprimer(Long id) {
        catalogueDao.delete(id);
    }
    
    @Override
    public void sauvegarder(Catalogue c) {
        catalogueDao.save(c);
    }

    @Override
    public void modifier(Catalogue c) {
        catalogueDao.modifier(c);
    }
}
