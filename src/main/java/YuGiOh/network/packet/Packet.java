package YuGiOh.network.packet;

import YuGiOh.model.User;
import YuGiOh.network.NotAuthenticatedException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class Packet implements Serializable {
    @Getter
    private final int id;
    @Getter @Setter
    private JwtToken token;

    public Packet(int id, JwtToken token) {
        this.id = id;
        this.token = token;
    }

    public User getUser() {
        if(token == null)
            throw new NotAuthenticatedException();
        return token.getUserThrows();
    }
}
