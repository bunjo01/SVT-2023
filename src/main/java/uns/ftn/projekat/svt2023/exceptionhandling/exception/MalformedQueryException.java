package uns.ftn.projekat.svt2023.exceptionhandling.exception;

public class MalformedQueryException extends RuntimeException {

    public MalformedQueryException(String message) {
        super(message);
    }
}
