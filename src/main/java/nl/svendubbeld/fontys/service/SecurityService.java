package nl.svendubbeld.fontys.service;

import nl.svendubbeld.fontys.dao.PermissionRepository;
import nl.svendubbeld.fontys.dao.SecurityGroupRepository;
import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.SecurityGroupDTO;
import nl.svendubbeld.fontys.model.security.Permission;
import nl.svendubbeld.fontys.model.security.SecurityGroup;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager entityManager;

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

    public void createUserGroupsView() {
        entityManager.createNativeQuery("DROP VIEW IF EXISTS `v_User_SecurityGroup`").executeUpdate();
        entityManager.createNativeQuery("CREATE VIEW v_User_SecurityGroup AS\n" +
                "SELECT User_SecurityGroup.User_id AS User_id, User_SecurityGroup.securityGroups_id AS securityGroup_id, `U`.email AS email, `S`.name AS securityGroup_name, `P`.`permission_key` AS permission_key\n" +
                "FROM User_SecurityGroup\n" +
                "INNER JOIN `User` `U` ON `User_SecurityGroup`.`User_id` = `U`.`id`\n" +
                "INNER JOIN `SecurityGroup` `S` ON `User_SecurityGroup`.`securityGroups_id` = `S`.`id`\n" +
                "INNER JOIN `SecurityGroup_Permission` `S2` ON `S`.`id` = `S2`.`SecurityGroup_id`\n" +
                "INNER JOIN `Permission` `P` ON `S2`.`permissions_id` = `P`.`id`")
                .executeUpdate();
    }
}
