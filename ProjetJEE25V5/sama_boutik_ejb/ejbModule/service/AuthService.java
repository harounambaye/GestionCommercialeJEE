package service;

import entity.Personne;
import entity.Profil;
import jakarta.ejb.Local;

@Local
public interface AuthService {
    /**
     * Authentifie un utilisateur avec email et mot de passe
     * @return Personne si succès, null sinon
     */
    Personne authentifier(String email, String motDePasse);
    
    /**
     * Récupère le profil principal de l'utilisateur
     */
    Profil getProfilPrincipal(Personne personne);
    
    /**
     * Vérifie si l'utilisateur est actif
     */
    boolean isActif(Personne personne);
}