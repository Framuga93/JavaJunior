package org.example;

import jakarta.persistence.*;
import org.example.Entities.Author;
import org.example.Entities.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

public class JPA {
    public static void main(String[] args) {
        final SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml").buildSessionFactory();

        createListOfBooksAndAuthors();

        System.out.println("-------------------------------------------------------");

        showAuthorBooks(sessionFactory);

        System.out.println("-------------------------------------------------------");

        addAuthorsToBooks(sessionFactory);

        System.out.println("-------------------------------------------------------");

        sessionFactory.close();
    }

    private static void addAuthorsToBooks(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Author leoTolst = new Author();
            leoTolst.setName("Leo Tolstoy");
            Author gustaveFlau = new Author();
            gustaveFlau.setName("Gustave Flaubert");
            Author vladimirNabokov = new Author();
            vladimirNabokov.setName("Vladimir Nabokov");
            Author markTwain = new Author();
            markTwain.setName("Mark Twain");

            Book warAndPeace = new Book();
            warAndPeace.setBookName("War and Peace");
            Book annaKaren = new Book();
            annaKaren.setBookName("Anna Karenina");
            Book lolita = new Book();
            lolita.setBookName("Lolita");
            Book hucklFin = new Book();
            hucklFin.setBookName("The Adventures of Huckleberry Finn");
            Book madameBov = new Book();
            madameBov.setBookName("Madame Bovary");

            session.persist(leoTolst);
            session.persist(gustaveFlau);
            session.persist(vladimirNabokov);
            session.persist(markTwain);

            warAndPeace.setAuthor(leoTolst);
            annaKaren.setAuthor(leoTolst);
            lolita.setAuthor(vladimirNabokov);
            hucklFin.setAuthor(markTwain);
            madameBov.setAuthor(gustaveFlau);

            session.persist(warAndPeace);
            session.persist(annaKaren);
            session.persist(lolita);
            session.persist(hucklFin);
            session.persist(madameBov);


            session.getTransaction().commit();
        }
    }

    private static void showAuthorBooks(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            TypedQuery<Book> query = session.createQuery("select b from Book b where author.name = ?1", Book.class);
            String authorName = "Leo Tolstoy";
            List<Book> authorBook = query.setParameter(1, authorName).getResultList();

            authorBook.forEach(System.out::println);
        }
    }

    private static void createListOfBooksAndAuthors() {
        List<Book> books = new ArrayList<>(List.of(
                new Book("Anna Karenina"),
                new Book("Madame Bovary"),
                new Book("War and Peace"),
                new Book("The Great Gatsby"),
                new Book("Lolita"),
                new Book("The Adventures of Huckleberry Finn"),
                new Book("Middlemarch"),
                new Book("The Stories of Anton Chekhov"),
                new Book("In Search of Lost Time"),
                new Book("Hamlet")
        )
        );

        List<Author> authors = new ArrayList<>(List.of(
                new Author("Leo Tolstoy"),
                new Author("Gustave Flaubert"),
                new Author("F. Scott Fitzgerald"),
                new Author("Vladimir Nabokov"),
                new Author("Mark Twain"),
                new Author("George Eliot"),
                new Author("Anton Chekhov"),
                new Author("Marcel Proust"),
                new Author("William Shakespeare")
        )
        );
    }
}
