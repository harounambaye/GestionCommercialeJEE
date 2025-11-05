package service;

import entity.PersonneProfil;
import entity.Personne;
import entity.Profil;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.util.List;

@Stateless
public class PersonneProfilServiceImpl implements PersonneProfilService {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void ajouterProfil(Personne personne, Profil profil) {
        if (!em.contains(personne)) {
            personne = em.merge(personne);
        }
        if (!em.contains(profil)) {
            profil = em.merge(profil);
        }

        boolean exist = !em.createQuery(
            "SELECT pp FROM PersonneProfil pp WHERE pp.personne = :p AND pp.profil = :pr", PersonneProfil.class)
            .setParameter("p", personne)
            .setParameter("pr", profil)
            .getResultList().isEmpty();

        if (!exist) {
            PersonneProfil pp = new PersonneProfil();
            pp.setPersonne(personne);
            pp.setProfil(profil);
            em.persist(pp);
            System.out.println("✅ Lien Personne-Profil ajouté : " + personne.getNom() + " → " + profil.getTitre());
        } else {
            System.out.println("ℹ️ Lien Personne-Profil déjà existant : " + personne.getNom());
        }
    }

    @Override
    public List<Profil> listerProfilsParPersonne(Personne personne) {
        return em.createQuery("SELECT pp.profil FROM PersonneProfil pp WHERE pp.personne = :p", Profil.class)
                 .setParameter("p", personne)
                 .getResultList();
    }
    
    @Override
    public List<Profil> listerProfilsUtilises() {
        return em.createQuery(
            "SELECT DISTINCT pp.profil FROM PersonneProfil pp ORDER BY pp.profil.titre",
            Profil.class
        ).getResultList();
    }
    
    //Ajoutée recentement
    @Override
    public void supprimerProfilsDePersonne(Personne personne) {
        List<PersonneProfil> liens = em.createQuery(
            "SELECT pp FROM PersonneProfil pp WHERE pp.personne = :personne", PersonneProfil.class)
            .setParameter("personne", personne)
            .getResultList();

        for (PersonneProfil pp : liens) {
            em.remove(em.merge(pp));
            System.out.println("🗑️ Ancien lien supprimé : " + personne.getPrenom() + " → " + pp.getProfil().getTitre());
        }
    }


}
