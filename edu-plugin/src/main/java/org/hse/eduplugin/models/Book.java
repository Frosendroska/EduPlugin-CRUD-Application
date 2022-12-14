package org.hse.eduplugin.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String author;
    @Column
    private String isbn;
    @Column
    private Integer printYear;
    @Column
    private Boolean readAlready;

    public Book() {
    }

    public Book(String title_, String description_, String author_, String isbn_, Integer printYear_, Boolean readAlready_) {
        title = title_;
        description = description_;
        author = author_;
        isbn = isbn_;
        printYear = printYear_;
        readAlready = readAlready_;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPrintYear() {
        return printYear;
    }

    public void setPrintYear(Integer printYear) {
        this.printYear = printYear;
    }

    public Boolean getReadAlready() {
        return readAlready;
    }

    public void setReadAlready(Boolean readAlready) {
        this.readAlready = readAlready;
    }
}
