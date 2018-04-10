package nl.svendubbeld.fontys.service.gravatar;

import java.util.Objects;

public class GravatarRequest {

    private String email;
    private int size;

    public GravatarRequest(String email, int size) {
        this.email = email;
        this.size = size;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GravatarRequest that = (GravatarRequest) o;
        return size == that.size && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(email, size);
    }
}
