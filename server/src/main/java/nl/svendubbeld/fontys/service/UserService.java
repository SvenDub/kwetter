package nl.svendubbeld.fontys.service;

import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.UserDTOSecure;
import nl.svendubbeld.fontys.exception.EncryptionException;
import nl.svendubbeld.fontys.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private DTOHelper dtoHelper;

    public User addUser(User user) {
        try {
            user.setPassword(encodeSHA256(user.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException("Can't encrypt password", e);
        }

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

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Stream<User> findAll() {
        return userRepository.findAll();
    }

    public List<UserDTOSecure> findAllAsDTOSecure() {
        return findAll().map(dtoHelper::convertToDTOSecure).collect(Collectors.toList());
    }

    public User edit(User user) {
        return userRepository.edit(user);
    }

    public UserDTOSecure editSecurityGroups(UserDTOSecure dto) {
        User convertedUser = dtoHelper.convertToEntity(dto);

        User user = findById(convertedUser.getId());
        user.setSecurityGroups(convertedUser.getSecurityGroups());


        return edit(user).convertSecure(dtoHelper);
    }

    public static String encodeSHA256(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public Stream<User> findFollowers(User user) {
        return userRepository.findFollowers(user);
    }
}
