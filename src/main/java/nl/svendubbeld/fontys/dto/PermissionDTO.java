package nl.svendubbeld.fontys.dto;

import nl.svendubbeld.fontys.model.security.Permission;

public class PermissionDTO implements ToEntityConvertible<Permission> {

    private long id;

    private String key;

    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Permission convert(DTOHelper dtoHelper) {
        Permission permission = new Permission();

        permission.setId(getId());
        permission.setKey(getKey());
        permission.setDescription(getDescription());

        return permission;
    }
}
