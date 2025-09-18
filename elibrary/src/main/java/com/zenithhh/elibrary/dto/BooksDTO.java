package com.zenithhh.elibrary.dto;

import com.zenithhh.elibrary.models.Person;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

public class BooksDTO {
    private int id;

    PersonDTOWithoutBooks owner;

    @NotEmpty
    private String title;

    @NotEmpty
    private String author;

    private int year;

    private LocalDateTime taken_at;

    public BooksDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public LocalDateTime getTaken_at() {
        return taken_at;
    }

    public void setTaken_at(LocalDateTime taken_at) {
        this.taken_at = taken_at;
    }

    public PersonDTOWithoutBooks getOwner() {
        return owner;
    }

    public void setOwner(PersonDTOWithoutBooks owner) {
        this.owner = owner;
    }
}
