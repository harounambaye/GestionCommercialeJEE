package dao;

import entity.Permission;
import java.util.List;

public interface PermissionDao {
    List<Permission> findAll();
    Permission findById(Long id);
    void update(Permission permission);
}
