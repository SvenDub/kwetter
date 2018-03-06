package nl.svendubbeld.fontys.dao;

import nl.svendubbeld.fontys.model.User;

/**
 * A repository for users.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Find a user by their email address.
     *
     * @param email The user's email address.
     * @return The user.
     */
    User findByEmail(String email);
}
