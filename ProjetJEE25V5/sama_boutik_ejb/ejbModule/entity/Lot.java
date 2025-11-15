package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lot",
       indexes = @Index(name = "idx_lot_codelot", columnList = "codeLot"))
public class Lot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "idStock", foreignKey = @ForeignKey(name = "fk_lot_stock"))
    private Stock stock;

    @Column(nullable = false, unique = true, length = 100)
    private String codeLot;

    @Column(nullable = false, length = 150)
    private String libelle;

    @Column(length = 255)
    private String description;

    private LocalDate dateReception;
    private LocalDate datePeremption;

    // ⚡ FetchType.EAGER pour éviter LazyInitializationException dans OpenJPA
    @OneToMany(mappedBy = "lot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Article> articles = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Lot() {
        this.articles = new ArrayList<>();
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (dateReception == null) dateReception = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 🔹 Méthodes utilitaires pour gérer les articles
    public void ajouterArticle(Article article) {
        if (!articles.contains(article)) {
            articles.add(article);
            article.setLot(this);
        }
    }

    public void retirerArticle(Article article) {
        if (articles.contains(article)) {
            articles.remove(article);
            article.setLot(null);
        }
    }

    public List<Article> getArticles() {
        if (articles == null) articles = new ArrayList<>();
        return articles;
    }

    public Integer getQuantiteInitiale() {
        return getArticles().stream().mapToInt(a -> a.getQuantiteInitiale() != null ? a.getQuantiteInitiale() : 0).sum();
    }

    public Integer getQuantiteRestante() {
        return getArticles().stream().mapToInt(a -> a.getQuantiteRestante() != null ? a.getQuantiteRestante() : 0).sum();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Stock getStock() { return stock; }
    public void setStock(Stock stock) { this.stock = stock; }

    public String getCodeLot() { return codeLot; }
    public void setCodeLot(String codeLot) { this.codeLot = codeLot; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateReception() { return dateReception; }
    public void setDateReception(LocalDate dateReception) { this.dateReception = dateReception; }

    public LocalDate getDatePeremption() { return datePeremption; }
    public void setDatePeremption(LocalDate datePeremption) { this.datePeremption = datePeremption; }

    public void setArticles(List<Article> articles) { this.articles = articles; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
