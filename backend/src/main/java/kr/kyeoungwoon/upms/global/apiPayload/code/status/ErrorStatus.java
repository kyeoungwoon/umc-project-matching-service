package kr.kyeoungwoon.upms.global.apiPayload.code.status;

import kr.kyeoungwoon.upms.global.apiPayload.code.BaseErrorCode;
import kr.kyeoungwoon.upms.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

  // Common
  _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500",
      "예상하지 않은 오류입니다. 관리자에게 문의해주세요."),
  _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
  _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
  _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "허용되지 않는 요청입니다."),

  JWT_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT0001", "AccessToken이 만료되었습니다."),
  JWT_REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT0002", "RefreshToken이 만료되었습니다."),
  JWT_TOKEN_NOT_RECEIVED(HttpStatus.UNAUTHORIZED, "JWT0003", "JWT 토큰이 전달되지 않았습니다."),
  JWT_TOKEN_OUT_OF_FORM(HttpStatus.UNAUTHORIZED, "JWT0004", "JWT 토큰의 형식이 올바르지 않습니다."),

  LOGOUT_USER(HttpStatus.UNAUTHORIZED, "AUTH0004", "로그아웃된 유저입니다."),
  USER_LACK_PERMISSION(HttpStatus.FORBIDDEN, "AUTH0007", "해당 작업에 대한 권한이 없습니다."),

  // Challenger
  CHALLENGER_BAD_QUERY(HttpStatus.BAD_REQUEST, "CLGR0001", "챌린저 조회 조건이 올바르지 않습니다."),
  CHALLENGER_NOT_FOUND(HttpStatus.NOT_FOUND, "CLGR0002", "요청한 챌린저를 찾을 수 없습니다."),
  CHALLENGER_UMSB_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "CLGR0003",
      "해당 UMSB ID의 챌린저를 찾을 수 없습니다."),
  CHALLENGER_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "CLGR0004",
      "학번 또는 비밀번호가 일치하지 않습니다."),
  CHALLENGER_PREV_PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "CLGR0005",
      "현재 비밀번호가 일치하지 않습니다."),

  // School
  SCHOOL_NOT_FOUND(HttpStatus.NOT_FOUND, "SCL0001", "학교를 찾을 수 없습니다."),

  // Chapter / Chapter Affiliation
  CHAPTER_NOT_FOUND(HttpStatus.NOT_FOUND, "CTR0001", "챕터를 찾을 수 없습니다."),
  CHAPTER_FOR_CHALLENGER_NOT_FOUND(HttpStatus.NOT_FOUND, "CTR0002",
      "해당 챌린저의 챕터 정보를 찾을 수 없습니다."),
  CHAPTER_SCHOOL_NOT_FOUND(HttpStatus.NOT_FOUND, "CTRSCL0001", "챕터-학교 정보를 찾을 수 없습니다."),

  CHAPTER_ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "CTRADM0001", "챕터 관리자를 찾을 수 없습니다."),
  CHAPTER_LEADER_NOT_FOUND(HttpStatus.NOT_FOUND, "CTRADM0002", "지정한 리더를 찾을 수 없습니다."),
  CHAPTER_VICE_LEADER_NOT_FOUND(HttpStatus.NOT_FOUND, "CTRADM0003", "지정한 부리더를 찾을 수 없습니다."),

  // Project
  PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRJ0001", "프로젝트를 찾을 수 없습니다."),
  PROJECT_PRODUCT_OWNER_NOT_FOUND(HttpStatus.NOT_FOUND, "PRJ0002", "프로젝트의 제품 소유자를 찾을 수 없습니다."),
  PROJECT_CHAPTER_NOT_FOUND(HttpStatus.NOT_FOUND, "PRJ0003", "프로젝트의 챕터를 찾을 수 없습니다."),
  PROJECT_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "PRJ0004", "프로젝트 멤버를 찾을 수 없습니다."),
  PROJECT_TO_NOT_FOUND(HttpStatus.NOT_FOUND, "PRJ0005", "프로젝트 TO 정보를 찾을 수 없습니다."),
  PROJECT_EDIT_NOT_ALLOWED(HttpStatus.FORBIDDEN, "PRJ0006", "PO가 아닌 사용자의 프로젝트 수정 요청입니다."),
  PROJECT_CREATE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "PRJ0007",
      "생성하고자 하는 지부의 Plan 챌린저이거나, 운영진이여야 프로젝트를 생성할 수 있습니다."),

  // Project Application / Form / Response
  PROJECT_APPLICATION_FORM_NOT_FOUND(HttpStatus.NOT_FOUND, "PRJAPL0001",
      "프로젝트 지원서 폼을 찾을 수 없습니다."),
  PROJECT_APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "PRJAPL0002",
      "프로젝트 지원서를 찾을 수 없습니다."),
  PROJECT_APPLICATION_RESPONSE_NOT_FOUND(HttpStatus.NOT_FOUND, "PRJAPL0003",
      "프로젝트 지원서 응답을 찾을 수 없습니다."),
  PROJECT_APPLICATION_APPLICANT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRJAPL0004",
      "지원자를 찾을 수 없습니다."),
  APPLICATION_FORM_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "PRJAPL0005",
      "지원서 질문을 찾을 수 없습니다."),
  APPLICATION_PROJECT_TO_FULL(HttpStatus.BAD_REQUEST, "PRJAPL0006",
      "지원하는 프로젝트의 TO가 모두 찼습니다."),
  ALREADY_PROJECT_MEMBER(HttpStatus.CONFLICT, "PRJAPL0007",
      "이미 다른 프로젝트의 멤버입니다."),

  PROJECT_APPLICATION_SAME_STATUS_UPDATE(HttpStatus.BAD_REQUEST, "PAC0001",
      "변경 전 상태와 변경하고자 하는 상태가 동일합니다."),
  PROJECT_APPLICATION_STATUS_NOT_PENDING(HttpStatus.BAD_REQUEST, "PAC0002",
      "이미 합격 또는 불합격 처리한 지원서의 상태를 변경할 수 없습니다. 운영진에게 문의해주세요."),
  PROJECT_APPLICATION_STATUS_CHANGE_MATCHING_ROUND_NOT_ENDED(HttpStatus.BAD_REQUEST,
      "PAC0003",
      "지원서의 철회가 가능하기 때문에, 지원서가 제출된 매칭 차수가 끝난 후에 합격 및 불합격 처리가 가능합니다."),
  PROJECT_APPLICATION_STATUS_CHANGE_ALREADY_MEMBER(HttpStatus.BAD_REQUEST,
      "PAC0004",
      "이미 해당 프로젝트의 멤버인 지원서의 상태는 변경할 수 없습니다."),
  PA_STAT_CHANGE_WAS_NOT_MEMBER(HttpStatus.BAD_REQUEST,
      "PAC0005",
      "불합격 처리된 지원서는 프로젝트 멤버가 아니므로, 멤버 관련 처리를 할 수 없습니다."),
  PA_STAT_CHANGE_NO_PERMISSION(HttpStatus.FORBIDDEN,
      "PAC0006",
      "해당 지원서의 상태를 변경할 권한이 없습니다."),
  PA_ALREADY_APPLIED(HttpStatus.CONFLICT,
      "PAC0007",
      "동일 차수 내에 중복 지원은 불가능합니다. 기존 지원서를 삭제 후 다시 시도해주세요."),
  PA_CANNOT_UPDATE_STATUS_BEFORE_ROUND_END(HttpStatus.BAD_REQUEST,
      "PAC0008",
      "매칭 라운드가 종료되기 전에는 지원서 상태를 변경할 수 없습니다."),
  PA_CANNOT_EXCEED_TO_COUNT(HttpStatus.BAD_REQUEST,
      "PAC0009",
      "프로젝트의 TO 인원을 초과하여 합격 처리를 할 수 없습니다."),
  PA_REJECT_NEED_MIN_SELECTION(HttpStatus.FORBIDDEN, "PAC0010",
      "디자이너 지원자가 2명 이상일 때는 최소 1명을 선택해야 거절할 수 있습니다."),
  PA_REJECT_NEED_HALF_SELECTION(HttpStatus.FORBIDDEN, "PAC0011",
      "지원자가 TO 이상일 때는 TO의 50% 이상을 선택해야 거절할 수 있습니다."),
  PA_REJECT_NEED_QUARTER_SELECTION(HttpStatus.FORBIDDEN, "PAC0012",
      "지원자가 TO의 50% 초과일 때는 TO의 25% 이상을 선택해야 거절할 수 있습니다."),
  PA_DELETE_AFTER_MATCHING_ROUND_END_NOT_ALLOWED(HttpStatus.FORBIDDEN, "PAC0013",
      "매칭 라운드가 끝난 이후에는 지원서 철회가 불가능합니다."),


  // Matching Round
  MATCHING_ROUND_NOT_FOUND(HttpStatus.NOT_FOUND, "MR0001", "매칭 라운드를 찾을 수 없습니다."),
  MATCHING_ROUND_NOT_AVAILABLE_FOR_CHAPTER(HttpStatus.NOT_FOUND, "MR0002",
      "해당 챕터에는 현재 진행 중인 매칭 라운드가 없습니다."),
  MATCHING_ROUND_INVALID_SCHEDULE(HttpStatus.BAD_REQUEST, "MR0003",
      "매칭 라운드의 시작/종료 시간이 올바르지 않습니다."),
  MATCHING_ROUND_PERIOD_OVERLAP(HttpStatus.CONFLICT, "MR0004",
      "동일 챕터에 기간이 겹치는 매칭 라운드가 존재합니다."),

  // Infra - File
  FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "INFRA0001", "해당하는 파일이 존재하지 않습니다."),
  FILE_NOT_UPLOADED(HttpStatus.BAD_REQUEST, "INFRA0002", "S3에 파일이 업로드되지 않았습니다.");

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
