package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "article",
       indexes = {
           @Index(name = "idx_article_idlot", columnList = "idLot"),
           @Index(name = "idx_article_idproduit", columnList = "idProduit")
       })
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idLot", foreignKey = @ForeignKey(name = "fk_article_lot"))
    private Lot lot;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idProduit", foreignKey = @ForeignKey(name = "fk_article_produit"))
    private Produit produit;

    @Column(nullable = false)
    private Integer quantiteInitiale;

    @Column(nullable = false)
    private Integer quantiteRestante;

    private BigDecimal prixUnitaire;

    @Column(length = 255)
    private String description;

    private LocalDate datePeremption;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Lot getLot() { return lot; }
    public void setLot(Lot lot) { this.lot = lot; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public Integer getQuantiteInitiale() { return quantiteInitiale; }
    public void setQuantiteInitiale(Integer quantiteInitiale) { this.quantiteInitiale = quantiteInitiale; }

    public Integer getQuantiteRestante() { return quantiteRestante; }
    public void setQuantiteRestante(Integer quantiteRestante) { this.quantiteRestante = quantiteRestante; }

    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDatePeremption() { return datePeremption; }
    public void setDatePeremption(LocalDate datePeremption) { this.datePeremption = datePeremption; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
