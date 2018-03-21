package nl.svendubbeld.fontys.service;

import nl.svendubbeld.fontys.dao.ProfileRepository;
import nl.svendubbeld.fontys.model.Profile;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class ProfileService {

    @Inject
    private ProfileRepository profileRepository;

    public Profile addProfile(Profile profile) {
        profileRepository.create(profile);

        return profile;
    }

    public void clear() {
        profileRepository.clear();
    }

    public Profile findById(long id) {
        return profileRepository.findById(id);
    }
}
