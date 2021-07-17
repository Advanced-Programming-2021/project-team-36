package YuGiOh.network;

import YuGiOh.model.User;
import YuGiOh.network.packet.JwtToken;
import YuGiOh.network.packet.Request;
import YuGiOh.network.packet.Response;
import YuGiOh.network.serializers.MethodSerializer;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ServerConnection extends NetworkConnection {
    public static String defaultIP = "127.0.0.1";
    public static int defaultPort = 5000;

    private static final List<ServerConnection> connections = new ArrayList<>();

    public ServerConnection(Socket socket) throws IOException {
        super(socket);
        connections.add(this);
    }

    private Response<?> runMethodRequest(Request request) {
        Throwable methodRunningException = null;
        Object result = null;
        try {
            MethodSerializer methodSerializer = (MethodSerializer) request.getBody("methodSerializer");
            Method method = methodSerializer.getMethod();
            int pos = 0;
            Object[] providedParameters = (Object[]) request.getBody("parameters");
            List<Object> parameters = new ArrayList<>();
            for(int i = 0; i < method.getParameterTypes().length; i++) {
                Class<?> param = method.getParameterTypes()[i];
                if(param.equals(Request.class)) {
                    parameters.add(request);
                } else {
                    parameters.add(providedParameters[pos]);
                    pos++;
                }
            }
            result = methodSerializer.getMethod().invoke(null, parameters.toArray());
        } catch (IllegalAccessException | IllegalArgumentException exception) {
            exception.printStackTrace();
        } catch (InvocationTargetException exception) {
            methodRunningException = exception.getCause();
        }

        System.out.println("token is " + request.getToken());
        System.out.println(methodRunningException + "   " + result);
        // todo this fails if result is not serializable. but luckily most of them are void
        if(methodRunningException == null)
            return Response.of(request, (Serializable) result);
        else
            return Response.exceptionalOf(request, methodRunningException);
    }

    @Override
    protected CompletableFuture<Response<?>> handleRequestImpl(Request request) {
        // todo it is not complete!
        if(request.getRequestType().equals(Request.RequestType.MethodRunRequest))
            return CompletableFuture.completedFuture(runMethodRequest(request));
        return null;
    }

    @Override
    protected void handleWaitingResponse(CompletableFuture<Response<?>> waitingResponse, Response<?> response) {
        waitingResponse.complete(response);
    }

    public synchronized static List<ServerConnection> getConnectionsWithUser(User user) {
        connections.removeAll(connections.stream().filter(ServerConnection::isClosed).collect(Collectors.toList()));
        return connections.stream().filter(serverConnection -> {
            User tokenUser = null;
            JwtToken token = serverConnection.lastTokenSentOrReceived;
            if(token != null) {
                try {
                    tokenUser = token.getUserThrows();
                } catch (NotAuthenticatedException ignored) {
                }
            }
            return user.equals(tokenUser);
        }).collect(Collectors.toList());
    }
}
