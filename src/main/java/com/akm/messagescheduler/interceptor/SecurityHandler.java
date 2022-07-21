package com.akm.messagescheduler.interceptor;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.akm.messagescheduler.annotation.ValidUser;
import com.akm.messagescheduler.error.InvalidTokenException;

@Component
public class SecurityHandler implements HandlerInterceptor {

    @Value("${auth.token}")
    private String authToken;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws InvalidTokenException {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            boolean hasValidUser = handlerMethod.hasMethodAnnotation(ValidUser.class);
            if (hasValidUser) {
                String validationToken = request.getHeader("token");
                if (Objects.isNull(validationToken) || Boolean.FALSE.equals(validationToken.equals(authToken))) {
                    throw new InvalidTokenException("Please provide valid token");
                }
            }
        }

        return true;
    }
}
