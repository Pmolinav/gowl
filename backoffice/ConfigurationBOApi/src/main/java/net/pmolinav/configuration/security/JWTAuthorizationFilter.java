package net.pmolinav.configuration.security;

import net.pmolinav.bookingslib.dto.MDCKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
//        try {
        MDC.clear();
        String correlationUid = UUID.randomUUID().toString();

        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request, correlationUid);
        HttpResponseWrapper responseWrapper = new HttpResponseWrapper(response, correlationUid);

        MDC.put(MDCKeys.originIP.name(), requestWrapper.getRemoteAddr());
        MDC.put(MDCKeys.correlationUid.name(), correlationUid);

        logger.info("Incoming call received. Method: {}. Path: {}. Query params: {}. " +
                        "Request Body: {}. Headers: {}. Client IP: {}. Correlation-Uid: {} ",
                requestWrapper.getMethod(), requestWrapper.getRequestURI(),
                requestWrapper.getQueryParams(), requestWrapper.getRequestBody(),
                requestWrapper.getHeaders(), requestWrapper.getRemoteAddr(), correlationUid
        );

        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        // TODO: Revisar por qué cuando una petición va OK luego siempre funciona aunque cambie
        // el token y coja uno que no es válido.
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.replace("Bearer ", "");
            UsernamePasswordAuthenticationToken usernamePasswordAT = TokenUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAT);
        }

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long duration = System.currentTimeMillis() - startTime;

        byte[] responseData = responseWrapper.getResponseData();
        String responseBody = new String(responseData, response.getCharacterEncoding());
        if (responseWrapper.getStatus() >= 400 && responseWrapper.getStatus() < 500) {
            logger.warn("Incoming call failed with status {}. Elapsed time: {} ms. Response Body: {}. Correlation-Uid: {}",
                    responseWrapper.getStatus(), duration, responseBody, correlationUid);
        } else if (responseWrapper.getStatus() >= 500) {
            logger.error("Incoming call failed with status {}. Elapsed time: {} ms. Response Body: {}. Correlation-Uid: {}",
                    responseWrapper.getStatus(), duration, responseBody, correlationUid);
        } else {
            logger.info("Incoming call succeeded with status {}. Elapsed time: {} ms. Response Body: {}. Correlation-Uid: {}",
                    responseWrapper.getStatus(), duration, responseBody, correlationUid);
        }

        MDC.put(MDCKeys.elapsedTime.name(), String.valueOf(duration));

        // Write the response data back to the actual response
        responseWrapper.copyBodyToResponse();
//        } finally {
//            // Clear the security context in all requests.
//            SecurityContextHolder.clearContext();
//        }
    }
}


