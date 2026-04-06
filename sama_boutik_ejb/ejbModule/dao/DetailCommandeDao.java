package dao;

import entity.DetailCommande;
import entity.Commande;
import java.util.List;

public interface DetailCommandeDao {
    void save(DetailCommande detail);
    List<DetailCommande> findByCommande(Commande commande);
    void update(DetailCommande detail);
    void delete(Long id);
}