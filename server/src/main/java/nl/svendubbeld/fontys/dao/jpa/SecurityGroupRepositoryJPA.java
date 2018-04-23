package nl.svendubbeld.fontys.dao.jpa;

import nl.svendubbeld.fontys.dao.SecurityGroupRepository;
import nl.svendubbeld.fontys.model.security.SecurityGroup;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * A JPA repository for security groups.
 */
public class SecurityGroupRepositoryJPA extends JPARepository<SecurityGroup, Long> implements SecurityGroupRepository {

    protected SecurityGroupRepositoryJPA() {
        super(SecurityGroup.class);
    }

    @Override
    public SecurityGroup findByName(String name) {
        TypedQuery<SecurityGroup> query = getEntityManager().createNamedQuery("securityGroup.findByName", getEntityClass());
        query.setParameter("name", name);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
