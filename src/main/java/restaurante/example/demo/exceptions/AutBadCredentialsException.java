package restaurante.example.demo.exceptions;

import org.springframework.security.authentication.BadCredentialsException;


public class AutBadCredentialsException extends BadCredentialsException {
    
    public AutBadCredentialsException(String message) {
        super(message);
    }
    
}
