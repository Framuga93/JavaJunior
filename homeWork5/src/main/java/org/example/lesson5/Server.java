package org.example.lesson5;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server {

    // message broker (kafka, redis, rabbitmq, ...)
    // client sent letter to broker

    // server sent to SMTP-server

    public static final int PORT = 8181;

    private static long clientIdCounter = 1L;
    private static final Map<Long, User> clients = new HashMap<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);
            while (true) {
                final Socket client = server.accept();
                final long clientId = clientIdCounter++;

                User wrapper = new User(clientId, client);
                System.out.println("Подключился новый клиент[" + wrapper + "]");
                clients.put(clientId, wrapper);

                new Thread(() -> {
                    try (Scanner input = wrapper.getInput(); PrintWriter output = wrapper.getOutput()) {
                        output.println("Подключение успешно. Список всех клиентов: " + clients);  // msg from server to client

                        while (true) {
                            String clientInput = input.nextLine();  // wait msg from client
                            if (Objects.equals("q", clientInput)) {
                                clientQuit(clientId);
                                break;
                            }

                            if (clientInput.startsWith("@")) {
                                sendPrivateMsg(clientId, clientInput);
                                continue;
                            }

                            if (Objects.equals("/admin", clientInput)) {

                            }

                            publicMsg(clientId, clientInput);
                        }
                    }
                }).start();
            }
        }
    }

    private static void publicMsg(long clientId, String clientInput) {
        clients.forEach((id, socket) -> {
            if (socket.getId() != clientId)
                socket.getOutput().println("Сообщение от [" + clientId + "] :" + clientInput);
        });
    }

    private static void clientQuit(long clientId) {
        clients.remove(clientId);
        clients.values().forEach(it -> it.getOutput().println("Клиент[" + clientId + "] отключился"));
    }

    private static void sendPrivateMsg(long clientId, String clientInput) {
        long destinationId = Long.parseLong(clientInput.substring(1, 2));
        User destination = clients.get(destinationId);
        List<String> clientMsg = new ArrayList<>(List.of(clientInput.split(" ")));
        clientMsg.remove(0);
        String result = clientMsg.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
        destination.getOutput().println("Сообщение от [" + clientId + "] :" + result);
    }

}

