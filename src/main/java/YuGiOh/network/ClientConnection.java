package YuGiOh.network;

import YuGiOh.model.User;
import YuGiOh.network.packet.JwtToken;
import YuGiOh.network.packet.Request;
import YuGiOh.network.packet.Response;
import javafx.application.Platform;
import javafx.print.PageLayout;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

public class ClientConnection extends NetworkConnection {
    public static ClientConnection instance;

    private ClientConnection() throws IOException {
        super(getSocket());
        instance = this;
    }
    private static Socket getSocket() throws IOException {
        return new Socket(ServerConnection.defaultIP, ServerConnection.defaultPort);
    }

    @Override
    public void send(Response<?> response) throws IOException {
        if(lastTokenSentOrReceived != null)
            response.setToken(lastTokenSentOrReceived);
        super.send(response);
    }

    @Override
    public CompletableFuture<Response<?>> send(Request request) throws IOException {
        if(lastTokenSentOrReceived != null)
            request.setToken(lastTokenSentOrReceived);
        return super.send(request);
    }

    public static ClientConnection getOrCreateInstance() throws IOException {
        if(instance == null || instance.isClosed())
            new ClientConnection();
        instance.startListening();
        return instance;
    }

    public static void disconnectAll() {
        try {
            if (instance != null)
                instance.closeConnection();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    @Override
    protected CompletableFuture<Response<?>> handleRequestImpl(Request request) {
        return CompletableFuture.failedFuture(new Error("not implemented yet!"));
        // todo not implemented
    }

    @Override
    protected void handleWaitingResponse(CompletableFuture<Response<?>> waitingResponse, Response<?> response) {
        Platform.runLater(()-> waitingResponse.complete(response));
    }

    @Override
    protected void handleResponse(Response<?> response) {
        super.handleResponse(response);
    }

    public static User getUserThrows() {
        if(instance == null || instance.lastTokenSentOrReceived == null)
            throw new NotAuthenticatedException();
        return instance.lastTokenSentOrReceived.getUserThrows();
    }
}
