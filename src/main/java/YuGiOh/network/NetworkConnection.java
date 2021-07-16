package YuGiOh.network;

import YuGiOh.network.packet.Packet;
import YuGiOh.network.packet.Request;
import YuGiOh.network.packet.Response;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.*;

public abstract class NetworkConnection {
    private final ConnectionThread connectionThread;
    private final Socket socket;
    private final HashMap<Integer, CompletableFuture<Response<?>>> waitingResponses = new HashMap<>();
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public NetworkConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.socket.setTcpNoDelay(true);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.connectionThread = new ConnectionThread();
        this.connectionThread.setDaemon(true);
    }

    public void startListening() {
        if(!connectionThread.isAlive()) {
            System.out.println(this + " started listening...");
            connectionThread.start();
        }
    }

    public void send(Response<?> response) throws IOException {
        out.writeObject(response);
    }

    public CompletableFuture<Response<?>> send(Request request) throws IOException {
        CompletableFuture<Response<?>> response = new CompletableFuture<>();
        waitingResponses.put(request.getId(), response);
        out.writeObject(request);
        return response;
    }

    private void sendResponseIfCan(Response<?> response) {
        try {
            send(response);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void closeConnection() throws IOException {
        socket.close();
        in.close();
        out.close();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    protected abstract CompletableFuture<Response<?>> handleRequest(Request request);

    protected void handleResponse(Response<?> response) {
        CompletableFuture<Response<?>> waitingResponse = waitingResponses.get(response.getId());
        if(waitingResponse.isDone())
            return;
        Platform.runLater(()-> waitingResponse.complete(response));
    }

    private class ConnectionThread extends Thread {
        @Override
        public void run() {
            while (!socket.isClosed()) {
                Packet packet;
                try {
                    packet = (Packet) in.readObject();
                } catch (Exception exception) {
                    exception.printStackTrace();
                    break;
                }
                if(packet instanceof Request)
                     handleRequest((Request) packet).thenAccept(NetworkConnection.this::sendResponseIfCan);
                else if(packet instanceof Response)
                    handleResponse((Response<?>) packet);
                // and ignores every other object
            }
        }
    }
}
