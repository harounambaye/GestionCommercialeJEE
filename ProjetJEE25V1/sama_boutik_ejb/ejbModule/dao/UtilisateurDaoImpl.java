package dao;

import entity.Personne;
import entity.Profil;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.List;

@Stateless
public class UtilisateurDaoImpl implements UtilisateurDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public List<Personne> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Personne> cq = cb.createQuery(Personne.class);
        Root<Personne> root = cq.from(Personne.class);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Personne> findByProfil(Long profilId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Personne> cq = cb.createQuery(Personne.class);
        Root<Profil> profilRoot = cq.from(Profil.class);
        Join<Profil, Personne> personneJoin = profilRoot.join("personne");

        cq.select(personneJoin)
          .where(cb.equal(profilRoot.get("id"), profilId));

        return em.createQuery(cq).getResultList();
    }
}
