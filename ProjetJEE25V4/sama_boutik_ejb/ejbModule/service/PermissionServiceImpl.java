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

    @Override
    public void ajouterPermission(Permission permission) {
        permissionDao.save(permission);
    }

    @Override
    public void supprimerPermission(Long id) {
        Permission p = permissionDao.findById(id);
        if (p != null) {
            if (permissionDao.estUtiliseeDansRole(p)) {
                throw new IllegalStateException("Cette permission est encore utilisée dans un rôle.");
            }
            permissionDao.delete(id);
        }
    }

}
