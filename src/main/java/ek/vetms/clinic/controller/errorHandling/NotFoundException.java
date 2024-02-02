package ek.vetms.clinic.controller.errorHandling;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
