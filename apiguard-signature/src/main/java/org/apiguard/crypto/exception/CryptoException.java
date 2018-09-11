package org.apiguard.crypto.exception;

public class CryptoException extends Exception {
    public CryptoException(Throwable e) {
        super(e);
    }

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(String message, Throwable e) {
        super(message, e);
    }
}
