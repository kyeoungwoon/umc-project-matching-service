package kr.kyeoungwoon.upms.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

  // 애초에 인증이 되지 않은 상태, 미로그인인 경우 호출됨 (401)

  @Override
  public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex)
      throws IOException {
    ErrorStatus status = ErrorStatus.JWT_TOKEN_NOT_RECEIVED; // 상황에 맞는 코드

    ApiResponse<Object> body = ApiResponse.onFailure(status.getCode(), status.getMessage(), null);

    res.setStatus(status.getHttpStatus().value());
    res.setCharacterEncoding(StandardCharsets.UTF_8.name());
    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
    res.getWriter().write(new ObjectMapper().writeValueAsString(body));
  }
}
