package dao;

import entity.EntiteCommerciale;
import entity.Stock;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class StockDaoImpl implements StockDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(Stock stock) {
        em.persist(stock);
    }

    @Override
    public List<Stock> findAll() {
        return em.createQuery("SELECT s FROM Stock s ORDER BY s.createdAt DESC", Stock.class)
                 .getResultList();
    }

    @Override
    public Stock findById(Long id) {
        return em.find(Stock.class, id);
    }

    @Override
    public void update(Stock stock) {
        em.merge(stock);
    }

    @Override
    public void delete(Long id) {
        Stock s = em.find(Stock.class, id);
        if (s != null) em.remove(s);
    }

    @Override
    public List<Stock> findByYear(int year, int page, int size) {
        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, 12, 31, 23, 59, 59);
        
        return em.createQuery(
            "SELECT s FROM Stock s " +
            "WHERE s.createdAt >= :start AND s.createdAt <= :end " +
            "ORDER BY s.createdAt DESC", 
            Stock.class)
            .setParameter("start", startDate)
            .setParameter("end", endDate)
            .setFirstResult(page * size)
            .setMaxResults(size)
            .getResultList();
    }

    @Override
    public long countByYear(int year) {
        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, 12, 31, 23, 59, 59);
        
        return em.createQuery(
            "SELECT COUNT(s) FROM Stock s " +
            "WHERE s.createdAt >= :start AND s.createdAt <= :end", 
            Long.class)
            .setParameter("start", startDate)
            .setParameter("end", endDate)
            .getSingleResult();
    }
    
    /**
     * ✅ CORRECTION : Utiliser des intervalles de dates au lieu de YEAR()
     * Trouve les stocks d'une entité filtrés par année avec pagination
     */
    @Override
    public List<Stock> findByEntiteAndYear(EntiteCommerciale entite, int annee, int page, int size) {
        try {
            System.out.println("📊 Recherche stocks pour entité ID: " + entite.getId() + ", année: " + annee);
            
            // ✅ Créer les bornes de l'année
            LocalDateTime debutAnnee = LocalDateTime.of(annee, 1, 1, 0, 0, 0);
            LocalDateTime finAnnee = LocalDateTime.of(annee, 12, 31, 23, 59, 59);
            
            System.out.println("  Période : " + debutAnnee + " → " + finAnnee);
            
            List<Stock> resultats = em.createQuery(
                "SELECT s FROM Stock s " +
                "WHERE s.entiteCommerciale.id = :entiteId " +
                "AND s.createdAt >= :debut " +
                "AND s.createdAt <= :fin " +
                "AND s.etat = true " +
                "ORDER BY s.createdAt DESC",
                Stock.class
            )
            .setParameter("entiteId", entite.getId())
            .setParameter("debut", debutAnnee)
            .setParameter("fin", finAnnee)
            .setFirstResult(page * size)
            .setMaxResults(size)
            .getResultList();
            
            System.out.println("✅ " + resultats.size() + " stocks trouvés");
            
            return resultats;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur findByEntiteAndYear: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * ✅ CORRECTION : Utiliser des intervalles de dates
     * Compte les stocks d'une entité pour une année
     */
    @Override
    public long countByEntiteAndYear(EntiteCommerciale entite, int annee) {
        try {
            // ✅ Créer les bornes de l'année
            LocalDateTime debutAnnee = LocalDateTime.of(annee, 1, 1, 0, 0, 0);
            LocalDateTime finAnnee = LocalDateTime.of(annee, 12, 31, 23, 59, 59);
            
            Long count = em.createQuery(
                "SELECT COUNT(s) FROM Stock s " +
                "WHERE s.entiteCommerciale.id = :entiteId " +
                "AND s.createdAt >= :debut " +
                "AND s.createdAt <= :fin " +
                "AND s.etat = true",
                Long.class
            )
            .setParameter("entiteId", entite.getId())
            .setParameter("debut", debutAnnee)
            .setParameter("fin", finAnnee)
            .getSingleResult();
            
            System.out.println("📊 Total stocks pour l'année " + annee + " : " + count);
            
            return count;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur countByEntiteAndYear: " + e.getMessage());
            e.printStackTrace();
            return 0L;
        }
    }
}