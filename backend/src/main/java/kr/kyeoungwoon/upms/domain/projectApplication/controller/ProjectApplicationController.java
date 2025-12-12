package kr.kyeoungwoon.upms.domain.projectApplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.service.ChallengerService;
import kr.kyeoungwoon.upms.domain.challenger.service.ChapterAdminService;
import kr.kyeoungwoon.upms.domain.challenger.service.ChapterService;
import kr.kyeoungwoon.upms.domain.project.service.ProjectMemberService;
import kr.kyeoungwoon.upms.domain.project.service.ProjectService;
import kr.kyeoungwoon.upms.domain.project.service.ProjectToService;
import kr.kyeoungwoon.upms.domain.projectApplication.dto.ProjectApplicationDto;
import kr.kyeoungwoon.upms.domain.projectApplication.service.ProjectApplicationService;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.service.ProjectApplicationFormService;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.service.ProjectMatchingRoundService;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import kr.kyeoungwoon.upms.global.apiPayload.exception.DomainException;
import kr.kyeoungwoon.upms.global.enums.ApplicationStatus;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;
import kr.kyeoungwoon.upms.security.UserPrincipal;
import kr.kyeoungwoon.upms.security.annotation.AdminOnly;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "프로젝트 지원", description = "프로젝트 지원서 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/application")
@Slf4j
public class ProjectApplicationController {

  private final ProjectApplicationService projectApplicationService;
  private final ChapterService chapterService;
  private final ProjectMatchingRoundService projectMatchingRoundService;
  private final ProjectService projectService;
  private final ProjectApplicationFormService projectApplicationFormService;
  private final ChapterAdminService chapterAdminService;
  private final ChallengerService challengerService;
  private final ProjectToService projectToService;
  private final ProjectMemberService projectMemberService;

  @Operation(summary = "지원서 제출", description = "프로젝트에 지원서를 제출합니다")
  @PostMapping
  public ApiResponse<ProjectApplicationDto.Response> createApplication(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody ProjectApplicationDto.SubmitRequest request) {

    log.info("지원서 제출 요청 - 폼 ID: {}, 신청자 챌린저 ID: {}", request.formId(),
        userPrincipal.challengerId());
    Long challengerChapterId = chapterService.findByChallengerId(userPrincipal.challengerId()).id();

    // 현재 매칭 라운드 조회 : 여기서도 없으면 터짐!
    Long currentMatchingRoundId = projectMatchingRoundService.findCurrent(
        challengerChapterId).id();

    Long applicationProjectId = projectApplicationFormService.findById(request.formId())
        .projectId();

    ChallengerPart applicantPart = challengerService.findById(userPrincipal.challengerId()).part();

    // 챌린저가 프로젝트가 속한 챕터의 멤버인지 확인
    projectService.throwIfProjectNotBelongsToChapter(applicationProjectId, challengerChapterId);
    // 지원하는 프로젝트에 TO가 없는 경우
    projectToService.throwIfProjectToFull(applicationProjectId, applicantPart);
    // 이미 다른 프로젝트의 멤버인 경우
    projectMemberService.throwIfAlreadyProjectMember(userPrincipal.challengerId());

    return ApiResponse.onSuccess(
        projectApplicationService.create(ProjectApplicationDto.CreateRequest.builder()
            .formId(request.formId())
            .applicantId(userPrincipal.challengerId())
            .matchingRoundId(currentMatchingRoundId)
            .status(ApplicationStatus.PENDING)
            .responses(request.responses())
            .build()
        ));
  }

  @Operation(summary = "지원서 조회", description = "ID로 지원서를 조회합니다")
  @GetMapping("/{id}")
  public ApiResponse<ProjectApplicationDto.Response> getApplication(
      @PathVariable Long id) {
    log.info("지원서 단건 조회 요청 - 지원서 ID: {}", id);
    return ApiResponse.onSuccess(projectApplicationService.findById(id));
  }

  @Operation(
      summary = "지원서 목록 조회",
      description = "지원서 목록을 조회합니다. 다음 조건에 따라 다르게 동작합니다:\n\n"
          + "1. 파라미터 없음: 전체 지원서 목록 조회\n"
          + "2. challengerId 제공: 해당 챌린저가 제출한 지원서를 응답과 함께 조회\n"
          + "3. projectId 제공: 해당 프로젝트의 지원서를 응답과 함께 조회\n"
          + "4. chapterId 제공: 해당 Chapter의 모든 프로젝트 지원서를 응답과 함께 조회\n"
          + "※ 우선순위: challengerId > projectId > chapterId"
  )
  @GetMapping
  public ApiResponse<?> getApplications(
      @io.swagger.v3.oas.annotations.Parameter(
          description = "챌린저 ID (optional) - 제공 시 해당 챌린저가 제출한 지원서를 응답과 함께 조회",
          example = "1"
      )
      @RequestParam(required = false) Long challengerId,
      @io.swagger.v3.oas.annotations.Parameter(
          description = "챕터 ID (optional) - 제공 시 해당 Chapter의 모든 프로젝트 지원서를 응답과 함께 조회",
          example = "1"
      )
      @RequestParam(required = false) Long chapterId,
      @io.swagger.v3.oas.annotations.Parameter(
          description = "프로젝트 ID (optional) - 제공 시 해당 프로젝트의 지원서를 응답과 함께 조회",
          example = "1"
      )
      @RequestParam(required = false) Long projectId
  ) {
    log.info("지원서 목록 조회 요청 - challengerId: {}, chapterId: {}, projectId: {}",
        challengerId, chapterId, projectId);
    // 1. challengerId가 있으면 챌린저별 조회 (우선순위 최고)
    if (challengerId != null) {
      return ApiResponse.onSuccess(projectApplicationService.findByChallengerId(challengerId));
    }

    // 2. projectId가 있으면 프로젝트별 조회
    if (projectId != null) {
      return ApiResponse.onSuccess(projectApplicationService.findByProjectId(projectId));
    }

    // 3. chapterId가 있으면 Chapter별 조회
    if (chapterId != null) {
      return ApiResponse.onSuccess(projectApplicationService.findByChapterId(chapterId));
    }

    // 4. 전체 목록 조회
    return ApiResponse.onSuccess(projectApplicationService.findAll());
  }

