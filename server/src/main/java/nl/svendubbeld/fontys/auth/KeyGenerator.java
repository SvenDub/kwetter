package nl.svendubbeld.fontys.auth;

import java.security.Key;

@FunctionalInterface
public interface KeyGenerator {

    Key generateKey();
}
