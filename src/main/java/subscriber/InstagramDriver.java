package subscriber;

import client.DataManager;

import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Properties;

public class InstagramDriver {

    private static Properties properties;

    private static String getTarget() {
        return properties.getProperty( "target" );
    }

    public static void start( Map serverData, ObjectOutputStream clientOutputStream ) {
        properties = PropertiesLoader.getProperties();
        Map locators = DataManager.unpackData( serverData );
        DataManager.startPinging( clientOutputStream );

        InstagramManager instagramManager = new InstagramManager( properties, locators );

        switch ( getTarget() ) {
            case "subscribe":
                System.out.println( "Начинается работа в режиме подписки" );
                InstagramSubscriber.start( instagramManager );
                break;
            case "unsubscribe":
                System.out.println( "Начинается работа в режиме отписки" );
                InstagramUnsubscriber.start( instagramManager );
                break;
            default:
                System.out.println( "Не распознан режим, завершение работы." );
                break;
        }
    }
}