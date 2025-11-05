package dao;

import entity.Personne;
import java.util.List;

public interface UtilisateurDao {
    List<Personne> findAll();
    List<Personne> findByProfil(Long profilId);
}