package groupquattro.demo.exceptions;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(String usersListNotFound) {
        super(usersListNotFound);
    }
}
