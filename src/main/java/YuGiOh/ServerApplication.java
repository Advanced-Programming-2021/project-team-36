package YuGiOh;

import YuGiOh.network.ServerConnection;
import YuGiOh.utils.DatabaseHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApplication {
    private final ServerSocket serverSocket;

    public ServerApplication(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        initialize();
    }
    private void initialize() {
        DatabaseHandler.importFromDatabase();
    }
    public void start() {
        Thread thread = new Thread(()->{
            while (!serverSocket.isClosed()) {
                try {
                    connectNewClient(serverSocket.accept());
                } catch (IOException ignored) {
                }
            }
        });
        thread.start();
    }

    public void connectNewClient(Socket socket) throws IOException {
        ServerConnection connection = new ServerConnection(socket);
        connection.startListening();
    }

    public static void main(String[] args) throws IOException {
        new ServerApplication(ServerConnection.defaultPort).start();
    }
}
