package groupquattro.demo.exceptions;

public class UserAlreadyAMemberException extends Exception {
    public UserAlreadyAMemberException(){
        super("user is already a member of this group");
    }
}