  @GetMapping("me")
  public ApiResponse<List<ProjectApplicationDto.DetailResponse>> getApplications(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    log.info("본인 지원서 목록 조회 요청 - 챌린저 ID: {}", userPrincipal.challengerId());
    return ApiResponse.onSuccess(projectApplicationService.findByChallengerId(
        userPrincipal.challengerId()));
  }

  @Operation(
      summary = "폼별 지원서 상세 목록 조회",
      description = "특정 폼에 제출된 모든 지원서를 응답과 함께 조회합니다")
  @GetMapping("/form/{formId}")
  public ApiResponse<List<ProjectApplicationDto.DetailResponse>> getApplicationsByForm(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable Long formId) {

    log.info("폼별 지원서 조회 요청 - 폼 ID: {}, 요청자 챌린저 ID: {}", formId,
        userPrincipal.challengerId());
    Long formProjectId = projectApplicationFormService.findById(formId).projectId();
    Long formChapterId = projectService.findById(formProjectId).chapterId();

    projectService.throwIfChallengerNotProductOwnerOrAdmin(userPrincipal.challengerId(),
        formChapterId, formProjectId);

    return ApiResponse.onSuccess(projectApplicationService.findByFormId(formId));
  }

  @Operation(
      summary = "Chapter별 챌린저 지원 현황 조회",
      description = "특정 Chapter의 모든 챌린저가 각 매칭 라운드에 어떤 프로젝트에 지원했는지 조회합니다. "
          + "각 챌린저별로 모든 매칭 라운드 정보와 해당 라운드에 제출한 지원서 목록을 반환합니다. "
          + "지원하지 않은 라운드는 빈 배열로 표시됩니다."
  )
  @AdminOnly
  @GetMapping("/chapter/{chapterId}/summary")
  public ApiResponse<List<ProjectApplicationDto.ChallengerApplicationSummary>> getChapterApplicationSummary(
      @PathVariable Long chapterId) {
    log.info("챕터 지원 현황 요약 조회 요청 - 챕터 ID: {}", chapterId);
    return ApiResponse.onSuccess(
        projectApplicationService.findChapterApplicationSummary(chapterId));
  }

  @Operation(summary = "지원서 상태 수정", description = "지원서 상태를 수정합니다")
  @PutMapping("/{id}")
  public ApiResponse<ProjectApplicationDto.Response> updateApplication(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable Long id,
      @RequestBody ProjectApplicationDto.UpdateRequest request) {
    log.info("지원서 상태 수정 요청 - 지원서 ID: {}, 요청자 챌린저 ID: {}", id,
        userPrincipal.challengerId());
    return ApiResponse.onSuccess(projectApplicationService.update(id, request, userPrincipal));
  }

  @Operation(summary = "지원서 삭제", description = "지원서를 삭제합니다")
  @DeleteMapping("/{id}")
  public ApiResponse<Void> deleteApplication(@AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable Long id) {
    log.info("지원서 삭제 요청 - 지원서 ID: {}, 요청자 챌린저 ID: {}", id, userPrincipal.challengerId());
    ProjectApplicationDto.Response application = projectApplicationService.findById(id);
    Long applicationProjectId = application.projectId();
    Long applicationChapterId = projectService.findById(applicationProjectId).chapterId();

    boolean isChapterAdmin = chapterAdminService.isChallengerChapterAdmin(
        userPrincipal.challengerId(),
        applicationChapterId);

    if (!isChapterAdmin) {
      Instant matchingRoundEnd = projectMatchingRoundService.findById(application.matchingRoundId())
          .endAt();
      if (Instant.now().isAfter(matchingRoundEnd)) {
        throw new DomainException(DomainType.PROJECT_APPLICATION,
            ErrorStatus.PA_DELETE_AFTER_MATCHING_ROUND_END_NOT_ALLOWED);
      }
    }

    projectApplicationService.delete(id);
    return ApiResponse.onSuccess(null);
  }
}
