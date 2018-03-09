package nl.svendubbeld.fontys.test.embedded.cucumber;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class World {

    private String username;
    private String token;
private String path;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
