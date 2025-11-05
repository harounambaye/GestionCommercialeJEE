package dao;

import entity.Entite;
import java.util.List;

public interface EntiteDao {
    void save(Entite entite);
    List<Entite> findAll();
    Entite findById(Long id);
    void update(Entite entite);
    void delete(Long id);
}
