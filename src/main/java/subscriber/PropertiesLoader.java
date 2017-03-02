package subscriber;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class PropertiesLoader {

    private static final String localDirectory = System.getProperty( "user.dir" ) + File.separator;
    private static final String propertiesFilename = "Subscribe.properties";

    static String getLocalDirectory() {
        return localDirectory;
    }

    static Properties getProperties() {
        Properties properties = null;
        String propertiesPath = getLocalDirectory() + propertiesFilename;

        try( FileInputStream propertiesInputStream = new FileInputStream( propertiesPath ) ) {
            properties = new Properties();
            properties.load( propertiesInputStream );

        }
        catch ( IOException ex ) {
            System.out.println( "Конфигурационный файл " + propertiesPath + " не найден либо содержит ошибки" );
        }
        return properties;
    }
}
