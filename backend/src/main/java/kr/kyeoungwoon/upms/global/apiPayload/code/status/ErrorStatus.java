package kr.kyeoungwoon.upms.global.apiPayload.code.status;

import kr.kyeoungwoon.upms.global.apiPayload.code.BaseErrorCode;
import kr.kyeoungwoon.upms.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

  // Error Code는 DOMAIN-CATEGORY-NUMBER 형식으로 작성할 것.
  // 에러 코드는 되도록 재사용 금지

  // Common
  _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-0001",
      "예상하지 않은 오류입니다. 관리자에게 문의해주세요."),
  _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON-400", "잘못된 요청입니다."),
  _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON-401", "인증이 필요합니다."),
  _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON-403", "허용되지 않는 요청입니다."),

  JWT_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT-0001", "만료된 Access Token 입니다."),
  JWT_REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT-0002", "만료된 Refresh Token 입니다."),
  JWT_TOKEN_NOT_RECEIVED(HttpStatus.UNAUTHORIZED, "JWT-0003", "JWT Token이 전달되지 않았습니다."),
  JWT_TOKEN_OUT_OF_FORM(HttpStatus.UNAUTHORIZED, "JWT-0004", "JWT Token의 형식이 올바르지 않습니다."),

  LOGOUT_USER(HttpStatus.UNAUTHORIZED, "AUTH-0001", "로그아웃한 사용자의 요청입니다."),
  USER_LACK_PERMISSION(HttpStatus.FORBIDDEN, "AUTH-0002", "해당 작업에 대한 권한이 없습니다."),

  // Challenger
  CHALLENGER_BAD_QUERY(HttpStatus.BAD_REQUEST, "CHALLENGER-0001", "챌린저 조회 조건이 올바르지 않습니다."),
  CHALLENGER_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGER-0002", "요청한 챌린저를 찾을 수 없습니다."),
  CHALLENGER_UMSB_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGER-0003",
      "해당 UMSB ID의 챌린저를 찾을 수 없습니다."),
  CHALLENGER_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "CHALLENGER-0004",
      "학번 또는 비밀번호가 일치하지 않습니다."),
  CHALLENGER_PREV_PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "CHALLENGER-0005",
      "현재 비밀번호가 일치하지 않습니다."),

  // School
  SCHOOL_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHOOL-0001", "학교를 찾을 수 없습니다."),

  // 지부
  CHAPTER_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAPTER-0001", "지부가 존재하지 않습니다."),
  CHAPTER_FOR_CHALLENGER_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAPTER-0002",
      "해당 챌린저의 지부 정보를 찾을 수 없습니다."),
  CHAPTER_SCHOOL_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAPTER-0003", "지부에 소속된 학교를 찾을 수 없습니다."),

  CHAPTER_ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAPTER-ADMIN-0001", "지부 운영진을 찾을 수 없습니다."),
  CHAPTER_LEADER_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAPTER-ADMIN-0002", "회장이 존재하지 않습니다."),
  CHAPTER_VICE_LEADER_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAPTER-ADMIN-0003", "부회장이 존재하지 않습니다."),

  // Project
  PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-0001", "프로젝트가 존재하지 않습니다."),
  PROJECT_PRODUCT_OWNER_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-0002",
      "Product Owner가 존재하지 않습니다."),
  PROJECT_CHAPTER_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-0003", "프로젝트가 속한 지부가 존재하지 않습니다."),
  PROJECT_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-0004", "프로젝트 멤버가 존재하지 않습니다."),
  PROJECT_TO_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-0005", "프로젝트 TO에 대한 정보를 찾을 수 없습니다."),
  PROJECT_EDIT_NOT_ALLOWED(HttpStatus.FORBIDDEN, "PROJECT-0006", "프로젝트 수정은 Product Owner만 가능합니다."),
  PROJECT_CREATE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "PROJECT-0007",
      "프로젝트 생성은 지부 내 Plan 챌린저 또는 운영진만 가능합니다."),
  PROJECT_NOT_IN_CHAPTER(HttpStatus.NOT_FOUND, "PROJECT-0008",
      "프로젝트가 해당 지부에 속하지 않습니다."),

  // Project Application / Form / Response
  PROJECT_APPLICATION_FORM_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-APPLICATION-0001",
      "프로젝트 지원 폼을 찾을 수 없습니다."),
  PROJECT_APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-APPLICATION-0002",
      "프로젝트 지원서를 찾을 수 없습니다."),
  PROJECT_APPLICATION_RESPONSE_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-APPLICATION-0003",
      "프로젝트 지원서의 응답을 찾을 수 없습니다."),
  PROJECT_APPLICATION_APPLICANT_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-APPLICATION-0004",
      "지원자를 찾을 수 없습니다."),
  APPLICATION_FORM_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-APPLICATION-0005",
      "프로젝트 지원 폼의 질문을 찾을 수 없습니다."),
  APPLICATION_PROJECT_TO_FULL(HttpStatus.BAD_REQUEST, "PROJECT-APPLICATION-0006",
      "지원하는 프로젝트의 TO 여석이 존재하지 않습니다."),
  ALREADY_PROJECT_MEMBER(HttpStatus.CONFLICT, "PROJECT-APPLICATION-0007",
      "이미 프로젝트의 멤버로 속해있습니다."),
  PROJECT_APPLICATION_SAME_STATUS_UPDATE(HttpStatus.BAD_REQUEST, "PROJECT-APPLICATION-0008",
      "지원서의 변경 전 상태와 변경하고자 하는 상태가 동일합니다."),
  PROJECT_APPLICATION_STATUS_NOT_PENDING(HttpStatus.BAD_REQUEST, "PROJECT-APPLICATION-0009",
      "합/불 처리 이후에는 지원서 상태 변경이 불가능합니다. 운영진에게 문의해주세요."),
  PROJECT_APPLICATION_STATUS_CHANGE_MATCHING_ROUND_NOT_ENDED(HttpStatus.BAD_REQUEST,
      "PROJECT-APPLICATION-0010", "합/불 처리는 지원서가 제출된 매칭 차수가 종료된 이후에 가능합니다."),
  PROJECT_APPLICATION_STATUS_CHANGE_ALREADY_MEMBER(HttpStatus.BAD_REQUEST,
      "PROJECT-APPLICATION-0011", "이미 프로젝트 멤버인 사용자의 지원서를 합격 처리할 수 없습니다."),
  PA_STAT_CHANGE_WAS_NOT_MEMBER(HttpStatus.BAD_REQUEST,
      "PROJECT-APPLICATION-0012", "프로젝트에 합격했던 지원자가 프로젝트의 멤버가 아닙니다. UPMS 운영진에게 문의해주세요."),
  PA_STAT_CHANGE_NO_PERMISSION(HttpStatus.FORBIDDEN,
      "PROJECT-APPLICATION-0013", "지원서의 상태를 변경할 권힌이 없습니다."),
  PA_ALREADY_APPLIED(HttpStatus.CONFLICT,
      "PROJECT-APPLICATION-0014",
      "동일한 매칭 차수 내에 중복 지원은 불가능합니다. 기존 지원서를 삭제 후 다시 시도해주세요."),
  PA_CANNOT_EXCEED_TO_COUNT(HttpStatus.BAD_REQUEST,
      "PROJECT-APPLICATION-0015", "프로젝트의 TO 여셕이 존재하지 않습니다."),
  PA_REJECT_NEED_MIN_SELECTION(HttpStatus.FORBIDDEN, "PROJECT-APPLICATION-0016",
      "디자인 파트 지원자가 2명 이상일 때는 최소 1명을 선택해야 거절할 수 있습니다."),
  PA_REJECT_NEED_HALF_SELECTION(HttpStatus.FORBIDDEN, "PROJECT-APPLICATION-0017",
      "지원자 수가 TO보다 많을 때는 TO의 50% 이상을 합격시킨 후에 불합격 처리를 할 수 있습니다."),
  PA_REJECT_NEED_QUARTER_SELECTION(HttpStatus.FORBIDDEN, "PROJECT-APPLICATION-0018",
      "지원자 수가 TO의 50%를 초과할 때는 TO의 25% 이상을 합격시킨 후에 불합격 처리를 할 수 있습니다."),
  PA_DELETE_AFTER_MATCHING_ROUND_END_NOT_ALLOWED(HttpStatus.FORBIDDEN, "PROJECT-APPLICATION-0019",
      "지원서 철회는 제출된 매칭 차수가 끝나기 전에만 가능합니다."),
  PA_STATUS_CHANGE_AFTER_DECISION_DEADLINE_NOT_ALLOWED(HttpStatus.FORBIDDEN,
      "PROJECT-APPLICATION-0020",
      "합/불 결정 기간이 경과한 이후에는 지원서 상태 변경이 불가능합니다."),


  // Matching Round
  MATCHING_ROUND_NOT_FOUND(HttpStatus.NOT_FOUND, "MATCHING-ROUND-0001", "매칭 차수가 존재하지 않습니다."),
  MATCHING_ROUND_NOT_AVAILABLE_FOR_CHAPTER(HttpStatus.NOT_FOUND, "MATCHING-ROUND-0002",
      "지부 내에 진행 중인 매칭 차수가 존재하지 않습니다."),
  MATCHING_ROUND_INVALID_SCHEDULE(HttpStatus.BAD_REQUEST, "MATCHING-ROUND-0003",
      "매칭 차수의 일정이 올바르지 않습니다."),
  MATCHING_ROUND_PERIOD_OVERLAP(HttpStatus.CONFLICT, "MATCHING-ROUND-0004",
      "매칭 차수는 서로 겹칠 수 없습니다."),

  // Infra - File
  FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "INFRA-0001", "파일이 존재하지 않습니다."),
  FILE_NOT_UPLOADED(HttpStatus.BAD_REQUEST, "INFRA-0002", "아직 파일이 업로드되지 않았습니다."),

  // ADMIN
  ADMIN_NOT_YOUR_CHAPTER(HttpStatus.FORBIDDEN, "ADMIN-0001", "해당 지부의 운영진이 아닙니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  @Override
  public ErrorReasonDTO getReason() {
    return ErrorReasonDTO.builder()
        .message(message)
        .code(code)
        .isSuccess(false)
        .build();
  }

  @Override
  public ErrorReasonDTO getReasonHttpStatus() {
    return ErrorReasonDTO.builder()
        .message(message)
        .code(code)
        .isSuccess(false)
        .httpStatus(httpStatus)
        .build();
  }
}
