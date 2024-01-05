package org.example.lesson5;

import lombok.Getter;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

@Getter
public class Administrator extends User {
    private boolean admin = false;

    Administrator(long id, Socket socket, boolean admin) throws IOException {
        super(id, socket);
        this.admin = admin;
    }

    public void kick(long clientId, Map<Long, User> clients){
        clients.remove(clientId);
    }

}
