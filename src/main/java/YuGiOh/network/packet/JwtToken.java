package YuGiOh.network.packet;

import YuGiOh.model.User;
import YuGiOh.network.NotAuthenticatedException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.io.Serializable;

public class JwtToken implements Serializable {
    private final static Algorithm algorithm = Algorithm.HMAC256(
            "uhiiyoguoguou7tanieoks09293iorjklaolwyf928ioqjcnjkjo8diuwjkhr28639187092yuk1jlqhn9tyug5ci7v6btu8ony98m");

    private final String token;
    private JwtToken(String token) {
        this.token = token;
    }

    public static JwtToken getTokenForUser(User user) {
        return new JwtToken(JWT.create()
                .withClaim("userId", user.getUserId())
                .sign(algorithm));
    }
    public static boolean verifyJwtToken(JwtToken jwtToken) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                .withClaim("userId", JWT.decode(jwtToken.token).getClaim("userId").asInt())
                .build();
            verifier.verify(jwtToken.token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }
    public User getUserThrows() throws NotAuthenticatedException {
        if(verifyJwtToken(this))
            return User.getUserByUserId(JWT.decode(token).getClaim("userId").asInt());
        throw new NotAuthenticatedException();
    }
}
