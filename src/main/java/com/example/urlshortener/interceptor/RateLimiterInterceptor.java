package com.example.urlshortener.interceptor;

import com.example.urlshortener.ratelimiter.RequestRateLimiter;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimiterInterceptor implements HandlerInterceptor {

  private final RequestRateLimiter rateLimiter;

  @Override
  public boolean preHandle(HttpServletRequest request, @Nullable HttpServletResponse response,
      @Nullable Object handler) throws Exception {
    String key = request.getRemoteAddr();

    if (!rateLimiter.allowRequest(key)) {
      if (response == null) {
        throw new IllegalStateException("HttpServletResponse cannot be null");
      }
      response.sendError(429, "Too many requests - try again later");
      return false;
    }
    return true;
  }
}