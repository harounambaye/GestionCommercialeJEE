package service;

import dao.ActiviteDao;
import entity.ActiviteCommerciale;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class ActiviteServiceImpl implements ActiviteService {

    @EJB private ActiviteDao activiteDao;

    @Override
    public void ajouterActivite(ActiviteCommerciale activite) {
        activiteDao.save(activite);
    }

    @Override
    public void modifierActivite(ActiviteCommerciale activite) {
        ActiviteCommerciale a = activiteDao.findById(activite.getId());

        if (a != null) {
            boolean statutAvant = a.getStatut();
            a.setNom(activite.getNom());
            a.setDescription(activite.getDescription());
            a.setStatut(activite.getStatut());

            // ✅ Si on désactive cette activité → désactiver ses entités
            if (statutAvant && !activite.getStatut()) {
                if (a.getEntites() != null) {
                    a.getEntites().forEach(e -> e.setStatut(false));
                }
            }

            activiteDao.update(a);
        }
    }


    @Override
    public void supprimerActivite(Long id) {
        long nbEntites = activiteDao.countEntitesLiees(id);
        if (nbEntites > 0) {
            throw new RuntimeException("Impossible de supprimer cette activité car elle contient des entités commerciales.");
        }
        activiteDao.delete(id);
    }

    @Override
    public ActiviteCommerciale trouverParId(Long id) {
        return activiteDao.findById(id);
    }

    @Override
    public List<ActiviteCommerciale> listerActivites() {
        return activiteDao.findAll();
    }
}
