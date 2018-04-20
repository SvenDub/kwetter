package nl.svendubbeld.fontys.dao;

import nl.svendubbeld.fontys.model.security.SecurityGroup;

/**
 * A repository for security groups.
 */
public interface SecurityGroupRepository extends CrudRepository<SecurityGroup, Long> {

    SecurityGroup findByName(String name);
}
