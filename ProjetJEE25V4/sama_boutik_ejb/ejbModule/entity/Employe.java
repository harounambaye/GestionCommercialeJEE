// =============================================================
// Entité Employe
// =============================================================
package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "employe",
       indexes = {
           @Index(name = "idx_employe_personne", columnList = "idPersonne"),
           @Index(name = "idx_employe_entitecommerciale", columnList = "idEntiteCommerciale"),
           @Index(name = "idx_employe_actif", columnList = "actif")
       })
public class Employe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idPersonne;

    @Column(nullable = false)
    private Long idEntiteCommerciale;

    @Column(nullable = false)
    private LocalDate dateEmbauche;

    @Column(precision = 10, scale = 2)
    private BigDecimal salaire;

    @Column(nullable = false)
    private Boolean actif = true;

    @Column(length = 20)
    private String jourSemaine;

    private LocalTime heureDebut;

    private LocalTime heureFin;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Employe() {}

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

    public Long getIdPersonne() { return idPersonne; }
    public void setIdPersonne(Long idPersonne) { this.idPersonne = idPersonne; }

    public Long getIdEntiteCommerciale() { return idEntiteCommerciale; }
    public void setIdEntiteCommerciale(Long idEntiteCommerciale) { this.idEntiteCommerciale = idEntiteCommerciale; }

    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public BigDecimal getSalaire() { return salaire; }
    public void setSalaire(BigDecimal salaire) { this.salaire = salaire; }

    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }

    public String getJourSemaine() { return jourSemaine; }
    public void setJourSemaine(String jourSemaine) { this.jourSemaine = jourSemaine; }

    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}