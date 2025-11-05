package dao;

import entity.ActiviteCommerciale;
import java.util.List;

public interface ActiviteDao {
    void save(ActiviteCommerciale a);
    void update(ActiviteCommerciale a);
    void delete(Long id);
    List<ActiviteCommerciale> findAll();
    ActiviteCommerciale findById(Long id);
    long countEntitesLiees(Long activiteId);

}
