package YuGiOh.network.packet;

import YuGiOh.network.serializers.MethodSerializer;
import lombok.Getter;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Request extends Packet {
    public enum RequestType {
        MethodRunRequest;
    }

    @Getter
    private final RequestType requestType;
    private final HashMap<String, Serializable> body = new HashMap<>();
    private static int idCounter = 0;

    public Request(Method method, Serializable... parameters) {
        super(idCounter, null);
        idCounter ++;
        this.requestType = RequestType.MethodRunRequest;
        body.put("parameters", parameters);
        body.put("methodSerializer", new MethodSerializer(method));
    }
    public Serializable getBody(String key) {
        return body.get(key);
    }
}
