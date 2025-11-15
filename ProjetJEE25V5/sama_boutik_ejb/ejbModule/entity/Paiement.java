package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiement",
       indexes = {
           @Index(name = "idx_paiement_commande", columnList = "idCommande"),
           @Index(name = "idx_paiement_etat", columnList = "etat")
       })
public class Paiement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idCommande;

    @Column(nullable = false, length = 50)
    private String modePaiement;

    @Column(precision = 10, scale = 2)
    private BigDecimal montant;

    private LocalDateTime datePaiement;
    
    @Column(length = 50)
    private String etat = "En attente";

    public Paiement() {}

    @PrePersist
    public void prePersist() {
        if (datePaiement == null) datePaiement = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdCommande() { return idCommande; }
    public void setIdCommande(Long idCommande) { this.idCommande = idCommande; }

    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }

    public LocalDateTime getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDateTime datePaiement) { this.datePaiement = datePaiement; }

    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }
}
