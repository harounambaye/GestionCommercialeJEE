package service;

import dao.PermissionDao;
import entity.Permission;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class PermissionServiceImpl implements PermissionService {

    @EJB
    private PermissionDao permissionDao;

    @Override
    public List<Permission> listerPermissions() {
        return permissionDao.findAll();
    }

    @Override
    public Permission trouverParId(Long id) {
        return permissionDao.findById(id);
    }

    @Override
    public void modifierPermission(Permission permission) {
        permissionDao.update(permission);
    }
}
