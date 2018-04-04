package nl.svendubbeld.fontys.exception;

public class EncryptionException extends RuntimeException {
    public EncryptionException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
