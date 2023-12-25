package org.example;

import java.lang.annotation.Retention;
import java.sql.*;

public class JDBC {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaJunior", "root", "root")){

            prepareTable(connection);
            insertData(connection);
            executeTables(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void insertData(Connection connection){
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate("""
                    insert into book (name, author)
                    values('Anna Karenina', 'Leo Tolstoy'),
                          ('Madame Bovary',  'Gustave Flaubert'),
                          ('War and Peace', 'Leo Tolstoy'),
                          ('The Great Gatsby', 'F. Scott Fitzgerald'),
                          ('Lolita', 'Vladimir Nabokov'),
                          ('The Adventures of Huckleberry Finn', 'Mark Twain'),
                          ('Middlemarch', 'George Eliot'),
                          ('The Stories of Anton Chekhov', 'Anton Chekhov'),
                          ('In Search of Lost Time', 'Marcel Proust'),
                          ('Hamlet', 'William Shakespeare')""");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void prepareTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    create table if not exists book (
                    id bigint auto_increment primary key,
                    name varchar(255),
                    author varchar(255)
                    )
                    """);

            //set auto incremented ID
//            statement.execute("""
//                    ALTER TABLE book MODIFY COLUMN id INT auto_increment
//                    """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void executeTables(Connection connection){
        try(Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("select * from book where author = 'Leo Tolstoy'");
            while (resultSet.next()){
                System.out.println("Author: " + resultSet.getString(2));
                System.out.println("Book Name: " + resultSet.getString(1));
            }
            resultSet.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
