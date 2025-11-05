// -------------------------------------------------------------
// src/main/java/entity/DetailProduit.java
// -------------------------------------------------------------
package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "detailproduit",
       indexes = {@Index(name = "idx_detailproduit_code", columnList = "codeProduit")})
public class DetailProduit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "idProduit", unique = true, foreignKey = @ForeignKey(name = "fk_detail_produit"))
    private Produit produit;

    @Column(name = "codeProduit", length = 100, nullable = false, unique = true)
    private String codeProduit;

    @Column(length = 150)
    private String libelle;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    public DetailProduit() {}

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() { updatedAt = LocalDateTime.now(); }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public String getCodeProduit() { return codeProduit; }
    public void setCodeProduit(String codeProduit) { this.codeProduit = codeProduit; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}