package kr.kyeoungwoon.upms.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class RequestTimingInterceptor implements HandlerInterceptor {

  private static final String START_TIME = "startTime";
  private static final String TRACE_ID = "traceId";
  private static final String TRACE_ID_HEADER = "X-Trace-Id";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    // 시작 시간 기록
    request.setAttribute(START_TIME, System.currentTimeMillis());

    // Trace ID 생성 또는 기존 것 사용
    String traceId = request.getHeader(TRACE_ID_HEADER);
    if (traceId == null || traceId.isEmpty()) {
      traceId = UUID.randomUUID().toString().substring(0, 8);
    }

    // Request attribute에 저장
    request.setAttribute(TRACE_ID, traceId);

    // MDC에 저장 (로그에서 자동으로 사용)
    MDC.put(TRACE_ID, traceId);

    // 응답 헤더에 추가
    response.setHeader(TRACE_ID_HEADER, traceId);

    return true;
  }

  /**
   * 컨트롤러 실행 후, 응답 커밋 전에 호출됨 여기서 헤더를 설정해야 정상적으로 클라이언트에 전달됨
   */
  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler, ModelAndView modelAndView) {
    Long startTime = (Long) request.getAttribute(START_TIME);
    String traceId = (String) request.getAttribute(TRACE_ID);

    if (startTime != null) {
      long duration = System.currentTimeMillis() - startTime;

      // 응답 헤더에 처리 시간 추가 (응답 커밋 전이므로 가능)
      response.setHeader("X-Response-Time", duration + "ms");

      // Trace ID도 다시 설정 (혹시 모를 경우 대비)
      if (traceId != null) {
        response.setHeader(TRACE_ID_HEADER, traceId);
      }
    }
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) {
    String traceId = (String) request.getAttribute(TRACE_ID);
    Long startTime = (Long) request.getAttribute(START_TIME);

    if (startTime != null) {
      long duration = System.currentTimeMillis() - startTime;
      String method = request.getMethod();
      String uri = request.getRequestURI();
      int status = response.getStatus();
      String emoji = status < 400 ? "✅" : "❌";

      log.info("⏱️  [{}] {} {} {} | {}ms | Status: {}",
          traceId, emoji, method, uri, duration, status);

      if (duration > 1000) {
        log.warn("🐌 [{}] SLOW: {} {} took {}ms", traceId, method, uri, duration);
      }

      if (ex != null) {
        log.error("💥 [{}] ERROR: {} {} | {}", traceId, method, uri, ex.getMessage());
      }
    }

    // MDC 정리 (메모리 누수 방지)
    MDC.clear();
  }
}
