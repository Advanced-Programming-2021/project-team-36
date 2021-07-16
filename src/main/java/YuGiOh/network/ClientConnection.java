package YuGiOh.network;

import YuGiOh.network.packet.JwtToken;
import YuGiOh.network.packet.Request;
import YuGiOh.network.packet.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

public class ClientConnection extends NetworkConnection {
    public static ClientConnection instance;
    private static JwtToken lastTokenSent;

    private ClientConnection() throws IOException {
        super(getSocket());
        instance = this;
    }
    private static Socket getSocket() throws IOException {
        return new Socket(ServerConnection.defaultIP, ServerConnection.defaultPort);
    }

    @Override
    public void send(Response<?> response) throws IOException {
        if(lastTokenSent != null)
            response.setToken(lastTokenSent);
        super.send(response);
    }

    @Override
    public CompletableFuture<Response<?>> send(Request request) throws IOException {
        if(lastTokenSent != null)
            request.setToken(lastTokenSent);
        return super.send(request);
    }

    public static ClientConnection getOrCreateInstance() throws IOException {
        if(instance == null || instance.isClosed())
            new ClientConnection();
        instance.startListening();
        return instance;
    }

    @Override
    protected CompletableFuture<Response<?>> handleRequest(Request request) {
        if(request.getToken() != null)
            lastTokenSent = request.getToken();
        return CompletableFuture.failedFuture(new Error("not implemented yet!"));
        // todo not implemented
    }

    @Override
    protected void handleResponse(Response<?> response) {
        if(response.getToken() != null)
            lastTokenSent = response.getToken();
        super.handleResponse(response);
    }
}
