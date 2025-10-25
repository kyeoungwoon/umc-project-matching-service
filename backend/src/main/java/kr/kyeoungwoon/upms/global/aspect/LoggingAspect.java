package kr.kyeoungwoon.upms.global.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {

  private final ObjectMapper objectMapper;

  @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
  private void controllerMethods() {
  }

  @Around("controllerMethods()")
  public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
    // 요청 정보 추출
    HttpServletRequest request = getCurrentRequest();
    String requestId = UUID.randomUUID().toString().substring(0, 8);
    String method = request != null ? request.getMethod() : "UNKNOWN";
    String uri = request != null ? request.getRequestURI() : "UNKNOWN";
    String clientIp = request != null ? getClientIp(request) : "UNKNOWN";

    // 메서드 정보
    String className = joinPoint.getSignature().getDeclaringTypeName();
    String methodName = joinPoint.getSignature().getName();
    String args = formatArgs(joinPoint.getArgs());

    // 민감한 정보 포함 엔드포인트는 로깅 제외
    if (shouldSkipLogging(uri)) {
      args = null;
    }

    log.info("[{}] >>> {} {} | {}.{}() | IP: {} | args: {}",
        requestId, method, uri, getSimpleClassName(className), methodName, clientIp, args);

    long startTime = System.currentTimeMillis();

    try {
      Object result = joinPoint.proceed();
      long duration = System.currentTimeMillis() - startTime;

      log.info("[{}] <<< {} {} | {}ms | return: {}",
          requestId, method, uri, duration, formatResult(result));

      return result;
    } catch (Exception e) {
      long duration = System.currentTimeMillis() - startTime;

      log.error("[{}] !!! {} {} | {}ms | exception: {} - {}",
          requestId, method, uri, duration, e.getClass().getSimpleName(), e.getMessage());

      throw e;
    }
  }

  private HttpServletRequest getCurrentRequest() {
    try {
      ServletRequestAttributes attrs =
          (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      return attrs != null ? attrs.getRequest() : null;
    } catch (Exception e) {
      return null;
    }
  }

  private String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.isEmpty()) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  private String getSimpleClassName(String fullClassName) {
    int lastDot = fullClassName.lastIndexOf('.');
    return lastDot > 0 ? fullClassName.substring(lastDot + 1) : fullClassName;
  }

  private String formatArgs(Object[] args) {
    if (args == null || args.length == 0) {
      return "(empty)";
    }

    // HttpServletRequest, HttpServletResponse 등은 제외
    Object[] filteredArgs = Arrays.stream(args)
        .filter(arg -> !(arg instanceof HttpServletRequest))
        .filter(arg -> !(arg instanceof jakarta.servlet.http.HttpServletResponse))
        .toArray();

    try {
      String json = objectMapper.writeValueAsString(filteredArgs);
      return json.length() > 500 ? json.substring(0, 500) + "..." : json;
    } catch (Exception e) {
      return Arrays.toString(filteredArgs);
    }
  }

  private String formatResult(Object result) {
    if (result == null) {
      return "null";
    }
    try {
      String json = objectMapper.writeValueAsString(result);
      return json.length() > 500 ? json.substring(0, 500) + "..." : json;
    } catch (Exception e) {
      return result.toString();
    }
  }

  private boolean shouldSkipLogging(String uri) {
    if (uri == null) {
      return false;
    }

    // 로그인 관련 엔드포인트 제외
    return uri.contains("/login")
        || uri.contains("/register")  // 회원가입도 비밀번호 포함
        || uri.contains("/password");  // 비밀번호 변경 관련
  }
}