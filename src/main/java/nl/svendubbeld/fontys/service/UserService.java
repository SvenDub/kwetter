package nl.svendubbeld.fontys.service;

import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.UserDTOSecure;
import nl.svendubbeld.fontys.model.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class UserService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private DTOHelper dtoHelper;

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

    public Stream<User> findAll() {
        return userRepository.findAll();
    }

    public List<UserDTOSecure> findAllAsDTOSecure() {
        return findAll().map(user -> user.convertSecure(dtoHelper)).collect(Collectors.toList());
    }

    public User edit(User user) {
        return userRepository.edit(user);
    }

    public UserDTOSecure editSecurityGroups(UserDTOSecure dto) {
        User convertedUser = dto.convert(dtoHelper);

        User user = findById(convertedUser.getId());
        user.setSecurityGroups(convertedUser.getSecurityGroups());


        return edit(user).convertSecure(dtoHelper);
    }
}
