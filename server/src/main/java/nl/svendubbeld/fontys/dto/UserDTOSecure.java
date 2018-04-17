package nl.svendubbeld.fontys.dto;

import nl.svendubbeld.fontys.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserDTOSecure extends UserDTO {

    private String email;

    private Set<SecurityGroupDTO> securityGroups;

    private Set<UserDTO> following;

    private Set<ProfileDTO> profiles;

    private Set<TokenDTO> tokens;

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

    public Set<UserDTO> getFollowing() {
        return following;
    }

    public void setFollowing(Set<UserDTO> following) {
        this.following = following;
    }

    public Set<ProfileDTO> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<ProfileDTO> profiles) {
        this.profiles = profiles;
    }

    public Set<TokenDTO> getTokens() {
        return tokens;
    }

    public void setTokens(Set<TokenDTO> tokens) {
        this.tokens = tokens;
    }

    @Override
    public User convert(DTOHelper dtoHelper) {
        User user = super.convert(dtoHelper);

        user.setEmail(getEmail());
        user.setSecurityGroups(getSecurityGroups().stream().map(dtoHelper::convertToEntity).collect(Collectors.toSet()));
        user.setFollowing(getFollowing().stream()
                .map(dtoHelper::convertToEntity)
                .map(User::getId)
                .map(dtoHelper.getUserService()::findById)
                .collect(Collectors.toSet()));
        user.setProfiles(getProfiles().stream().map(dtoHelper::convertToEntity).collect(Collectors.toSet()));
        user.setTokens(getTokens().stream().map(dtoHelper::convertToEntity).collect(Collectors.toSet()));

        return user;
    }
}
