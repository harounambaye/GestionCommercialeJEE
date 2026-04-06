package dao;

import entity.Livraison;
import entity.Commande;
import java.util.List;

public interface LivraisonDao {
    void save(Livraison livraison);
    Livraison findByCommande(Commande commande);
    List<Livraison> findAll();
    void update(Livraison livraison);
}