package nl.svendubbeld.fontys.service;

import nl.svendubbeld.fontys.dao.PermissionRepository;
import nl.svendubbeld.fontys.dao.SecurityGroupRepository;
import nl.svendubbeld.fontys.model.security.Permission;
import nl.svendubbeld.fontys.model.security.SecurityGroup;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class SecurityService {

    @Inject
    private PermissionRepository permissionRepository;

    @Inject
    private SecurityGroupRepository securityGroupRepository;

    public Permission addPermission(Permission permission) {
        permissionRepository.create(permission);

        return permission;
    }


    public SecurityGroup addGroup(SecurityGroup securityGroup) {
        securityGroupRepository.create(securityGroup);

        return securityGroup;
    }

    public void clear() {
        securityGroupRepository.clear();
        permissionRepository.clear();
    }
}
