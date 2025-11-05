// -------------------------------------------------------------
// src/main/java/controller/ProduitController.java
// Jakarta EE 10 (TomEE 10) – JSF 3 + EJB Service injection
// Bonnes pratiques : on N'instancie PAS manuellement l'EntityManager ici;
// on consomme le service EJB via @EJB (JTA gérée par le conteneur).
// -------------------------------------------------------------
package controller;

import entity.Produit;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import service.ProduitService;

@Named("produitController")
@RequestScoped
public class ProduitController implements Serializable {

    private Produit produit = new Produit();
    private List<Produit> produits; // lazy-load

    @EJB
    private ProduitService produitService;

    public String creerProduit() {
        try {
            produitService.creerProduit(produit);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Produit créé avec succès."));
            // reset & refresh
            produit = new Produit();
            produits = null; // force reload at next getter call
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
        }
        return null; // rester sur la même page
    }

    public List<Produit> getProduits() {
        if (produits == null) {
            produits = produitService.listerProduits();
        }
        return produits;
    }

    // Getters / Setters
    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public void setProduits(List<Produit> produits) { this.produits = produits; }
}
