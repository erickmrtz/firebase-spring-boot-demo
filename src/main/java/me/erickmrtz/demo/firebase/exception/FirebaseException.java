package me.erickmrtz.demo.firebase.exception;

public class FirebaseException extends RuntimeException{
    public FirebaseException() {
    }

    public FirebaseException(String message) {
        super(message);
    }

    public FirebaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FirebaseException(Throwable cause) {
        super(cause);
    }
}
