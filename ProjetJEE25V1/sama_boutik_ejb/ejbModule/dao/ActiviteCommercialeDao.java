package dao;

import entity.ActiviteCommerciale;
import java.util.List;

public interface ActiviteCommercialeDao {
    void save(ActiviteCommerciale activite);
    ActiviteCommerciale findById(Long id);
    List<ActiviteCommerciale> findAll();
    
    ActiviteCommerciale update(ActiviteCommerciale activite);
    void delete(Long id);
}
