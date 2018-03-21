package nl.svendubbeld.fontys.service;

import nl.svendubbeld.fontys.dao.PermissionRepository;
import nl.svendubbeld.fontys.dao.SecurityGroupRepository;
import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.SecurityGroupDTO;
import nl.svendubbeld.fontys.model.security.Permission;
import nl.svendubbeld.fontys.model.security.SecurityGroup;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class SecurityService {

    @Inject
    private PermissionRepository permissionRepository;

    @Inject
    private SecurityGroupRepository securityGroupRepository;

    @Inject
    private DTOHelper dtoHelper;

    public Permission addPermission(Permission permission) {
        permissionRepository.create(permission);

        return permission;
    }


    public SecurityGroup addGroup(SecurityGroup securityGroup) {
        securityGroupRepository.create(securityGroup);

        return securityGroup;
    }

    public Stream<SecurityGroup> findAllGroups() {
        return securityGroupRepository.findAll();
    }

    public List<SecurityGroupDTO> findAllGroupsAsDTO() {
        return findAllGroups().map(securityGroup -> securityGroup.convert(dtoHelper)).collect(Collectors.toList());
    }

    public void clear() {
        securityGroupRepository.clear();
        permissionRepository.clear();
    }
}
