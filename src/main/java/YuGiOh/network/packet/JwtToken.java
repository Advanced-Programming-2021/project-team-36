package YuGiOh.network.packet;

import YuGiOh.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.io.Serializable;

public class JwtToken implements Serializable {
    private final static Algorithm algorithm = Algorithm.HMAC256(
            "uhiiyoguoguou7tanieoks09293iorjklaolwyf928ioqjcnjkjo8diuwjkhr28639187092yuk1jlqhn9tyug5ci7v6btu8ony98m");

    private String token;
    private JwtToken(String token) {
        this.token = token;
    }

    public static JwtToken getTokenForUser(User user) {
        return new JwtToken(JWT.create()
                .withClaim("username", user.getUsername())
                .sign(algorithm));
    }
    public static boolean verifyJwtToken(JwtToken jwtToken) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                .withClaim("username", JWT.decode(jwtToken.token).getClaim("username").asString())
                .build();
            verifier.verify(jwtToken.token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }
    public User getUser() {
        if(verifyJwtToken(this))
            return User.getUserByUsername(JWT.decode(token).getClaim("username").asString());
        return null;
    }
}
