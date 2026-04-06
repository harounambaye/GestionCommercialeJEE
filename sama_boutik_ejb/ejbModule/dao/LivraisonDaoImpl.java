package dao;

import entity.Livraison;
import entity.Commande;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class LivraisonDaoImpl implements LivraisonDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(Livraison livraison) {
        em.persist(livraison);
        System.out.println("✅ Livraison enregistrée pour commande : " + 
                         livraison.getCommande().getCodeCommande());
    }

    @Override
    public Livraison findByCommande(Commande commande) {
        try {
            return em.createQuery(
                "SELECT l FROM Livraison l " +
                "LEFT JOIN FETCH l.livreur " +
                "WHERE l.commande = :commande",
                Livraison.class
            )
            .setParameter("commande", commande)
            .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Livraison> findAll() {
        return em.createQuery(
            "SELECT l FROM Livraison l " +
            "LEFT JOIN FETCH l.commande " +
            "LEFT JOIN FETCH l.livreur " +
            "ORDER BY l.createdat DESC",
            Livraison.class
        ).getResultList();
    }

    @Override
    public void update(Livraison livraison) {
        em.merge(livraison);
    }
}