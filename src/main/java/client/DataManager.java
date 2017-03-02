package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DataManager {


    /*
     * Получение данных с сервера
     * Возвращает HashMap с данными сервера
     */
    static Map getMessageFromClient( ObjectInputStream clientInputStream )
            throws IOException, ClassNotFoundException {
        return ( HashMap ) clientInputStream.readObject();
    }


    /*
     * Отправка данных на сервер
     * Отправляет: clientID, IPs
     */
    static void sendMessageToClient( ObjectOutputStream clientOutputStream, String clientID ) throws IOException {
        Map clientRequest = packClientRequestData( clientID );
        clientOutputStream.writeObject( clientRequest );
    }


    /*
     * Упаковывает данных для отправки
     * Помещает clientID, IPs в HashMap для отпраки на сервер
     */
    private static HashMap packClientRequestData( String clientID ) {
        HashMap<String, String> clientRequest = new HashMap<>();

        clientRequest.put( "clientID", clientID );
        clientRequest.put( "IPs", NetworkContainer.getIPAddresses() );
        return clientRequest;
    }


    /*
     * Распаковка данных, полученных от сервера
     * Печатаем в консоль значения полученных ключей
     * Возвращает HashMap с данными от сервера
     */
    public static HashMap unpackData( Map serverData ) {
        catchErrors( serverData );
        showAnnouncement( serverData );
        return ( HashMap ) serverData;
    }


    /*
     * Начало пинга сервера:
     * Раз в минуту происходит оповещение активности клиентского приложения
     */
    public static void startPinging( ObjectOutputStream clientOutputStream ) {
        ServerPinger serverPinger = new ServerPinger( clientOutputStream );
        serverPinger.start();
    }


    /*
     * Перехватывает ошибки в ответе, если они есть
     * Печатает полученные ошибки в консоль
     */
    private static void catchErrors( Map serverData ) {
        String errorText = ( String ) serverData.get( "errorText" );

        if ( errorText != null ) {
            System.out.println();
            System.out.println( "-----------------------------------" );
            System.out.println( "ОБНАРУЖЕНЫ ОШИБКИ:" );
            System.out.println( errorText );
            System.out.println( "-----------------------------------" );
            System.out.println();
            System.exit( -1 );
        }
    }


    /*
     * Показывает объявление пользователю, если оно есть в БД
     */
    private static void showAnnouncement(  Map serverData  ) {
        String announcement = ( String ) serverData.get( "announcement" );

        if ( announcement != null ) {
            System.out.println( "Внимание, объявление:" );
            System.out.println( announcement );
        }
    }
}
