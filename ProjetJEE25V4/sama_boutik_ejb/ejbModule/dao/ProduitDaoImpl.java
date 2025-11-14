package dao;

import entity.Produit;
import entity.EntiteCommerciale;
import entity.Famille;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProduitDaoImpl implements ProduitDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(Produit produit) {
        em.persist(produit);
    }

    @Override
    public List<Produit> findAll() {
        try {
            List<Produit> result = em.createQuery("SELECT p FROM Produit p", Produit.class)
                .getResultList();
            System.out.println("📦 ProduitDao.findAll() retourne: " + result.size() + " produits");
            return result;
        } catch (Exception e) {
            System.err.println("❌ Erreur findAll produits: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Produit findById(Long id) {
        return em.find(Produit.class, id);
    }

    @Override
    public void update(Produit produit) {
        em.merge(produit);
    }

    @Override
    public void delete(Long id) {
        Produit p = em.find(Produit.class, id);
        if (p != null) em.remove(p);
    }

    @Override
    public List<Produit> findByFamille(Famille famille, int page, int size) {
        return em.createQuery("SELECT p FROM Produit p WHERE p.famille = :fam ORDER BY p.nom", Produit.class)
                 .setParameter("fam", famille)
                 .setFirstResult(page * size)
                 .setMaxResults(size)
                 .getResultList();
    }

    @Override
    public long countByFamille(Famille famille) {
        return em.createQuery("SELECT COUNT(p) FROM Produit p WHERE p.famille = :fam", Long.class)
                 .setParameter("fam", famille)
                 .getSingleResult();
    }

    @Override
    public String generateReference(String familleCode) {
        Long count = em.createQuery("SELECT COUNT(p) FROM Produit p", Long.class)
                       .getSingleResult();
        return String.format("PRD-%s-%03d", familleCode.toUpperCase().substring(0, 3), count + 1);
    }
    /*
    @Override
    public List<Produit> findByEntite(EntiteCommerciale entite) {
        if (entite == null) return List.of();

        return em.createQuery(
            "SELECT p FROM Produit p " +
            "WHERE p.entiteCommerciale = :entite " +
            "ORDER BY p.nom",
            Produit.class
        )
        .setParameter("entite", entite)
        .getResultList();
    }*/
    
    @Override
    public List<Produit> findByFamilleAndEntite(Famille famille, EntiteCommerciale entite, int page, int size) {
        try {
            System.out.println("📦 Recherche produits - Famille ID: " + famille.getId() + 
                             ", Entité ID: " + entite.getId());
            
            List<Produit> produits = em.createQuery(
                "SELECT p FROM Produit p " +
                "WHERE p.famille.id = :familleId " +
                "AND p.entiteCommerciale.id = :entiteId " +
                "ORDER BY p.nom ASC",
                Produit.class
            )
            .setParameter("familleId", famille.getId())
            .setParameter("entiteId", entite.getId())
            .setFirstResult(page * size)
            .setMaxResults(size)
            .getResultList();
            
            System.out.println("✅ Produits chargés: " + produits.size());
            
            // Debug: afficher les produits
            for (Produit p : produits) {
                System.out.println("  - " + p.getNom() + " (ID: " + p.getId() + 
                                 ", Entité: " + (p.getEntiteCommerciale() != null ? 
                                               p.getEntiteCommerciale().getNom() : "null") + ")");
            }
            
            return produits;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur findByFamilleAndEntite: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


   
    /**
     * Compte les produits d'une famille ET d'une entité
     */ 
    @Override
    public long countByFamilleAndEntite(Famille famille, EntiteCommerciale entite) {
        try {
            return em.createQuery(
                "SELECT COUNT(p) FROM Produit p " +
                "WHERE p.famille.id = :familleId " +
                "AND p.entiteCommerciale.id = :entiteId",
                Long.class
            )
            .setParameter("familleId", famille.getId())
            .setParameter("entiteId", entite.getId())
            .getSingleResult();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur countByFamilleAndEntite: " + e.getMessage());
            e.printStackTrace();
            return 0L;
        }
    }
    
    
    /**
     * Trouve tous les produits d'une entité (sans pagination)
     */  
    @Override
    public List<Produit> findByEntite(EntiteCommerciale entite) {
        try {
            System.out.println("📦 Recherche tous les produits pour entité ID: " + entite.getId());
            
            List<Produit> produits = em.createQuery(
                "SELECT p FROM Produit p " +
                "WHERE p.entiteCommerciale.id = :entiteId " +
                "ORDER BY p.nom ASC",
                Produit.class
            )
            .setParameter("entiteId", entite.getId())
            .getResultList();
            
            System.out.println("✅ Produits trouvés: " + produits.size());
            
            return produits;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur findByEntite: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ========================================

    // Ajoutez cette implémentation dans ProduitDaoImpl

    @Override
    public List<Produit> findBySearchTermAndEntite(String searchTerm, EntiteCommerciale entite) {
        try {
            System.out.println("🔍 Recherche produits : terme='" + searchTerm + "', entite=" + entite.getId());
            
            String term = "%" + searchTerm.toLowerCase() + "%";
            
            List<Produit> resultats = em.createQuery(
                "SELECT p FROM Produit p " +
                "WHERE p.entiteCommerciale.id = :entiteId " +
                "AND (LOWER(p.nom) LIKE :term " +
                "  OR LOWER(p.reference) LIKE :term " +
                "  OR LOWER(p.description) LIKE :term) " +
                "ORDER BY p.nom ASC",
                Produit.class
            )
            .setParameter("entiteId", entite.getId())
            .setParameter("term", term)
            .setMaxResults(20) // Limiter à 20 résultats
            .getResultList();
            
            System.out.println("✅ " + resultats.size() + " produits trouvés");
            
            return resultats;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur findBySearchTermAndEntite: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}