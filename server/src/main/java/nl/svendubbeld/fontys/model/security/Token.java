package nl.svendubbeld.fontys.model.security;

import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.ToDTOConvertible;
import nl.svendubbeld.fontys.dto.TokenDTO;
import nl.svendubbeld.fontys.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class Token implements ToDTOConvertible<TokenDTO> {

    /**
     * A unique id identifying this token.
     */
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    @NotNull
    private String uuid;

    @ManyToOne
    @NotNull
    private User user;

    @NotNull
    private boolean revoked;

    @PastOrPresent
    @NotNull
    private OffsetDateTime lastUsed;

    public Token() {
    }

    public Token(@NotNull String uuid, @NotNull User user, @NotNull boolean revoked) {
        this.uuid = uuid;
        this.user = user;
        this.revoked = revoked;
        this.lastUsed = OffsetDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public boolean isValid() {
        return !isRevoked();
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public OffsetDateTime getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(OffsetDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(uuid, token.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public TokenDTO convert(DTOHelper dtoHelper) {
        TokenDTO dto = new TokenDTO();

        dto.setId(id);
        dto.setUuid(uuid);
        dto.setRevoked(revoked);
        dto.setLastUsed(lastUsed);

        return dto;
    }
}
