package client;

import subscriber.InstagramDriver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class InstagramSubscriberClient {

    // Хардкод порта и хоста
    private static final String HOST = "85.143.211.235";
    private static final int PORT = 13542;
    private static final String clientID = "TEST-UUID-1239084";
    // TODO: 12.11.16 Скрипт создания клиента, id положить сюда

    public static void main(String[] args) {
        //addCloseDriversHook();

        try(Socket fromServerSocket = new Socket(HOST, PORT);
        ObjectOutputStream clientOutputStream = new ObjectOutputStream(fromServerSocket.getOutputStream());
        ObjectInputStream clientInputStream = new ObjectInputStream(fromServerSocket.getInputStream())) {

            // Отправка данных на сервер
            DataManager.sendMessageToClient(clientOutputStream, clientID);

            // Получение данных с сервера
            Map serverData = DataManager.getMessageFromClient(clientInputStream);

            InstagramDriver.start( serverData, clientOutputStream );

        } catch (IOException e) {
            System.out.println("Errors with client socket");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Errors with client output stream");
            e.printStackTrace();
        }
    }

    // TODO: 20.11.2016 Не используется сейчас
    /*
     * Хук, уничтожающий все chromedriver
     */
    private static void addCloseDriversHook() {
        Runtime runtime = Runtime.getRuntime();

        runtime.addShutdownHook( new Thread() {

            @Override
            public void run() {
                String[] commands = {"cmd /c taskkill /f /im chromedriver*"};
                try {

                    for ( String command : commands ) {
                        runtime.exec(command);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Stopping");
            }
        } );
    }
}