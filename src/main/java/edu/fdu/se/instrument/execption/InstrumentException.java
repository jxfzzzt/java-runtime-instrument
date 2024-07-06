package edu.fdu.se.instrument.execption;


public class InstrumentException extends RuntimeException {
    public InstrumentException() {
        super();
    }

    public InstrumentException(String message) {
        super(message);
    }

    public InstrumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
