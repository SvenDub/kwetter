package nl.svendubbeld.fontys.dto;

import nl.svendubbeld.fontys.model.security.Token;

public class TokenDTO implements ToEntityConvertible<Token> {

    private long id;

    private String uuid;

    private boolean revoked;

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

    @Override
    public Token convert(DTOHelper dtoHelper) {
        Token token = new Token();

        token.setId(id);
        token.setUuid(uuid);
        token.setRevoked(revoked);

        return token;
    }
}
