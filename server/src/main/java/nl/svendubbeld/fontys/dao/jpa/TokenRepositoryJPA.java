package nl.svendubbeld.fontys.dao.jpa;

import nl.svendubbeld.fontys.dao.TokenRepository;
import nl.svendubbeld.fontys.model.security.Token;

public class TokenRepositoryJPA extends JPARepository<Token, Long> implements TokenRepository {

    protected TokenRepositoryJPA() {
        super(Token.class);
    }
}
