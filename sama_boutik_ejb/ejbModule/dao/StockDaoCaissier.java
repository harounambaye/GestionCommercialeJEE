package dao;

import entity.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class StockDaoCaissier {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    /**
     * Trouve les stocks d'une entité avec pagination
     */
    public List<Stock> findByEntite(Long entiteId, int page, int size) {
        try {
            return em.createQuery("""
                SELECT s FROM Stock s
                WHERE s.entiteCommerciale.id = :entiteId
                AND s.etat = true
                ORDER BY s.nom
            """, Stock.class)
                .setParameter("entiteId", entiteId)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur findByEntite: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Compte les stocks d'une entité
     */
    public long countByEntite(Long entiteId) {
        try {
            return em.createQuery("""
                SELECT COUNT(s) FROM Stock s
                WHERE s.entiteCommerciale.id = :entiteId
                AND s.etat = true
            """, Long.class)
                .setParameter("entiteId", entiteId)
                .getSingleResult();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur countByEntite: " + e.getMessage());
            return 0L;
        }
    }

    /**
     * Trouve les lots d'un stock
     */
    public List<Lot> findLotsByStock(Long stockId) {
        try {
            return em.createQuery("""
                SELECT l FROM Lot l
                WHERE l.stock.id = :stockId
                ORDER BY l.dateReception DESC
            """, Lot.class)
                .setParameter("stockId", stockId)
                .getResultList();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur findLotsByStock: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Trouve les articles d'un lot
     */
    public List<Article> findArticlesByLot(Long lotId) {
        try {
            return em.createQuery("""
                SELECT a FROM Article a
                JOIN FETCH a.produit p
                WHERE a.lot.id = :lotId
                ORDER BY p.nom
            """, Article.class)
                .setParameter("lotId", lotId)
                .getResultList();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur findArticlesByLot: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Statistiques du stock pour le dashboard
     */
    public StockStats getStats(Long entiteId) {
        try {
            StockStats stats = new StockStats();
            
            // Nombre de stocks
            stats.setNombreStocks(countByEntite(entiteId));
            
            // Nombre total de lots
            Long totalLots = em.createQuery("""
                SELECT COUNT(l) FROM Lot l
                WHERE l.stock.entiteCommerciale.id = :entiteId
            """, Long.class)
                .setParameter("entiteId", entiteId)
                .getSingleResult();
            stats.setTotalLots(totalLots);
            
            // Articles disponibles (quantité > 0)
            Long articlesDisponibles = em.createQuery("""
                SELECT COUNT(a) FROM Article a
                WHERE a.produit.entiteCommerciale.id = :entiteId
                AND a.quantiteRestante > 0
            """, Long.class)
                .setParameter("entiteId", entiteId)
                .getSingleResult();
            stats.setArticlesDisponibles(articlesDisponibles);
            
            // Quantité totale en stock
            Long quantiteTotale = em.createQuery("""
                SELECT COALESCE(SUM(a.quantiteRestante), 0) FROM Article a
                WHERE a.produit.entiteCommerciale.id = :entiteId
            """, Long.class)
                .setParameter("entiteId", entiteId)
                .getSingleResult();
            stats.setQuantiteTotale(quantiteTotale);
            
            // Nombre de produits distincts
            Long produitsDistincts = em.createQuery("""
                SELECT COUNT(DISTINCT a.produit.id) FROM Article a
                WHERE a.produit.entiteCommerciale.id = :entiteId
                AND a.quantiteRestante > 0
            """, Long.class)
                .setParameter("entiteId", entiteId)
                .getSingleResult();
            stats.setProduitsDistincts(produitsDistincts);
            
            // Valeur totale du stock (quantité × prix unitaire)
            BigDecimal valeurTotale = em.createQuery("""
                SELECT COALESCE(SUM(a.quantiteRestante * a.prixUnitaire), 0) FROM Article a
                WHERE a.produit.entiteCommerciale.id = :entiteId
                AND a.quantiteRestante > 0
                AND a.prixUnitaire IS NOT NULL
            """, BigDecimal.class)
                .setParameter("entiteId", entiteId)
                .getSingleResult();
            stats.setValeurTotale(valeurTotale != null ? valeurTotale.doubleValue() : 0.0);
            
            return stats;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur getStats: " + e.getMessage());
            e.printStackTrace();
            return new StockStats();
        }
    }

    /**
     * Classe interne pour les statistiques stock
     */
    public static class StockStats {
        private long nombreStocks = 0;
        private long totalLots = 0;
        private long articlesDisponibles = 0;
        private long quantiteTotale = 0;
        private long produitsDistincts = 0;
        private double valeurTotale = 0.0;

        // Getters
        public long getNombreStocks() { return nombreStocks; }
        public long getTotalLots() { return totalLots; }
        public long getArticlesDisponibles() { return articlesDisponibles; }
        public long getQuantiteTotale() { return quantiteTotale; }
        public long getProduitsDistincts() { return produitsDistincts; }
        public double getValeurTotale() { return valeurTotale; }

        // Setters
        public void setNombreStocks(long nombreStocks) { this.nombreStocks = nombreStocks; }
        public void setTotalLots(long totalLots) { this.totalLots = totalLots; }
        public void setArticlesDisponibles(long articlesDisponibles) { this.articlesDisponibles = articlesDisponibles; }
        public void setQuantiteTotale(long quantiteTotale) { this.quantiteTotale = quantiteTotale; }
        public void setProduitsDistincts(long produitsDistincts) { this.produitsDistincts = produitsDistincts; }
        public void setValeurTotale(double valeurTotale) { this.valeurTotale = valeurTotale; }
    }
}