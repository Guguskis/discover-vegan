package lt.liutikas.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    private static final String SECRET = "secret";
    private static final String ISSUER = "discover-vegan-auth";

    public String getToken(Integer userId, String userType) {

        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim("userId", userId)
                .withClaim("userType", userType)
                .sign(getAlgorithm());
    }

    public boolean tokenVerified(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer(ISSUER)
                .build();

        verifier.verify(token);
        return true;
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SECRET);
    }
}
