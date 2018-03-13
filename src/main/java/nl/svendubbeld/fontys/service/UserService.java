package nl.svendubbeld.fontys.service;

import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.model.User;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UserService {

    @Inject
    private UserRepository userRepository;

    public User addUser(User user) {
        userRepository.create(user);

        return user;
    }

    public boolean exists(String username) {
        return userRepository.exists(username);
    }

    public void clear() {
        userRepository.clear();
    }

    public long getTweetsCount(User user) {
        return userRepository.getTweetsCount(user);
    }

    public long getFollowersCount(User user) {
        return userRepository.getFollowersCount(user);
    }

    public long getFollowingCount(User user) {
        return userRepository.getFollowingCount(user);
    }

    public User findById(long id) {
        return userRepository.findById(id);
    }
}
