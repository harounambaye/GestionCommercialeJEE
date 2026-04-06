package service;

import entity.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public interface CommandeService {
    Commande creerCommande(
        Personne client,
        Map<Article, Integer> panier,
        String typeCommande,
        String adresseLivraison,
        String telephoneLivraison,
        String referencePaiement
    );
    
    void validerPaiement(Commande commande, String referencePaiement);
    void changerStatut(Commande commande, String nouveauStatut);
    BigDecimal calculerTotal(Map<Article, Integer> panier);
}