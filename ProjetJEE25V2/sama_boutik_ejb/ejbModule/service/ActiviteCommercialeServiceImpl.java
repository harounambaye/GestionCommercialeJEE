package service;

import dao.ActiviteCommercialeDao;
import entity.ActiviteCommerciale;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class ActiviteCommercialeServiceImpl implements ActiviteCommercialeService {

    @EJB
    private ActiviteCommercialeDao activiteDao; // ✅ Injection EJB ajoutée

    @Override
    public void creerActivite(ActiviteCommerciale activite) {
        activiteDao.save(activite);
    }

    @Override
    public List<ActiviteCommerciale> listerActivites() {
        return activiteDao.findAll();
    }

    @Override
    public ActiviteCommerciale trouverParId(Long id) {
        return activiteDao.findById(id);
    }
    
    @Override
    public ActiviteCommerciale modifierActivite(ActiviteCommerciale activite) {
        return activiteDao.update(activite);
    }

    @Override
    public void supprimerActivite(Long id) {
        activiteDao.delete(id);
    }

}
