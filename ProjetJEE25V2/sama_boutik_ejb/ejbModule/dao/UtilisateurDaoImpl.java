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
    
    // ✅ nouvelle méthode basée sur la table de liaison PersonneProfil
    @Override
    public List<Personne> findByProfilViaPersonneProfil(Long profilId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Personne> cq = cb.createQuery(Personne.class);

        // 🔹 Table de liaison PersonneProfil
        Root<entity.PersonneProfil> root = cq.from(entity.PersonneProfil.class);

        // 🔹 Jointures vers Personne et Profil
        Join<Object, Personne> personneJoin = root.join("personne");
        Join<Object, entity.Profil> profilJoin = root.join("profil");

        // 🔹 WHERE et ORDER BY
        cq.select(personneJoin)
          .distinct(true)
          .where(cb.equal(profilJoin.get("id"), profilId))
          .orderBy(cb.asc(personneJoin.get("nom")));

        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public void update(Personne p) {
        em.merge(p);
    }
    
    // Ajoutée recement
    @Override
    public Profil findProfilByPersonne(Long personneId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Profil> cq = cb.createQuery(Profil.class);
        Root<entity.PersonneProfil> root = cq.from(entity.PersonneProfil.class);
        Join<Object, Profil> profilJoin = root.join("profil");
        Join<Object, entity.Personne> personneJoin = root.join("personne");

        cq.select(profilJoin)
          .where(cb.equal(personneJoin.get("id"), personneId));

        List<Profil> result = em.createQuery(cq).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
    
    // Champ de recherche
    @Override
    public List<Personne> rechercherUtilisateurs(String terme) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Personne> cq = cb.createQuery(Personne.class);
        Root<Personne> root = cq.from(Personne.class);

        String pattern = "%" + terme.toLowerCase() + "%";
        cq.select(root).where(cb.or(
            cb.like(cb.lower(root.get("prenom")), pattern),
            cb.like(cb.lower(root.get("nom")), pattern),
            cb.like(cb.lower(root.get("email")), pattern)
        ));

        return em.createQuery(cq).getResultList();
    }



}
