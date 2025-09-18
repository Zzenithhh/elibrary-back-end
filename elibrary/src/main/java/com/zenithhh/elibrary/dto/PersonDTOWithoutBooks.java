package com.zenithhh.elibrary.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class PersonDTOWithoutBooks {
    private int id;

    @NotEmpty(message = "First name can't be empty")
    private String first_name;

    @NotEmpty(message = "Last name can't be empty")
    private String last_name;

    @Min(value = 1900, message = "Year must be grater than 1900")
    private int year;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
