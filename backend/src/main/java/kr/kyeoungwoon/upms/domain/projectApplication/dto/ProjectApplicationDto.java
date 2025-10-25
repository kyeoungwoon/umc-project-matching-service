package kr.kyeoungwoon.upms.domain.projectApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import kr.kyeoungwoon.upms.global.enums.ApplicationStatus;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;
import lombok.Builder;

public class ProjectApplicationDto {

  @Builder
  @Schema(name = "ProjectApplicationSubmitRequest", description = "프로젝트 지원서 제출 요청 (사용자용)")
  public record SubmitRequest(
      @Schema(description = "지원서 폼 ID", example = "1")
      Long formId,
      @Schema(description = "지원서 응답 리스트")
      List<ProjectApplicationResponseDto.SingleQuestionResponse> responses
  ) {

  }

  @Builder
  @Schema(name = "ProjectApplicationCreateRequest", description = "프로젝트 지원서 제출 요청")
  public record CreateRequest(
      @Schema(description = "지원서 폼 ID", example = "1")
      Long formId,
      @Schema(description = "지원자 ID", example = "1")
      Long applicantId,
      @Schema(description = "매칭 라운드 ID", example = "1")
      Long matchingRoundId,
      @Schema(description = "지원 상태", example = "PENDING")
      ApplicationStatus status,
      @Schema(description = "지원서 응답 리스트")
      List<ProjectApplicationResponseDto.SingleQuestionResponse> responses
  ) {

  }

  @Builder
  @Schema(name = "ProjectApplicationUpdateRequest", description = "프로젝트 지원서 상태 수정 요청")
  public record UpdateRequest(
      @Schema(description = "지원 상태", example = "CONFIRMED")
      ApplicationStatus status
  ) {

  }

  @Builder
  @Schema(name = "ProjectApplicationResponse", description = "프로젝트 지원서 정보 응답")
  public record Response(
      @Schema(description = "지원서 ID", example = "1")
      Long id,
      @Schema(description = "지원서 폼 ID", example = "1")
      Long formId,
      @Schema(description = "지원서 폼 제목", example = "9기 UPMS 지원용 폼")
      String formTitle,
      @Schema(description = "프로젝트 ID", example = "1")
      Long projectId,
      @Schema(description = "프로젝트명", example = "북리플")
      String projectName,
      @Schema(description = "지원자 ID", example = "1")
      Long applicantId,
      @Schema(description = "지원자 이름", example = "정보운")
      String applicantName,
      @Schema(description = "지원자 닉네임", example = "보니")
      String applicantNickname,
      @Schema(description = "지원자 파트", example = "SPRINGBOOT")
      ChallengerPart applicantPart,
      @Schema(description = "지원자 학교 ID", example = "1")
      Long applicantSchoolId,
      @Schema(description = "지원자 학교명", example = "중앙대학교")
      String applicantSchoolName,
      @Schema(description = "매칭 라운드 ID", example = "1")
      Long matchingRoundId,
      @Schema(description = "매칭 라운드명", example = "7기 1차 매칭")
      String matchingRoundName,
      @Schema(description = "지원 상태", example = "PENDING")
      ApplicationStatus status,
      @Schema(description = "생성 일시")
      Instant createdAt,
      @Schema(description = "수정 일시")
      Instant updatedAt
  ) {

  }

  @Builder
  @Schema(name = "ProjectApplicationDetailResponse", description = "프로젝트 지원서 상세 정보 응답 (응답 포함)")
  public record DetailResponse(
      @Schema(description = "지원서 ID", example = "1")
      Long id,
      @Schema(description = "지원서 폼 ID", example = "1")
      Long formId,
      @Schema(description = "지원서 폼 제목", example = "7기 UMC 프로젝트 지원서")
      String formTitle,
      @Schema(description = "프로젝트 ID", example = "1")
      Long projectId,
      @Schema(description = "프로젝트명", example = "북리플")
      String projectName,
      @Schema(description = "지원자 ID", example = "1")
      Long applicantId,
      @Schema(description = "지원자 이름", example = "홍길동")
      String applicantName,
      @Schema(description = "지원자 닉네임", example = "gildong")
      String applicantNickname,
      @Schema(description = "지원자 파트", example = "SPRINGBOOT")
      ChallengerPart applicantPart,
      @Schema(description = "지원자 학교 ID", example = "1")
      Long applicantSchoolId,
      @Schema(description = "지원자 학교명", example = "중앙대학교")
      String applicantSchoolName,
      @Schema(description = "매칭 라운드 ID", example = "1")
      Long matchingRoundId,
      @Schema(description = "매칭 라운드명", example = "7기 1차 매칭")
      String matchingRoundName,
      @Schema(description = "지원 상태", example = "PENDING")
      ApplicationStatus status,
      @Schema(description = "응답 목록")
      List<ProjectApplicationResponseDto.Response> responses,
      @Schema(description = "생성 일시")
      Instant createdAt,
      @Schema(description = "수정 일시")
      Instant updatedAt
  ) {

  }

  @Builder
  @Schema(name = "ChallengerApplicationSummary", description = "챌린저별 지원 현황 요약")
  public record ChallengerApplicationSummary(
      @Schema(description = "챌린저 ID", example = "1")
      Long challengerId,
      @Schema(description = "챌린저 이름", example = "홍길동")
      String challengerName,
      @Schema(description = "챌린저 닉네임", example = "gildong")
      String challengerNickname,
      @Schema(description = "챌린저 파트", example = "SPRINGBOOT")
      ChallengerPart challengerPart,
      @Schema(description = "챌린저 학교 ID", example = "1")
      Long challengerSchoolId,
      @Schema(description = "챌린저 학교명", example = "중앙대학교")
      String challengerSchoolName,
      @Schema(description = "멤버로 속한 프로젝트")
      MemberProject memberProjects,
      @Schema(description = "매칭 라운드별 지원 내역")
      List<MatchingRoundApplications> matchingRoundApplications

  ) {

  }

  @Builder
  @Schema(name = "MatchingRoundApplications", description = "매칭 라운드별 지원 내역")
  public record MatchingRoundApplications(
      @Schema(description = "매칭 라운드 ID", example = "1")
      Long matchingRoundId,
      @Schema(description = "매칭 라운드명", example = "9기 1차 매칭")
      String matchingRoundName,
      @Schema(description = "시작 시간")
      Instant startAt,
      @Schema(description = "종료 시간")
      Instant endAt,
      @Schema(description = "해당 라운드에 제출한 지원서 (지원하지 않은 경우 null)")
      ApplicationBrief application
  ) {

  }

  @Builder
  @Schema(name = "ApplicationBrief", description = "지원서 간략 정보")
  public record ApplicationBrief(
      @Schema(description = "지원서 ID", example = "1")
      Long applicationId,
      @Schema(description = "프로젝트 ID", example = "1")
      Long projectId,
      @Schema(description = "프로젝트명", example = "북리플")
      String projectName,
      @Schema(description = "지원 상태", example = "PENDING")
      ApplicationStatus status,
      @Schema(description = "지원 일시")
      Instant appliedAt
  ) {

  }

  @Builder
  @Schema(name = "MemberProject", description = "멤버로 속한 프로젝트 정보")
  public record MemberProject(
      @Schema(description = "프로젝트 ID", example = "1")
      Long projectId,
      @Schema(description = "프로젝트명", example = "북리플")
      String projectName,
      @Schema(description = "프로젝트 설명", example = "독서를 기록하고 나누는 서비스")
      String projectDescription
  ) {

  }
}
