package YuGiOh.network;

import YuGiOh.model.User;
import YuGiOh.network.packet.JwtToken;
import YuGiOh.network.packet.Packet;
import YuGiOh.network.packet.Request;
import YuGiOh.network.packet.Response;

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
    protected JwtToken lastTokenSentOrReceived = null;

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
        if(response.getToken() != null)
            lastTokenSentOrReceived = response.getToken();
        out.writeObject(response);
    }

    public CompletableFuture<Response<?>> send(Request request) throws IOException {
        if(request.getToken() != null)
            lastTokenSentOrReceived = request.getToken();
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
        lastTokenSentOrReceived = null;
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    private CompletableFuture<Response<?>> handleRequest(Request request) {
        if(request.getToken() != null)
            lastTokenSentOrReceived = request.getToken();
        return handleRequestImpl(request);
    }

    abstract protected CompletableFuture<Response<?>> handleRequestImpl(Request request);
    abstract protected void handleWaitingResponse(CompletableFuture<Response<?>> waitingResponse, Response<?> response);

    protected void handleResponse(Response<?> response) {
        if(response.getToken() != null)
            lastTokenSentOrReceived = response.getToken();
        CompletableFuture<Response<?>> waitingResponse = waitingResponses.get(response.getId());
        if(waitingResponse.isDone())
            return;
        handleWaitingResponse(waitingResponse, response);
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
