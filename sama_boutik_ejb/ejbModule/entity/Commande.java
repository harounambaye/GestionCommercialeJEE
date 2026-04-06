package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "commande",
       indexes = {
           @Index(name = "idx_commande_personne", columnList = "idPersonne"),
           @Index(name = "idx_commande_code", columnList = "codeCommande")
       })
public class Commande implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idPersonne;

    @Column(nullable = false, unique = true, length = 100)
    private String codeCommande;

    @Column(length = 30, nullable = false)
    private String typecommande;  // AVEC_LIVRAISON / SANS_LIVRAISON

    @Column(length = 150)
    private String referencepaiement;  // référence transaction (ex: Wave)

    private LocalDateTime dateCommande;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @Column(length = 50)
    private String statut = "En attente";

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Commande() {}

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (dateCommande == null) dateCommande = now;
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;

        if (typecommande == null) typecommande = "SANS_LIVRAISON";
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ============================
    //       GETTERS / SETTERS
    // ============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdPersonne() { return idPersonne; }
    public void setIdPersonne(Long idPersonne) { this.idPersonne = idPersonne; }

    public String getCodeCommande() { return codeCommande; }
    public void setCodeCommande(String codeCommande) { this.codeCommande = codeCommande; }

    public String getTypecommande() { return typecommande; }
    public void setTypecommande(String typecommande) { this.typecommande = typecommande; }

    public String getReferencepaiement() { return referencepaiement; }
    public void setReferencepaiement(String referencepaiement) { this.referencepaiement = referencepaiement; }

    public LocalDateTime getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDateTime dateCommande) { this.dateCommande = dateCommande; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
