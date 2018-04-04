package nl.svendubbeld.fontys.dto;

import nl.svendubbeld.fontys.model.security.SecurityGroup;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityGroupDTO implements ToEntityConvertible<SecurityGroup> {

    private long id;

    private String name;

    private Set<PermissionDTO> permissions;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionDTO> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecurityGroupDTO that = (SecurityGroupDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public SecurityGroup convert(DTOHelper dtoHelper) {
        SecurityGroup securityGroup = new SecurityGroup();

        securityGroup.setId(getId());
        securityGroup.setName(getName());
        securityGroup.setPermissions(getPermissions().stream().map(permissionDTO -> permissionDTO.convert(dtoHelper)).collect(Collectors.toSet()));

        return securityGroup;
    }
}
