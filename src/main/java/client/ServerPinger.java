package client;

import java.io.IOException;
import java.io.ObjectOutputStream;

class ServerPinger extends Thread {

    private ObjectOutputStream clientOutputStream;

    ServerPinger( ObjectOutputStream _clientOutputStream ) {
        super( "ServerPinger" );
        setDaemon(true);
        this.clientOutputStream = _clientOutputStream;
    }


    @Override
    public void run() {
        try {

            while ( true ) {
                Thread.sleep( 1000 * 60 );
                clientOutputStream.writeObject( 1 );
            }
        } catch ( IOException | InterruptedException e ) {}
    }
}
