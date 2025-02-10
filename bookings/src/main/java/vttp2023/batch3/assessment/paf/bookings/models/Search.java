package vttp2023.batch3.assessment.paf.bookings.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Search {
    @NotNull(message = "Field cannot be empty")
    private String country;

    @Min(value = 1, message = "Number of guests between 1 and 10 (inclusive)")
    @Max(value = 10, message = "Number of guests between 1 and 10 (inclusive)")
    private int numPerson;


    @Min(value = 1, message = "Price between 1 and 10000 (inclusive)")
    private long minPrice;

    @Max(value = 10000, message = "Price between 1 and 10000 (inclusive)")
    private long maxPrice;

    public long getMinPrice() {
        return minPrice;
    }
    public void setMinPrice(long minPrice) {
        this.minPrice = minPrice;
    }
    public long getMaxPrice() {
        return maxPrice;
    }
    public void setMaxPrice(long maxPrice) {
        this.maxPrice = maxPrice;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public int getNumPerson() {
        return numPerson;
    }
    public void setNumPerson(int numPerson) {
        this.numPerson = numPerson;
    }
    @Override
    public String toString() {
        return "Search [country=" + country + ", numPerson=" + numPerson + ", minPrice=" + minPrice + ", maxPrice="
                + maxPrice + "]";
    }
    
}
