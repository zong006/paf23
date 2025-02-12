package vttp2023.batch3.assessment.paf.bookings.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReservationException.class)
    public String handleReservationException(ReservationException ex, Model model){
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }
}
