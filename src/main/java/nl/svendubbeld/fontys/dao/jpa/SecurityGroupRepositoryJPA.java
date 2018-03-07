package nl.svendubbeld.fontys.dao.jpa;

import nl.svendubbeld.fontys.dao.SecurityGroupRepository;
import nl.svendubbeld.fontys.model.security.SecurityGroup;

/**
 * A JPA repository for security groups.
 */
public class SecurityGroupRepositoryJPA extends JPARepository<SecurityGroup, Long> implements SecurityGroupRepository {

    protected SecurityGroupRepositoryJPA() {
        super(SecurityGroup.class);
    }
}