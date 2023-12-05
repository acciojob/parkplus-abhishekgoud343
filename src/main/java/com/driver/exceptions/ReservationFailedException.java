package com.driver.exceptions;

public class ReservationFailedException extends Exception {
    public ReservationFailedException() {
        super("Reservation cannot be made");
    }

    public ReservationFailedException(String message) {
        super(message);
    }
}