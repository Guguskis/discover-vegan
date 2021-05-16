package lt.liutikas.configuration;

import lt.liutikas.utility.IsAuthorized;
import lt.liutikas.utility.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationInterceptor.class);

    private final TokenUtil tokenUtil;

    public AuthorizationInterceptor(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true; // Preflight request
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (!handlerMethod.hasMethodAnnotation(IsAuthorized.class)) {
            return true;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (StringUtils.isBlank(authorizationHeader)) {
            return false;
        }

        String token = authorizationHeader.replace("Bearer ", "");

        return tokenUtil.verified(token);
    }
}
