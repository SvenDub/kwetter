package nl.svendubbeld.fontys.model.security;

import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.SecurityGroupDTO;
import nl.svendubbeld.fontys.dto.ToDTOConvertible;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A group with {@link Permission permissions}.
 */
@Entity
@SuppressWarnings("squid:S1710")
@NamedQueries({
        @NamedQuery(name = "securityGroup.findByName", query = "select s from SecurityGroup s where s.name = :name")
})
public class SecurityGroup implements ToDTOConvertible<SecurityGroupDTO> {

    /**
     * A unique id identifying the group.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * The name of the group.
     */
    @NotBlank
    private String name;

    /**
     * The permissions of the group.
     */
    @ManyToMany
    private Set<Permission> permissions;

    public SecurityGroup() {
    }

    public SecurityGroup(String name, Set<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    /**
     * @return A unique id identifying the group.
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The name of the group.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The permissions of the group.
     */
    public Set<Permission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * @return If the group has a permission set.
     */
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    @Override
    public SecurityGroupDTO convert(DTOHelper dtoHelper) {
        SecurityGroupDTO dto = new SecurityGroupDTO();

        dto.setId(getId());
        dto.setName(getName());
        dto.setPermissions(getPermissions().stream().map(permission -> permission.convert(dtoHelper)).collect(Collectors.toSet()));

        return dto;
    }
}
