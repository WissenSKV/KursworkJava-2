//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package network;

import java.awt.event.ActionEvent;
import java.net.Socket;

public class ConnectionHandler extends Thread {
    private ConnectionListener listener;
    private Socket socket;

    public ConnectionHandler(ConnectionListener var1, Socket var2) {
        this.listener = var1;
        this.socket = var2;
    }

    public void run() {
        if (this.listener != null) {
            ActionEvent var1 = new ActionEvent(this, 0, "CONNECTION ACCEPT");
            if (this.listener.getConnectionHandler() != null) {
                this.listener.getConnectionHandler().actionPerformed(var1);
            }
        }

    }

    public ConnectionListener getListener() {
        return this.listener;
    }

    public Socket getSocket() {
        return this.socket;
    }
}
