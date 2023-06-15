package groupquattro.demo.exceptions;

public class UserAlreadyAMemberException extends Exception {
    public UserAlreadyAMemberException(){
        super("this is user is already a member of this group");
    }
}
