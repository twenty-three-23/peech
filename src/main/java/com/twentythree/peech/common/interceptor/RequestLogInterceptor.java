package com.twentythree.peech.common.interceptor;

import com.twentythree.peech.auth.dto.SecurityContextHolderDTO;
import com.twentythree.peech.auth.service.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

public class RequestLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
        Long userId;
        String funnel;
        try {
            SecurityContextHolderDTO contextHolder = SecurityContextHolder.getContextHolder();
            userId = contextHolder.getUserId();
            funnel = contextHolder.getFunnel();

        } catch (NullPointerException e) {
            userId = null;
            funnel = "not found funnel";
        }
        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        logger.info("REQUEST LOG: [ Funnel: {}, User ID: {}, Http Method: {}, Request URI: {}, UUID: {} ]", funnel, userId, httpMethod, requestURI, uuid);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
