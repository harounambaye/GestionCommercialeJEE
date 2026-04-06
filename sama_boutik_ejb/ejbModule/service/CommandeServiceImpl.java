package service;

import entity.*;
import dao.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Stateless
public class CommandeServiceImpl implements CommandeService {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @EJB
    private CommandeDao commandeDao;

    @EJB
    private DetailCommandeDao detailCommandeDao;

    @EJB
    private LivraisonDao livraisonDao;

    @EJB
    private ArticleDao articleDao;

    @Override
    public Commande creerCommande(
        Personne client,
        Map<Article, Integer> panier,
        String typeCommande,
        String adresseLivraison,
        String telephoneLivraison,
        String referencePaiement
    ) {
        try {
            System.out.println("🛒 Création commande pour : " + client.getEmail());

            // 1️⃣ Créer la commande
            Commande commande = new Commande();
            commande.setIdPersonne(client.getId());
            commande.setCodeCommande(commandeDao.genererCodeCommande());
            commande.setTypecommande(typeCommande);
            commande.setDateCommande(LocalDateTime.now());
            commande.setTotal(calculerTotal(panier));
            commande.setStatut("En attente");
            commande.setReferencepaiement(referencePaiement);

            commandeDao.save(commande);

            // 2️⃣ Créer les détails de commande
            for (Map.Entry<Article, Integer> entry : panier.entrySet()) {
                Article article = entry.getKey();
                Integer quantite = entry.getValue();

                // Vérifier le stock
                if (article.getQuantiteRestante() < quantite) {
                    throw new RuntimeException(
                        "Stock insuffisant pour : " + article.getProduit().getNom());
                }

                // Créer le détail
                DetailCommande detail = new DetailCommande();
                detail.setCommande(commande);
                detail.setArticle(article);
                detail.setQuantite(quantite);
                detail.setPrixunitaire(article.getPrixUnitaire());
                detail.setTotalligne(article.getPrixUnitaire().multiply(new BigDecimal(quantite)));

                detailCommandeDao.save(detail);

                // Décrémenter le stock
                article.setQuantiteRestante(article.getQuantiteRestante() - quantite);
                articleDao.update(article);

                System.out.println("  ✅ Ajouté : " + article.getProduit().getNom() + 
                                 " x" + quantite);
            }

            // 3️⃣ Créer la livraison si nécessaire
            if ("AVEC_LIVRAISON".equals(typeCommande)) {
                Livraison livraison = new Livraison();
                livraison.setCommande(commande);
                livraison.setAdresse(adresseLivraison);
                livraison.setTelephone(telephoneLivraison);
                livraison.setDatelivraisonprevue(LocalDateTime.now().plusDays(3));
                livraison.setStatutlivraison("EN ATTENTE VALIDATION");

                livraisonDao.save(livraison);
                System.out.println("  📦 Livraison créée");
            }

            System.out.println("✅ Commande créée : " + commande.getCodeCommande());
            return commande;

        } catch (Exception e) {
            System.err.println("❌ Erreur création commande : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Impossible de créer la commande", e);
        }
    }

    @Override
    public void validerPaiement(Commande commande, String referencePaiement) {
        commande.setReferencepaiement(referencePaiement);
        commande.setStatut("PAYEE");
        commandeDao.update(commande);
        System.out.println("✅ Paiement validé : " + commande.getCodeCommande());
    }

    @Override
    public void changerStatut(Commande commande, String nouveauStatut) {
        commande.setStatut(nouveauStatut);
        commandeDao.update(commande);
    }

    @Override
    public BigDecimal calculerTotal(Map<Article, Integer> panier) {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Article, Integer> entry : panier.entrySet()) {
            BigDecimal prixLigne = entry.getKey().getPrixUnitaire()
                .multiply(new BigDecimal(entry.getValue()));
            total = total.add(prixLigne);
        }
        return total;
    }
}