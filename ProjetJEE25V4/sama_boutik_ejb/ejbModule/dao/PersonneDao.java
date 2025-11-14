package dao;

import entity.Personne;
import java.util.List;

public interface PersonneDao {
    void save(Personne personne);
    List<Personne> findAll();
    Personne findByEmail(String email);
    void update(Personne personne);
    void delete(Long id);
    Personne findById(Long id);
}
