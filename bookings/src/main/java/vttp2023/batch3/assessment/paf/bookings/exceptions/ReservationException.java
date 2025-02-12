package vttp2023.batch3.assessment.paf.bookings.exceptions;

public class ReservationException extends RuntimeException{
    public ReservationException(){
    }
    public ReservationException(String message){
        super(message);
    }
    public ReservationException(String message, Throwable throwable){
        super(message, throwable);
    }
}
