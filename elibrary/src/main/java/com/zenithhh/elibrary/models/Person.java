package com.zenithhh.elibrary.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.event.SpringApplicationEvent;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    @Column(name = "first_name")
    @NotEmpty(message = "First name can't be empty")
    private String first_name;

    @Column(name = "last_name")
    @NotEmpty(message = "Last name can't be empty")
    private String last_name;

    @Column(name = "year")
    @Min(value = 1900, message = "Year must be grater than 1900")
    private int year;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Column(name = "created_who")
    @NotEmpty
    private String created_who;

    public Person() {
    }

    public Person(int id, String first_name, String last_name, int year) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.year = year;
    }

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

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_who() {
        return created_who;
    }

    public void setCreated_who(String created_who) {
        this.created_who = created_who;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
