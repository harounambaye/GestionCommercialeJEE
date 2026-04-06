package service;

import dao.EntiteDao;
import dao.PersonneDao;
import entity.Personne;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class PersonneServiceImpl implements PersonneService {

    @EJB
    private PersonneDao personneDao;
    
    @EJB
    private EntiteDao entiteDao;

    @Override
    public void ajouterUtilisateur(Personne personne) {
        // Génération du matricule automatique
        String initiales = personne.getPrenom().substring(0, 1).toUpperCase() +
                           personne.getNom().substring(0, 1).toUpperCase();
        String matricule = initiales + System.currentTimeMillis() % 10000;
        personne.setMatricule(matricule);

        // Cryptage du mot de passe
        String hashed = BCrypt.hashpw(personne.getPassword(), BCrypt.gensalt());
        personne.setPassword(hashed);

        personneDao.save(personne);
    }

    @Override
    public List<Personne> listerUtilisateurs() {
        return personneDao.findAll();
    }
    
    @Override
    public void mettreAJour(Personne personne) {
        Personne ancienne = personneDao.findByEmail(personne.getEmail());
        boolean etaitActif = (ancienne != null && ancienne.isActif());
        
        personneDao.update(personne);

        // ✅ Si elle vient d’être désactivée → détacher
        if (etaitActif && !personne.isActif()) {
            entiteDao.detacherGestionnaireInactif(personne);
        }
    }

}
