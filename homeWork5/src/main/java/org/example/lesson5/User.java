package org.example.lesson5;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

@Getter
@Setter
class User implements AutoCloseable {

    private final long id;
    private final Socket socket;
    private final Scanner input;
    private final PrintWriter output;

    private boolean admin = false;

    User(long id, Socket socket) throws IOException {
        this.id = id;
        this.socket = socket;
        this.input = new Scanner(socket.getInputStream());
        this.output = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }

    @Override
    public String toString() {
        if (this.isAdmin())
            return String.format("%s - ADMIN", socket.getInetAddress().toString());
        return String.format("%s", socket.getInetAddress().toString());
    }
}

