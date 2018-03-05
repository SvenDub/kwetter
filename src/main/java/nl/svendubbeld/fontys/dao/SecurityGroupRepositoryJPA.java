package nl.svendubbeld.fontys.dao;

import nl.svendubbeld.fontys.model.security.SecurityGroup;

/**
 * A JPA repository for security groups.
 */
public class SecurityGroupRepositoryJPA extends JPARepository<SecurityGroup, Long> implements SecurityGroupRepository {

    protected SecurityGroupRepositoryJPA() {
        super(SecurityGroup.class);
    }
}
