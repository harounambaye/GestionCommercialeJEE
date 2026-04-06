package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "boutique",
       indexes = {@Index(name = "idx_boutique_libelle", columnList = "libelle")})
public class Boutique implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String libelle;

    @Column(length = 255)
    private String adresse;

    @Column(length = 50)
    private String telephone;

    // 🔥 Ajoute cette relation pour corriger le mappedBy
    /*
    @ManyToOne
    @JoinColumn(name = "entite_id", foreignKey = @ForeignKey(name = "fk_boutique_entite"))
    private Entite entite;*/

    @ManyToOne
    @JoinColumn(name = "idPersonne", foreignKey = @ForeignKey(name = "fk_boutique_personne"))
    private Personne proprietaire;

    private LocalDate dateOuverture;

    @Column(name = "statut")
    private Boolean statut = Boolean.TRUE;

    public Boutique() {}

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public Personne getProprietaire() { return proprietaire; }
    public void setProprietaire(Personne proprietaire) { this.proprietaire = proprietaire; }

    /*public Entite getEntite() { return entite; }
    public void setEntite(Entite entite) { this.entite = entite; }*/

    public LocalDate getDateOuverture() { return dateOuverture; }
    public void setDateOuverture(LocalDate dateOuverture) { this.dateOuverture = dateOuverture; }

    public Boolean getStatut() { return statut; }
    public void setStatut(Boolean statut) { this.statut = statut; }
}
