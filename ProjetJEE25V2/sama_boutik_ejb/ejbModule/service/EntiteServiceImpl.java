package service;

import dao.EntiteDao;
import entity.Entite;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class EntiteServiceImpl implements EntiteService {

    @EJB
    private EntiteDao entiteDao;

    @Override
    public void creerEntite(Entite entite) { entiteDao.save(entite); }

    @Override
    public List<Entite> listerEntites() { return entiteDao.findAll(); }

    @Override
    public Entite trouverParId(Long id) { return entiteDao.findById(id); }

    @Override
    public void modifierEntite(Entite entite) { entiteDao.update(entite); }

    @Override
    public void supprimerEntite(Long id) { entiteDao.delete(id); }
}
