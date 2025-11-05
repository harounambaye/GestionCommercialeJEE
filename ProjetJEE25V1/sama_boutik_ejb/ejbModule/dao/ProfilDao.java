package dao;

import entity.Profil;
import java.util.List;

public interface ProfilDao {
    void save(Profil profil);
    List<Profil> findAll();
    Profil findById(Long id);
    void update(Profil profil);
    void delete(Long id);
}
