package YuGiOh.network.packet;

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
}
