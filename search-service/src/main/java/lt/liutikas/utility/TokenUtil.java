package lt.liutikas.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lt.liutikas.configuration.exception.AuthorizationException;
import org.apache.commons.lang3.StringUtils;
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

    public boolean verified(String token) {

        if (StringUtils.isBlank(token)) {
            throw new AuthorizationException("No authorization token provided");
        }

        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer(ISSUER)
                .build();

        try {
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new AuthorizationException("Authorization token failed verification");
        }

        return true;
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SECRET);
    }
}
