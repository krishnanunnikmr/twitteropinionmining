package twitter.opinion.mining.domain.service;


public class UserIdAlreadyExistsException extends RuntimeException{
    public UserIdAlreadyExistsException(String message) {
        super(message);
    }
}
