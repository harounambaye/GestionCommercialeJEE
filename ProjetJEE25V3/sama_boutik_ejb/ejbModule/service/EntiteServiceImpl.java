package service;

import dao.ActiviteDao;
import dao.EntiteDao;
import dao.UtilisateurDao;
import entity.ActiviteCommerciale;
import entity.EntiteCommerciale;
import entity.Personne;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class EntiteServiceImpl implements EntiteService {

    @EJB private EntiteDao entiteDao;
    @EJB private ActiviteDao activiteDao;
    @EJB private UtilisateurDao utilisateurDao;

    @Override
    public List<Personne> listerGestionnaires() {
        return entiteDao.findGestionnaires();
    }

    @Override
    public void ajouterEntite(EntiteCommerciale e, Long activiteId, Long gestionnaireId) {
        ActiviteCommerciale a = activiteDao.findById(activiteId);
        Personne g = utilisateurDao.findById(gestionnaireId);
        if (a != null && g != null) {
            e.setActivite(a);
            e.setGestionnaire(g);
            entiteDao.save(e);
        }
    }

    @Override
    public void modifierEntite(EntiteCommerciale e) {
        entiteDao.update(e);
    }

    @Override
    public void supprimerEntite(Long id) {
        entiteDao.delete(id);
    }
    
    @Override
    public EntiteCommerciale trouverParId(Long id) {
        EntiteCommerciale e = entiteDao.findById(id);
        if (e != null)
            System.out.println("🔍 Entité trouvée pour suppression : " + e.getNom());
        else
            System.out.println("⚠️ Entité introuvable avec ID : " + id);
        return e;
    }


    @Override
    public List<EntiteCommerciale> listerEntitesParActivite(Long activiteId) {
        return entiteDao.findByActivite(activiteId);
    }

    @Override
    public List<EntiteCommerciale> listerToutes() {
        return entiteDao.findAll();
    }
}
