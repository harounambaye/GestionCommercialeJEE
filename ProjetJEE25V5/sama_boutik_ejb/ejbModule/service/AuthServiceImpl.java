package service;

import entity.Personne;
import entity.Profil;
import entity.PersonneProfil;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import java.util.List;

@Stateless
public class AuthServiceImpl implements AuthService {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public Personne authentifier(String email, String motDePasse) {
        if (email == null || motDePasse == null || email.trim().isEmpty()) {
            return null;
        }

        try {
            Personne personne = em.createQuery(
                "SELECT p FROM Personne p WHERE p.email = :email", Personne.class)
                .setParameter("email", email.trim())
                .getSingleResult();

            // Vérifier le mot de passe (haché avec BCrypt)
            if (personne.getPassword() != null && 
                BCrypt.checkpw(motDePasse, personne.getPassword())) {
                return personne;
            }
            
            return null;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Profil getProfilPrincipal(Personne personne) {
        try {
            List<PersonneProfil> liens = em.createQuery(
                "SELECT pp FROM PersonneProfil pp WHERE pp.personne = :personne " +
                "ORDER BY pp.createdAt ASC", PersonneProfil.class)
                .setParameter("personne", personne)
                .getResultList();

            if (!liens.isEmpty()) {
                return liens.get(0).getProfil();
            }
            return null;
        } catch (Exception e) {
            System.err.println("❌ Erreur récupération profil : " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isActif(Personne personne) {
        return personne != null && personne.isActif();
    }
}