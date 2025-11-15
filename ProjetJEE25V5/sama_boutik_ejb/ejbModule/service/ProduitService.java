// =====================================================================
// Sama_Boutik – Services @Local & Impl @Stateless (Jakarta EE 10)
// Pattern demandé :
// interface @Local avec méthodes minimalistes (creerX, listerX)
// impl @Stateless, @EJB vers le DAO correspondant, @TransactionAttribute(REQUIRED) sur la création
// =====================================================================


// ---------------------------------------------------------------------
// src/main/java/service/ProduitService.java
// ---------------------------------------------------------------------
package service;


import entity.Produit;
import jakarta.ejb.Local;
import java.util.List;


@Local
public interface ProduitService {
void creerProduit(Produit produit);
List<Produit> listerProduits();
}