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
                        output.println("Подключение успешно. Список всех клиентов: " + clients);

                        while (true) {
                            String clientInput = input.nextLine();
                            if (Objects.equals("q", clientInput)) {
                                clientQuit(clientId);
                                break;
                            }

                            if (clientInput.startsWith("@")) {
                                sendPrivateMsg(wrapper, clientInput);
                                continue;
                            }

                            if (Objects.equals("/admin", clientInput)) {
                                authAdmin(wrapper, input);
                                continue;
                            }

                            if (Objects.equals("kick", clientInput) && wrapper.isAdmin()) {
                                kickIfAdmin(wrapper, input);
                                continue;
                            }

                            if (Objects.equals("clients", clientInput)) {
                                getClientList(wrapper);
                                continue;
                            }

                            publicMsg(clientId, clientInput);
                        }
                    }
                }).start();
            }
        }
    }


    private static void clientQuit(long clientId) {
        clients.remove(clientId);
        clients.values().forEach(it -> it.getOutput().println("Клиент[" + clientId + "] отключился"));
    }

    private static void sendPrivateMsg(User wrapper, String clientInput) {
        long destinationId = Long.parseLong(clientInput.substring(1, 2));
        if (destinationId < clients.size()+1) {
            User destination = clients.get(destinationId);
            List<String> clientMsg = new ArrayList<>(List.of(clientInput.split(" ")));
            clientMsg.remove(0);
            String result = clientMsg.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(" "));
            destination.getOutput().println("Сообщение от [" + wrapper.getId() + "] : " + result);
            return;
        }
        wrapper.getOutput().println("Такого пользователя не существует\n" +
                "Список пользователей " + clients);
    }

    private static void authAdmin(User wrapper, Scanner input) {
        wrapper.getOutput().println("Введите пароль");
        int tempPass = 0;
        while (true) {
            String pass = input.nextLine();
            if (tempPass > 2) {
                wrapper.getOutput().println("Вы ввели парольн не правильно слишком много раз");
                break;
            }
            if (pass.equals("pass")) {
                wrapper.getOutput().println("Добро пожаловать Админ ВСЕЯРУСИ");
                wrapper.setAdmin(true);
                break;
            }
            wrapper.getOutput().println("Не верный пароль");
            tempPass++;
        }
    }

    private static void kickIfAdmin(User wrapper, Scanner input) {
        wrapper.getOutput().println("Введите ID того кого нужно кикнуть \n" +
                "Список клиентов " + clients);
        String kickId = input.nextLine();
        clients.remove(Long.parseLong(kickId));
        wrapper.getOutput().println("Клиент " + kickId + " кикнут");
    }

    private static void getClientList(User wrapper) {
        wrapper.getOutput().println(clients);
    }

    private static void publicMsg(long clientId, String clientInput) {
        clients.forEach((id, socket) -> {
            if (socket.getId() != clientId)
                socket.getOutput().println("Сообщение от [" + clientId + "] :" + clientInput);
        });
    }


}

