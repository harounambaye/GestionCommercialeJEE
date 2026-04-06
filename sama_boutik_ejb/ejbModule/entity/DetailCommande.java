package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "detailcommande")
public class DetailCommande implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idcommande", nullable = false)
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "idarticle", nullable = false)
    private Article article;

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prixunitaire;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalligne;

    private LocalDateTime createdat;
    private LocalDateTime updatedat;

    public DetailCommande() {}

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdat == null) createdat = now;
        if (updatedat == null) updatedat = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedat = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Commande getCommande() { return commande; }
    public void setCommande(Commande commande) { this.commande = commande; }

    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public BigDecimal getPrixunitaire() { return prixunitaire; }
    public void setPrixunitaire(BigDecimal prixunitaire) { this.prixunitaire = prixunitaire; }

    public BigDecimal getTotalligne() { return totalligne; }
    public void setTotalligne(BigDecimal totalligne) { this.totalligne = totalligne; }

    public LocalDateTime getCreatedat() { return createdat; }
    public void setCreatedat(LocalDateTime createdat) { this.createdat = createdat; }

    public LocalDateTime getUpdatedat() { return updatedat; }
    public void setUpdatedat(LocalDateTime updatedat) { this.updatedat = updatedat; }
}
