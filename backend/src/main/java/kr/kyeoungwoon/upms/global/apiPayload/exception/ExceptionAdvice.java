package kr.kyeoungwoon.upms.global.apiPayload.exception;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import kr.kyeoungwoon.upms.global.apiPayload.code.ErrorReasonDTO;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
@RequiredArgsConstructor
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

  @Value("${spring.profiles.active:local}")
  private String activeProfile;

  /**
   * Spring Security 권한 거부 예외 처리 - AuthorizationDeniedException: @PreAuthorize 등 메서드 레벨 보안 -
   * AccessDeniedException: URL 패턴 기반 보안
   */
  @ExceptionHandler({AuthorizationDeniedException.class, AccessDeniedException.class})
  public ResponseEntity<Object> handleAccessDenied(Exception e, WebRequest request) {
    log.warn("[ACCESS DENIED] {}", e.getMessage());

    return buildErrorResponse(e, ErrorStatus.USER_LACK_PERMISSION, request, e.getMessage());
  }

  /**
   * Validation 제약 조건 위반 예외 처리
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e,
      WebRequest request) {
    String errorMessage = e.getConstraintViolations().stream()
        .map(ConstraintViolation::getMessage)
        .findFirst()
        .orElse("_BAD_REQUEST");

    ErrorStatus errorStatus;
    try {
      errorStatus = ErrorStatus.valueOf(errorMessage);
    } catch (IllegalArgumentException ex) {
      log.warn("[CONSTRAINT VIOLATION] 알 수 없는 ErrorStatus: {}", errorMessage);
      errorStatus = ErrorStatus._BAD_REQUEST;
    }

    return buildErrorResponse(e, errorStatus, request, null);
  }

  /**
   * @Valid 검증 실패 예외 처리
   */
  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {

    Map<String, String> errors = new LinkedHashMap<>();

    e.getBindingResult().getFieldErrors()
        .forEach(fieldError -> {
          String fieldName = fieldError.getField();
          String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
          errors.merge(fieldName, errorMessage,
              (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", "
                  + newErrorMessage);
        });

    log.warn("[VALIDATION ERROR] {}", errors);

    return handleExceptionInternalArgs(e, HttpHeaders.EMPTY, ErrorStatus._BAD_REQUEST,
        request, errors);
  }

  /**
   * JSON 파싱 에러 처리 (Request Body 읽기 실패)
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException e,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    log.error("JSON 파싱 에러: {}", e.getMessage());

    String errorMessage = e.getMessage();
    String simplifiedMessage = "잘못된 요청 형식입니다";

    if (errorMessage != null) {
      if (errorMessage.contains("Cannot deserialize")) {
        simplifiedMessage = "요청 데이터 타입이 올바르지 않습니다";
      } else if (errorMessage.contains("Required request body is missing")) {
        simplifiedMessage = "요청 본문이 필요합니다";
      } else if (errorMessage.contains("JSON parse error")) {
        simplifiedMessage = "JSON 형식이 올바르지 않습니다";
      }
    }

    return handleExceptionInternalFalse(
        e,
        ErrorStatus._BAD_REQUEST,
        headers,
        HttpStatus.BAD_REQUEST,
        request,
        simplifiedMessage + " - " + e.getMostSpecificCause().getMessage()
    );
  }


  /**
   * 처리되지 않은 모든 예외의 기본 핸들러 프로덕션 환경에서는 상세 에러 메시지를 숨김
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleUnhandledException(Exception e, WebRequest request) {
    log.error("[UNHANDLED EXCEPTION] {}", e.getMessage(), e);

    String errorDetail = isProductionProfile()
        ? "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
        : e.getMessage();

    return buildErrorResponse(e, ErrorStatus._INTERNAL_SERVER_ERROR, request, errorDetail);
  }

  @ExceptionHandler(value = DomainException.class)
  public ResponseEntity<Object> onDomainException(DomainException domainException,
      HttpServletRequest request) {
    log.error("[DOMAIN EXCEPTION] [DOMAIN] {} [MESSAGE] {}", domainException.getDomainType(),
        domainException.getErrorReason());

    ErrorReasonDTO errorReasonHttpStatus = domainException.getErrorReasonHttpStatus();
    return handleExceptionInternal(domainException, errorReasonHttpStatus, null, request);
  }

  @ExceptionHandler(value = GeneralException.class)
  public ResponseEntity<Object> onThrowException(GeneralException generalException,
      HttpServletRequest request) {
    ErrorReasonDTO errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();
    return handleExceptionInternal(generalException, errorReasonHttpStatus, null, request);
  }

  private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorReasonDTO reason,
      HttpHeaders headers, HttpServletRequest request) {

    ApiResponse<Object> body = ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null);

    WebRequest webRequest = new ServletWebRequest(request);
    return super.handleExceptionInternal(
        e,
        body,
        headers,
        reason.getHttpStatus(),
        webRequest
    );
  }

  /**
   * 통합 에러 응답 빌더
   */
  private ResponseEntity<Object> buildErrorResponse(
      Exception e,
      ErrorStatus errorStatus,
      WebRequest request,
      Object errorDetail) {

    ApiResponse<Object> body = ApiResponse.onFailure(
        errorStatus.getCode(),
        errorStatus.getMessage(),
        errorDetail
    );

    return super.handleExceptionInternal(
        e,
        body,
        HttpHeaders.EMPTY,
        errorStatus.getHttpStatus(),
        request
    );
  }

  private ResponseEntity<Object> handleExceptionInternalFalse(Exception e,
      ErrorStatus errorCommonStatus,
      HttpHeaders headers, HttpStatus status, WebRequest request, String errorPoint) {
    ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(),
        errorCommonStatus.getMessage(), errorPoint);
    return super.handleExceptionInternal(
        e,
        body,
        headers,
        status,
        request
    );
  }

  private ResponseEntity<Object> handleExceptionInternalArgs(Exception e, HttpHeaders headers,
      ErrorStatus errorCommonStatus,
      WebRequest request, Map<String, String> errorArgs) {
    ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(),
        errorCommonStatus.getMessage(), errorArgs);
    return super.handleExceptionInternal(
        e,
        body,
        headers,
        errorCommonStatus.getHttpStatus(),
        request
    );
  }

  /**
   * 프로덕션 환경 여부 확인
   */
  private boolean isProductionProfile() {
    return "prod".equalsIgnoreCase(activeProfile);
  }
}
