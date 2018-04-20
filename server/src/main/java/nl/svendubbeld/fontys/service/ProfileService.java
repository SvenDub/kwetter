package nl.svendubbeld.fontys.service;

import nl.svendubbeld.fontys.dao.ProfileRepository;
import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.LocationDTO;
import nl.svendubbeld.fontys.exception.UserExistsException;
import nl.svendubbeld.fontys.model.Location;
import nl.svendubbeld.fontys.model.Profile;
import nl.svendubbeld.fontys.model.User;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class ProfileService {

    @Inject
    private ProfileRepository profileRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private DTOHelper dtoHelper;

    public Profile addProfile(User user, String username, String name, String bio, Location location, String website) throws UserExistsException {
        if (userRepository.exists(username) && userRepository.findByUsername(username) != user) {
            throw new UserExistsException("User '" + username + "' already exists");
        }

        Profile newProfile = user.createProfile(username, name, bio, location, website);

        profileRepository.create(newProfile);

        return newProfile;
    }

    public Profile addProfile(User user, String username, String name, String bio, LocationDTO location, String website) throws UserExistsException {
        return addProfile(user, username, name, bio, dtoHelper.convertToEntity(location), website);
    }

    public void clear() {
        profileRepository.clear();
    }

    public Profile findById(long id) {
        return profileRepository.findById(id);
    }

    public void remove(Profile profile) {
        profileRepository.remove(profile);
    }
}
