package nl.svendubbeld.fontys.dao;

import nl.svendubbeld.fontys.model.Profile;

/**
 * A repository for profiles.
 */
public interface ProfileRepository extends CrudRepository<Profile, Long> {

    /**
     * Find a profile by its username.
     *
     * @param username The username of the profile.
     * @return The profile.
     */
    Profile findByUsername(String username);
}
