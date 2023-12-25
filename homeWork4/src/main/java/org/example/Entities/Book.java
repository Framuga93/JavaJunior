package org.example.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "bookHibernate")
public class Book {
    public Book() {
    }

    public Book(String bookName) {
        this.bookName = bookName;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String bookName;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }


    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + bookName + '\'' +
//                ", author='" + author + '\'' +
                '}';
    }
}
