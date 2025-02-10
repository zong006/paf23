package vttp2023.batch3.assessment.paf.bookings.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Booking {
    private String name;
    private String email;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bookingDate;
    
    private int duration;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getBookingDate() {
        return bookingDate;
    }
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    @Override
    public String toString() {
        return "Booking [name=" + name + ", email=" + email + ", bookingDate=" + bookingDate + ", duration=" + duration
                + "]";
    }

    
}
