package groupquattro.demo.exceptions;

import javax.management.RuntimeErrorException;

public class ParsingExpenceException extends Exception {
    ParsingExpenceException(String s) {
        super(s);
    }
    ParsingExpenceException(){}
}
