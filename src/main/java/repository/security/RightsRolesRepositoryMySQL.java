package repository.security;

import model.Right;
import model.Role;
import model.User;

import java.util.List;

public class RightRolesRepositoryMySQL implements RightsRolesRepository{
    @Override
    public void addRole(String role) {
        
    }

    @Override
    public void addRight(String right) {

    }

    @Override
    public Role findRoleByTitle(String role) {
        return null;
    }

    @Override
    public Role findRoleById(Long roleId) {
        return null;
    }

    @Override
    public Right findRightByTitle(String right) {
        return null;
    }

    @Override
    public void addRolesToUser(User user, List<Role> roles) {

    }

    @Override
    public List<Role> findRolesForUser(Long userId) {
        return List.of();
    }

    @Override
    public void addRoleRight(Long roleId, Long rightId) {

    }
}
