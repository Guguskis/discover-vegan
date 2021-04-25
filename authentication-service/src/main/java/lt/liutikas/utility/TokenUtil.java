package lt.liutikas.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lt.liutikas.model.UserType;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    public String getToken(Integer userId, UserType userType) {
        Algorithm algorithm = Algorithm.HMAC256("secret");

        return JWT.create()
                .withIssuer("discover-vegan-auth")
                .withClaim("userId", userId)
                .withClaim("userType", userType.toString())
                .sign(algorithm);
    }
}
