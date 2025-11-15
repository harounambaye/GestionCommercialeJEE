package dao;

import entity.Lot;
import entity.Produit;
import entity.Stock;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class LotDaoImpl implements LotDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(Lot lot) {
        em.persist(lot);
    }

    @Override
    public List<Lot> findAll() {
        return em.createQuery("SELECT l FROM Lot l ORDER BY l.createdAt DESC", Lot.class)
                 .getResultList();
    }

    @Override
    public Lot findById(Long id) {
        return em.find(Lot.class, id);
    }

    @Override
    public void delete(Long id) {
        Lot l = em.find(Lot.class, id);
        if (l != null) em.remove(l);
    }

    @Override
    public List<Lot> findByStock(Stock stock) {
        try {
            return em.createQuery(
                "SELECT l FROM Lot l " +
                "WHERE l.stock.id = :stockId " +
                "ORDER BY l.createdAt DESC",
                Lot.class
            )
            .setParameter("stockId", stock.getId())
            .getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erreur findByStock: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * ✅ VERSION CORRIGÉE : Utiliser FUNCTION pour extraire l'année
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Lot> findByStockAndYear(Stock stock, int year) {
        try {
            System.out.println("📦 Recherche lots du stock " + stock.getNom() +
                               " pour l'année " + year);

            String nativeSql = "SELECT l.* FROM lot l " +
                               "WHERE l.idstock = ? " +
                               "AND EXTRACT(YEAR FROM l.datereception) = ? " +
                               "ORDER BY l.datereception DESC";

            List<Lot> resultats = (List<Lot>) em.createNativeQuery(nativeSql, Lot.class)
                .setParameter(1, stock.getId())
                .setParameter(2, year)
                .getResultList();

            for (Lot lot : resultats) {
                if (lot.getArticles() != null) {
                    lot.getArticles().size(); // Force le chargement
                    System.out.println("  Lot " + lot.getCodeLot() + " : " +
                        lot.getArticles().size() + " articles");
                }
            }

            return resultats;

        } catch (Exception e) {
            System.err.println("❌ Erreur findByStockAndYear (SQL natif): " + e.getMessage());
            e.printStackTrace();

            return findByStockAndYearFallback(stock, year);
        }
    }

    
    /**
     * ✅ VERSION ALTERNATIVE : Comparaison de chaînes
     */
    @Override
    public List<Lot> findByStockAndYearSimple(Stock stock, int year) {
        try {
            System.out.println("📦 Recherche SIMPLE lots du stock " + stock.getNom());
            
            // ✅ SOLUTION 2 : Récupérer tous les lots et filtrer en Java
            List<Lot> tousLesLots = em.createQuery(
                "SELECT l FROM Lot l WHERE l.stock.id = :stockId ORDER BY l.dateReception DESC",
                Lot.class
            )
            .setParameter("stockId", stock.getId())
            .getResultList();
            
            List<Lot> resultats = new ArrayList<>();
            for (Lot lot : tousLesLots) {
                if (lot.getDateReception() != null && 
                    lot.getDateReception().getYear() == year) {
                    // Forcer le chargement des articles
                    if (lot.getArticles() != null) {
                        lot.getArticles().size();
                        System.out.println("  Lot " + lot.getCodeLot() + " : " + 
                            lot.getArticles().size() + " articles");
                    }
                    resultats.add(lot);
                }
            }
            
            System.out.println("✅ " + resultats.size() + " lots filtrés en Java");
            return resultats;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur findByStockAndYearSimple: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * ✅ SOLUTION DE SECOURS : Filtrage en Java
     */
    private List<Lot> findByStockAndYearFallback(Stock stock, int year) {
        try {
            System.out.println("⚠️ Utilisation du fallback Java pour filtrer par année");
            
            List<Lot> tousLesLots = em.createQuery(
                "SELECT l FROM Lot l WHERE l.stock.id = :stockId ORDER BY l.dateReception DESC",
                Lot.class
            )
            .setParameter("stockId", stock.getId())
            .getResultList();
            
            List<Lot> resultats = new ArrayList<>();
            for (Lot lot : tousLesLots) {
                if (lot.getDateReception() != null && 
                    lot.getDateReception().getYear() == year) {
                    // Forcer le chargement des articles
                    if (lot.getArticles() != null) {
                        lot.getArticles().size();
                    }
                    resultats.add(lot);
                }
            }
            
            System.out.println("✅ " + resultats.size() + " lots filtrés en Java");
            return resultats;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur fallback: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
 
    
    @Override
    public long countByProduit(Produit produit) {
        try {
            return em.createQuery(
                "SELECT COUNT(a) FROM Article a " +
                "WHERE a.produit.id = :produitId",
                Long.class
            )
            .setParameter("produitId", produit.getId())
            .getSingleResult();
        } catch (Exception e) {
            System.err.println("❌ Erreur countByProduit: " + e.getMessage());
            return 0L;
        }
    }

    @Override
    public String generateCodeLot() {
        try {
            String prefix = "LOT-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-";
            Long seq = ((Number) em.createNativeQuery("SELECT nextval('lot_seq')").getSingleResult()).longValue();
            String code = prefix + String.format("%04d", seq);
            System.out.println("🔢 Code lot généré : " + code);
            return code;
        } catch (Exception e) {
            System.err.println("❌ Erreur generateCodeLot: " + e.getMessage());
            String fallback = "LOT-" + System.currentTimeMillis();
            System.out.println("⚠️ Utilisation code fallback : " + fallback);
            return fallback;
        }
    }

}