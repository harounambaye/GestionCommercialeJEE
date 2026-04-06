package dao;

import entity.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class LivraisonDaoCaissier {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    /**
     * Trouve une livraison par son ID de commande
     */
    public Livraison findByCommande(Long commandeId) {
        try {
            return em.createQuery("""
                SELECT l FROM Livraison l
                JOIN FETCH l.commande c
                LEFT JOIN FETCH l.livreur liv
                LEFT JOIN FETCH liv.personne
                WHERE c.id = :commandeId
                """, Livraison.class)
                .setParameter("commandeId", commandeId)
                .getSingleResult();
            
        } catch (Exception e) {
            System.err.println("❌ Aucune livraison trouvée pour la commande: " + commandeId);
            return null;
        }
    }

    /**
     * Trouve les livraisons d'une entité avec pagination et filtre statut
     */
    public List<Livraison> findByEntiteAndStatut(Long entiteId, String statut, int page, int size) {
        try {
            String jpql = """
                SELECT DISTINCT l FROM Livraison l
                JOIN FETCH l.commande c
                LEFT JOIN FETCH l.livreur
                JOIN c.detailsCommandes dc
                JOIN dc.article a
                JOIN a.produit p
                WHERE p.entiteCommerciale.id = :entiteId
            """;
            
            if (statut != null && !statut.equals("TOUS")) {
                jpql += " AND l.statutlivraison = :statut";
            }
            
            jpql += " ORDER BY l.createdat DESC";
            
            var query = em.createQuery(jpql, Livraison.class)
                .setParameter("entiteId", entiteId)
                .setFirstResult(page * size)
                .setMaxResults(size);
            
            if (statut != null && !statut.equals("TOUS")) {
                query.setParameter("statut", statut);
            }
            
            return query.getResultList();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur findByEntiteAndStatut: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Compte les livraisons par statut
     */
    public long countByEntiteAndStatut(Long entiteId, String statut) {
        try {
            String jpql = """
                SELECT COUNT(DISTINCT l) FROM Livraison l
                JOIN l.commande c
                JOIN c.detailsCommandes dc
                JOIN dc.article a
                JOIN a.produit p
                WHERE p.entiteCommerciale.id = :entiteId
            """;
            
            if (statut != null && !statut.equals("TOUS")) {
                jpql += " AND l.statutlivraison = :statut";
            }
            
            var query = em.createQuery(jpql, Long.class)
                .setParameter("entiteId", entiteId);
            
            if (statut != null && !statut.equals("TOUS")) {
                query.setParameter("statut", statut);
            }
            
            return query.getSingleResult();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur countByEntiteAndStatut: " + e.getMessage());
            return 0L;
        }
    }

    /**
     * Met à jour le statut d'une livraison
     */
    public void updateStatut(Long livraisonId, String statut) {
        try {
            Livraison livraison = em.find(Livraison.class, livraisonId);
            if (livraison != null) {
                livraison.setStatutlivraison(statut);
                em.merge(livraison);
                System.out.println("✅ Statut livraison mis à jour: " + livraisonId + " → " + statut);
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur updateStatut: " + e.getMessage());
        }
    }

    /**
     * Attribue un livreur à une livraison
     */
    public void attribuerLivreur(Long livraisonId, Employe livreur) {
        try {
            Livraison livraison = em.find(Livraison.class, livraisonId);
            if (livraison != null) {
                livraison.setLivreur(livreur);
                livraison.setStatutlivraison("EN_COURS");
                // Définir la date de livraison prévue (par défaut: +1 jour)
                livraison.setDatelivraisonprevue(java.time.LocalDateTime.now().plusDays(1));
                em.merge(livraison);
                System.out.println("✅ Livreur attribué: " + livreur.getPersonne().getNom() + " → " + livraisonId);
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur attribuerLivreur: " + e.getMessage());
        }
    }

    /**
     * ✅ CORRECTION : Liste des livreurs disponibles
     * Cherche toutes les personnes ayant le profil "Livreur"
     */
    public List<Personne> findLivreursDisponibles() {
        try {
            // Méthode 1 : Par titre de profil (plus flexible)
            List<Personne> livreurs = em.createQuery("""
                SELECT DISTINCT pp.personne FROM PersonneProfil pp
                WHERE LOWER(pp.profil.titre) LIKE '%livreur%'
                AND pp.personne.actif = true
                ORDER BY pp.personne.nom, pp.personne.prenom
            """, Personne.class)
                .getResultList();
            
            System.out.println("✅ " + livreurs.size() + " livreurs trouvés");
            
            // Si aucun livreur trouvé avec la méthode 1, essayer par ID
            if (livreurs.isEmpty()) {
                System.out.println("⚠️ Aucun livreur trouvé par titre, essai par ID...");
                livreurs = em.createQuery("""
                    SELECT DISTINCT pp.personne FROM PersonneProfil pp
                    WHERE pp.profil.id = 6
                    AND pp.personne.actif = true
                    ORDER BY pp.personne.nom, pp.personne.prenom
                """, Personne.class)
                    .getResultList();
                
                System.out.println("✅ " + livreurs.size() + " livreurs trouvés par ID");
            }
            
            // Debug : afficher les profils disponibles si toujours vide
            if (livreurs.isEmpty()) {
                System.out.println("🔍 Liste des profils existants:");
                List<Object[]> profils = em.createQuery(
                    "SELECT p.id, p.titre FROM Profil p ORDER BY p.id", Object[].class)
                    .getResultList();
                for (Object[] profil : profils) {
                    System.out.println("  - ID " + profil[0] + ": " + profil[1]);
                }
            }
            
            return livreurs;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur findLivreursDisponibles: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Livraison save(Livraison livraison) {
        try {
            if (livraison.getId() == null) {
                em.persist(livraison);
                System.out.println("✅ Livraison créée: " + livraison.getId());
            } else {
                livraison = em.merge(livraison);
                System.out.println("✅ Livraison mise à jour: " + livraison.getId());
            }
            return livraison;

        } catch (Exception e) {
            System.err.println("❌ Erreur save Livraison: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Vérifie si une commande a déjà une livraison
     */
    public boolean existeLivraisonPourCommande(Long commandeId) {
        try {
            Long count = em.createQuery("""
                SELECT COUNT(l) FROM Livraison l
                WHERE l.commande.id = :commandeId
                """, Long.class)
                .setParameter("commandeId", commandeId)
                .getSingleResult();
            
            return count > 0;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur existeLivraisonPourCommande: " + e.getMessage());
            return false;
        }
    }
}