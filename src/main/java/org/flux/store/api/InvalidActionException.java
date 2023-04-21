package org.flux.store.api;

public class InvalidActionException extends Exception{

    public InvalidActionException(String message) {super(message);}

    public InvalidActionException(Throwable ex) {super(ex);}

    public InvalidActionException(String message, Throwable ex) {super(message, ex);}
}
