package com.easybook.apigateway.filter;

import com.easybook.apigateway.util.JwtUtil;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

  @Autowired
  private RouteValidator routeValidator;

  @Autowired
  private JwtUtil jwtUtil;

  public AuthFilter() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      if (routeValidator.isSecured.test(exchange.getRequest())) {
        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
          throw new RuntimeException("Missing authorization header");
        }
        String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
          authHeader = authHeader.substring(7);
        }
        try {
          jwtUtil.validateToken(authHeader);
        } catch (Exception e) {
          throw new RuntimeException("Invalid access");
        }
      }
      return chain.filter(exchange);
    };
  }

  public static class Config {

  }
}
