package groupquattro.demo.exceptions;

public class WrongExpenceTypeException extends Throwable {
    WrongExpenceTypeException(){
        super("Something wrong with this expence");
    }
}
