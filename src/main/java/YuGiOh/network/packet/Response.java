package YuGiOh.network.packet;

import lombok.Getter;

import java.io.Serializable;
import java.util.Optional;

public class Response<T extends Serializable> extends Packet{
    private enum Status {
        OK, ERROR
    }

    @Getter
    private final T data;
    private final Status status;

    private Response(Request request, T data, Status status) {
        super(request.getId(), request.getToken());
        this.data = data;
        this.status = status;
    }

    public static <T extends Serializable> Response<T> of(Request request, T data) {
        return new Response<>(request, data, Status.OK);
    }
    public static Response<Throwable> exceptionalOf(Request request, Throwable throwable) {
        return new Response<>(request, throwable, Status.ERROR);
    }


    public Optional<Throwable> getException() {
        if(status.equals(Status.OK))
            return Optional.empty();
        else if(status.equals(Status.ERROR))
            return Optional.of((Throwable) data);
        return Optional.empty();
    }
}
