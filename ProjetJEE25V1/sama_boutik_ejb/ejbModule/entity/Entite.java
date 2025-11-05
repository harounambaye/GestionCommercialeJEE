package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "entite")
public class Entite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(name = "email_contact", length = 150)
    private String emailContact;

    @Column(length = 50)
    private String telephone;

    @Column(columnDefinition = "TEXT")
    private String description;

    // 🔗 Relation avec ActiviteCommerciale
    @ManyToOne
    @JoinColumn(name = "activite_id")
    private ActiviteCommerciale activite;

    // 🔗 Relation avec Compte (administrateur)
    @ManyToOne
    @JoinColumn(name = "compte_admin_id")
    private Compte compteAdmin;

    // ==========================
    //   Constructeurs
    // ==========================

    public Entite() {}

    public Entite(String nom, String emailContact, String telephone, String description) {
        this.nom = nom;
        this.emailContact = emailContact;
        this.telephone = telephone;
        this.description = description;
    }

    // ==========================
    //   Getters / Setters
    // ==========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActiviteCommerciale getActivite() {
        return activite;
    }

    public void setActivite(ActiviteCommerciale activite) {
        this.activite = activite;
    }

    public Compte getCompteAdmin() {
        return compteAdmin;
    }

    public void setCompteAdmin(Compte compteAdmin) {
        this.compteAdmin = compteAdmin;
    }

    // ==========================
    //   Méthodes utilitaires
    // ==========================

    @Override
    public String toString() {
        return "Entite{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", emailContact='" + emailContact + '\'' +
                ", telephone='" + telephone + '\'' +
                ", activite=" + (activite != null ? activite.getLibelle() : "Aucune") +
                '}';
    }
}
