// =============================================================
// Sama_Boutik – JPA Entities (Jakarta EE 10, Hibernate 5.6)
// Entity: Employe
// =============================================================

package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employe",
       indexes = {@Index(name = "idx_employe_salairemois", columnList = "salairemois")})
public class Employe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idemploye;

    // Référence à la personne
    @OneToOne
    @JoinColumn(name = "idpersonne", nullable = false)
    private Personne personne;

    // Référence à l'entité commerciale
    @ManyToOne
    @JoinColumn(name = "identitecommerciale", nullable = false)
    private EntiteCommerciale entiteCommerciale;

    // Informations salariales
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal montantsalaire;

    @Column(length = 20)
    private String salairemois;

    @Column(length = 3)
    private String paye; // "oui" ou "non"

    // Contrat
    @Column(length = 20, nullable = false)
    private String typecontrat; // CDI, CDD, Stage, Prestation

    private LocalDate datedebut;
    private LocalDate datefin;

    // Tracking
    private LocalDateTime createdate;
    private LocalDateTime updatedate;

    // Actif ou non
    @Column(nullable = false)
    private boolean actif = true;

    public Employe() {}

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdate == null) createdate = now;
        if (updatedate == null) updatedate = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedate = LocalDateTime.now();
    }

    // Getters & Setters

    public Long getIdemploye() { return idemploye; }
    public void setIdemploye(Long idemploye) { this.idemploye = idemploye; }

    public Personne getPersonne() { return personne; }
    public void setPersonne(Personne personne) { this.personne = personne; }

    public EntiteCommerciale getEntiteCommerciale() { return entiteCommerciale; }
    public void setEntiteCommerciale(EntiteCommerciale entiteCommerciale) { this.entiteCommerciale = entiteCommerciale; }

    public BigDecimal getMontantsalaire() { return montantsalaire; }
    public void setMontantsalaire(BigDecimal montantsalaire) { this.montantsalaire = montantsalaire; }

    public String getSalairemois() { return salairemois; }
    public void setSalairemois(String salairemois) { this.salairemois = salairemois; }

    public String getPaye() { return paye; }
    public void setPaye(String paye) { this.paye = paye; }

    public String getTypecontrat() { return typecontrat; }
    public void setTypecontrat(String typecontrat) { this.typecontrat = typecontrat; }

    public LocalDate getDatedebut() { return datedebut; }
    public void setDatedebut(LocalDate datedebut) { this.datedebut = datedebut; }

    public LocalDate getDatefin() { return datefin; }
    public void setDatefin(LocalDate datefin) { this.datefin = datefin; }

    public LocalDateTime getCreatedate() { return createdate; }
    public void setCreatedate(LocalDateTime createdate) { this.createdate = createdate; }

    public LocalDateTime getUpdatedate() { return updatedate; }
    public void setUpdatedate(LocalDateTime updatedate) { this.updatedate = updatedate; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
}
