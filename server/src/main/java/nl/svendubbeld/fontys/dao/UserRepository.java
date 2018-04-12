package nl.svendubbeld.fontys.dao;

import nl.svendubbeld.fontys.model.User;

import java.util.stream.Stream;

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

    /**
     * Find a user by their current username.
     *
     * @param username The user's username.
     * @return The user.
     */
    User findByUsername(String username);

    /**
     * Get the amount of tweets placed by a user.
     *
     * @param user The user.
     * @return The amount of tweets placed by the user.
     */
    long getTweetsCount(User user);

    /**
     * Gets the amount of users following a user.
     *
     * @param user The user.
     * @return The amount of users following the user.
     */
    long getFollowersCount(User user);

    /**
     * Gets the amount of users followed by a user.
     *
     * @param user The user.
     * @return The amount of users followed by the user.
     */
    long getFollowingCount(User user);

    /**
     * Checks if a user with a given username exists.
     *
     * @param username The username.
     * @return Whether the user exists.
     */
    boolean exists(String username);

    /**
     * Gets all the users who follow a user.
     *
     * @param user The user.
     * @return The users following the user.
     */
    Stream<User> findFollowers(User user);
}
