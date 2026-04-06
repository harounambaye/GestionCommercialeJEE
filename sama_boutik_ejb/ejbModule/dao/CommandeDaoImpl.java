package dao;

import entity.Commande;
import entity.Personne;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless
public class CommandeDaoImpl implements CommandeDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(Commande commande) {
        em.persist(commande);
        System.out.println("✅ Commande enregistrée : " + commande.getCodeCommande());
    }

    @Override
    public Commande findById(Long id) {
        return em.find(Commande.class, id);
    }

    @Override
    public List<Commande> findByPersonne(Personne personne) {
        return em.createQuery(
            "SELECT c FROM Commande c " +
            "WHERE c.idPersonne = :personneId " +
            "ORDER BY c.dateCommande DESC",
            Commande.class
        )
        .setParameter("personneId", personne.getId())
        .getResultList();
    }

    @Override
    public List<Commande> findAll() {
        return em.createQuery(
            "SELECT c FROM Commande c ORDER BY c.dateCommande DESC",
            Commande.class
        ).getResultList();
    }

    @Override
    public void update(Commande commande) {
        em.merge(commande);
        System.out.println("✅ Commande mise à jour : " + commande.getCodeCommande());
    }

    @Override
    public String genererCodeCommande() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        Long count = em.createQuery(
            "SELECT COUNT(c) FROM Commande c " +
            "WHERE c.codeCommande LIKE :pattern",
            Long.class
        )
        .setParameter("pattern", "CMD-" + date + "%")
        .getSingleResult();
        
        int numero = count.intValue() + 1;
        String code = String.format("CMD-%s-%04d", date, numero);
        
        System.out.println("✅ Code commande généré : " + code);
        return code;
    }

    @Override
    public Commande findByCode(String codeCommande) {
        try {
            return em.createQuery(
                "SELECT c FROM Commande c WHERE c.codeCommande = :code",
                Commande.class
            )
            .setParameter("code", codeCommande)
            .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public void flush() {
        em.flush();
        System.out.println("✅ EntityManager flushed - Commande managée");
    }
}