// -------------------------------------------------------------
// src/main/java/entity/Produit.java
// -------------------------------------------------------------
package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "produit",
       indexes = {@Index(name = "idx_produit_nom", columnList = "nom")})
public class Produit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "idBoutique", foreignKey = @ForeignKey(name = "fk_produit_boutique"))
    private Boutique boutique;

    @ManyToOne(optional = true)
    @JoinColumn(name = "idCatalogue", foreignKey = @ForeignKey(name = "fk_produit_catalogue"))
    private Catalogue catalogue;

    private Integer quantite = 0;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(name = "prixUnitaire", precision = 10, scale = 2, nullable = false)
    private BigDecimal prixUnitaire;

    @Lob
    private String description;

    public Produit() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Boutique getBoutique() { return boutique; }
    public void setBoutique(Boutique boutique) { this.boutique = boutique; }

    public Catalogue getCatalogue() { return catalogue; }
    public void setCatalogue(Catalogue catalogue) { this.catalogue = catalogue; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
