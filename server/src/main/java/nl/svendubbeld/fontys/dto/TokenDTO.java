package nl.svendubbeld.fontys.dto;

import nl.svendubbeld.fontys.model.security.Token;

import java.time.OffsetDateTime;

public class TokenDTO implements ToEntityConvertible<Token> {

    private long id;

    private String uuid;

    private boolean revoked;

    private OffsetDateTime lastUsed;

    public TokenDTO() {
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

    public boolean isRevoked() {
        return revoked;
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
    public Token convert(DTOHelper dtoHelper) {
        Token token = new Token();

        token.setId(id);
        token.setUuid(uuid);
        token.setRevoked(revoked);
        token.setLastUsed(lastUsed);

        return token;
    }
}
