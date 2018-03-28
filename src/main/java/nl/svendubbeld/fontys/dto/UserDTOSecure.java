package nl.svendubbeld.fontys.dto;

import nl.svendubbeld.fontys.model.Profile;
import nl.svendubbeld.fontys.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserDTOSecure extends UserDTO {

    private String email;

    private Set<SecurityGroupDTO> securityGroups;

    private Set<ProfileDTO> following;

    private Set<ProfileDTO> profiles;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<SecurityGroupDTO> getSecurityGroups() {
        return securityGroups;
    }

    public void setSecurityGroups(Set<SecurityGroupDTO> securityGroups) {
        this.securityGroups = securityGroups;
    }

    public Set<ProfileDTO> getFollowing() {
        return following;
    }

    public void setFollowing(Set<ProfileDTO> following) {
        this.following = following;
    }

    public Set<ProfileDTO> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<ProfileDTO> profiles) {
        this.profiles = profiles;
    }

    @Override
    public User convert(DTOHelper dtoHelper) {
        User user = super.convert(dtoHelper);

        user.setEmail(getEmail());
        user.setSecurityGroups(getSecurityGroups().stream().map(securityGroupDTO -> securityGroupDTO.convert(dtoHelper)).collect(Collectors.toSet()));
        user.setFollowing(getFollowing().stream()
                .map(profileDTO -> profileDTO.convert(dtoHelper))
                .map(Profile::getId)
                .map(aLong -> dtoHelper.getProfileService().findById(aLong))
                .map(Profile::getUser)
                .collect(Collectors.toSet()));
        user.setProfiles(getProfiles().stream().map(profileDTO -> profileDTO.convert(dtoHelper)).collect(Collectors.toSet()));

        return user;
    }
}
