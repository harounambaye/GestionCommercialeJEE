package dao;

import entity.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class CommandeDaoCaissier {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    /**
     * Trouve les commandes d'une entité avec pagination et filtre statut
     */
    public List<Commande> findByEntiteAndStatut(Long entiteId, String statut, int page, int size) {
        try {
            // ✅ Syntaxe OpenJPA compatible (jointures implicites)
            String jpql = """
                SELECT DISTINCT c FROM Commande c, DetailCommande dc
                WHERE dc.commande.id = c.id
                AND dc.article.produit.entiteCommerciale.id = :entiteId
            """;
            
            if (statut != null && !statut.equals("TOUS")) {
                jpql += " AND c.statut = :statut";
            }
            
            jpql += " ORDER BY c.dateCommande DESC";
            
            var query = em.createQuery(jpql, Commande.class)
                .setParameter("entiteId", entiteId)
                .setFirstResult(page * size)
                .setMaxResults(size);
            
            if (statut != null && !statut.equals("TOUS")) {
                query.setParameter("statut", statut);
            }
            
            return query.getResultList();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur findByEntiteAndStatut: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Compte les commandes d'une entité par statut
     */
    public long countByEntiteAndStatut(Long entiteId, String statut) {
        try {
            String jpql = """
                SELECT COUNT(DISTINCT c) FROM Commande c, DetailCommande dc
                WHERE dc.commande.id = c.id
                AND dc.article.produit.entiteCommerciale.id = :entiteId
            """;
            
            if (statut != null && !statut.equals("TOUS")) {
                jpql += " AND c.statut = :statut";
            }
            
            var query = em.createQuery(jpql, Long.class)
                .setParameter("entiteId", entiteId);
            
            if (statut != null && !statut.equals("TOUS")) {
                query.setParameter("statut", statut);
            }
            
            return query.getSingleResult();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur countByEntiteAndStatut: " + e.getMessage());
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * Trouve les nouvelles commandes depuis une date
     */
    public List<Commande> findNouvellesCommandes(Long entiteId, LocalDateTime depuis) {
        try {
            return em.createQuery("""
                SELECT DISTINCT c FROM Commande c, DetailCommande dc
                WHERE dc.commande.id = c.id
                AND dc.article.produit.entiteCommerciale.id = :entiteId
                AND c.dateCommande > :depuis
                AND c.statut = 'EN_ATTENTE'
                ORDER BY c.dateCommande DESC
            """, Commande.class)
                .setParameter("entiteId", entiteId)
                .setParameter("depuis", depuis)
                .getResultList();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur findNouvellesCommandes: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Vérifie si une commande appartient à l'entité
     */
    public boolean commandeAppartientEntite(Long commandeId, Long entiteId) {
        try {
            Long count = em.createQuery("""
                SELECT COUNT(DISTINCT c) FROM Commande c, DetailCommande dc
                WHERE dc.commande.id = c.id
                AND c.id = :commandeId
                AND dc.article.produit.entiteCommerciale.id = :entiteId
            """, Long.class)
                .setParameter("commandeId", commandeId)
                .setParameter("entiteId", entiteId)
                .getSingleResult();
            
            return count > 0;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur commandeAppartientEntite: " + e.getMessage());
            return false;
        }
    }

    /**
     * Met à jour le statut d'une commande
     */
    public void updateStatut(Long commandeId, String statut) {
        try {
            Commande commande = em.find(Commande.class, commandeId);
            if (commande != null) {
                commande.setStatut(statut);
                em.merge(commande);
                System.out.println("✅ Statut commande mis à jour: " + commandeId + " → " + statut);
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur updateStatut: " + e.getMessage());
        }
    }

    /**
     * Trouve les articles disponibles d'une entité pour créer une commande
     */
    /**
     * Trouve les articles disponibles d'une entité avec recherche et pagination
     */
    public List<Article> findArticlesDisponibles(Long entiteId, String recherche, int page, int size) {
        try {
            String jpql = """
                SELECT a FROM Article a
                WHERE a.produit.entiteCommerciale.id = :entiteId
                AND a.quantiteRestante > 0
            """;
            
            // Ajouter le filtre de recherche si nécessaire
            if (recherche != null && !recherche.trim().isEmpty()) {
                jpql += " AND LOWER(a.produit.nom) LIKE :recherche";
            }
            
            jpql += " ORDER BY a.produit.nom";
            
            var query = em.createQuery(jpql, Article.class)
                .setParameter("entiteId", entiteId)
                .setFirstResult(page * size)
                .setMaxResults(size);
            
            if (recherche != null && !recherche.trim().isEmpty()) {
                query.setParameter("recherche", "%" + recherche.trim().toLowerCase() + "%");
            }
            
            List<Article> resultList = query.getResultList();
            
            // ✅ Créer une nouvelle liste modifiable
            List<Article> articles = new ArrayList<>(resultList);
            
            // ✅ Force le chargement des produits
            for (Article article : articles) {
                if (article.getProduit() != null) {
                    article.getProduit().getNom();
                }
            }
            
            System.out.println("✅ " + articles.size() + " articles chargés (recherche: '" + recherche + "')");
            return articles;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur findArticlesDisponibles: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Compte les articles disponibles avec recherche
     */
    public long countArticlesDisponibles(Long entiteId, String recherche) {
        try {
            String jpql = """
                SELECT COUNT(a) FROM Article a
                WHERE a.produit.entiteCommerciale.id = :entiteId
                AND a.quantiteRestante > 0
            """;
            
            if (recherche != null && !recherche.trim().isEmpty()) {
                jpql += " AND LOWER(a.produit.nom) LIKE :recherche";
            }
            
            var query = em.createQuery(jpql, Long.class)
                .setParameter("entiteId", entiteId);
            
            if (recherche != null && !recherche.trim().isEmpty()) {
                query.setParameter("recherche", "%" + recherche.trim().toLowerCase() + "%");
            }
            
            return query.getSingleResult();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur countArticlesDisponibles: " + e.getMessage());
            return 0L;
        }
    }
    /**
     * Statistiques rapides pour le dashboard
     */
    public CommandeStats getStats(Long entiteId) {
        try {
            CommandeStats stats = new CommandeStats();
            
            // Commandes en attente
            stats.setEnAttente(countByEntiteAndStatut(entiteId, "EN_ATTENTE"));
            
            // Commandes validées
            stats.setValidees(countByEntiteAndStatut(entiteId, "VALIDEE"));
            
            // Commandes livrées aujourd'hui
            LocalDateTime debutJour = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            Long livrees = em.createQuery("""
                SELECT COUNT(DISTINCT c) FROM Commande c, DetailCommande dc
                WHERE dc.commande.id = c.id
                AND dc.article.produit.entiteCommerciale.id = :entiteId
                AND c.statut = 'LIVREE'
                AND c.dateCommande >= :debutJour
            """, Long.class)
                .setParameter("entiteId", entiteId)
                .setParameter("debutJour", debutJour)
                .getSingleResult();
            stats.setLivreesAujourdhui(livrees);
            
            // Montant total du jour
            BigDecimal montant = em.createQuery("""
                SELECT COALESCE(SUM(c.total), 0) FROM Commande c, DetailCommande dc
                WHERE dc.commande.id = c.id
                AND dc.article.produit.entiteCommerciale.id = :entiteId
                AND c.dateCommande >= :debutJour
            """, BigDecimal.class)
                .setParameter("entiteId", entiteId)
                .setParameter("debutJour", debutJour)
                .getSingleResult();
            stats.setMontantJour(montant != null ? montant.doubleValue() : 0.0);
            
            // Articles disponibles
            Long articlesDisponibles = em.createQuery("""
                SELECT COUNT(a) FROM Article a
                WHERE a.produit.entiteCommerciale.id = :entiteId
                AND a.quantiteRestante > 0
            """, Long.class)
                .setParameter("entiteId", entiteId)
                .getSingleResult();
            stats.setArticlesDisponibles(articlesDisponibles);
            
            return stats;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur getStats: " + e.getMessage());
            e.printStackTrace();
            return new CommandeStats();
        }
    }
    
    /**
     * Trouve une commande par son ID
     */
    public Commande findById(Long commandeId) {
        try {
            return em.find(Commande.class, commandeId);
        } catch (Exception e) {
            System.err.println("❌ Erreur findById: " + e.getMessage());
            return null;
        }
    }

    // Classe interne pour les stats
    public static class CommandeStats {
        private long enAttente = 0;
        private long validees = 0;
        private long livreesAujourdhui = 0;
        private double montantJour = 0.0;
        private long articlesDisponibles = 0;

        // Getters
        public long getEnAttente() { return enAttente; }
        public long getValidees() { return validees; }
        public long getLivreesAujourdhui() { return livreesAujourdhui; }
        public double getMontantJour() { return montantJour; }
        public long getArticlesDisponibles() { return articlesDisponibles; }

        // Setters
        public void setEnAttente(long enAttente) { this.enAttente = enAttente; }
        public void setValidees(long validees) { this.validees = validees; }
        public void setLivreesAujourdhui(long livreesAujourdhui) { this.livreesAujourdhui = livreesAujourdhui; }
        public void setMontantJour(double montantJour) { this.montantJour = montantJour; }
        public void setArticlesDisponibles(long articlesDisponibles) { this.articlesDisponibles = articlesDisponibles; }
    }
}