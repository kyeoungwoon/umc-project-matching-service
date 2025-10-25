package kr.kyeoungwoon.upms.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.function.Supplier;
import kr.kyeoungwoon.upms.security.annotation.Public;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class CustomAuthorizationManager implements
    AuthorizationManager<RequestAuthorizationContext> {

  private final RequestMappingHandlerMapping handlerMapping;

  public CustomAuthorizationManager(
      @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
    this.handlerMapping = handlerMapping;
  }

  @Override
  public AuthorizationDecision check(Supplier<Authentication> authentication,
      RequestAuthorizationContext context) {
    HttpServletRequest request = context.getRequest();

    try {
      HandlerExecutionChain handler = handlerMapping.getHandler(request);

      if (handler != null && handler.getHandler() instanceof HandlerMethod handlerMethod) {
        // 메서드에 @Public이 있으면 허용
        if (handlerMethod.hasMethodAnnotation(Public.class)) {
          return new AuthorizationDecision(true);
        }

        // 클래스에 @Public이 있으면 허용
        if (handlerMethod.getBeanType().isAnnotationPresent(Public.class)) {
          return new AuthorizationDecision(true);
        }
      }
    } catch (Exception e) {
      // 핸들러를 찾지 못한 경우
    }

    // @Public이 없으면 인증 여부 확인
    Authentication auth = authentication.get();
    boolean isAuthenticated = auth != null && auth.isAuthenticated()
        && !"anonymousUser".equals(auth.getPrincipal());

    return new AuthorizationDecision(isAuthenticated);
  }
}