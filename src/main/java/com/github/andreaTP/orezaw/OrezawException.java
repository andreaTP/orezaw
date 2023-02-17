package com.github.andreaTP.orezaw;

public class OrezawException extends RuntimeException {

    OrezawException(String message) {
        super(message);
    }

    OrezawException(String message, Exception cause) {
        super(message, cause);
    }

}
