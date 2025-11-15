package dao;

import entity.Commande;
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
    }

    @Override
    public List<Commande> findAll() {
        return em.createQuery("SELECT c FROM Commande c ORDER BY c.dateCommande DESC", Commande.class)
                 .getResultList();
    }

    @Override
    public Commande findById(Long id) {
        return em.find(Commande.class, id);
    }

    @Override
    public void update(Commande commande) {
        em.merge(commande);
    }

    @Override
    public void delete(Long id) {
        Commande c = em.find(Commande.class, id);
        if (c != null) em.remove(c);
    }

    @Override
    public List<Commande> findByStatut(String statut) {
        return em.createQuery("SELECT c FROM Commande c WHERE c.statut = :statut ORDER BY c.dateCommande DESC", Commande.class)
                 .setParameter("statut", statut)
                 .getResultList();
    }

    @Override
    public String generateCodeCommande() {
        Long count = em.createQuery("SELECT COUNT(c) FROM Commande c", Long.class)
                       .getSingleResult();
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format("CMD-%s-%05d", date, count + 1);
    }
}