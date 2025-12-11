package kr.kyeoungwoon.upms.domain.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.kyeoungwoon.upms.domain.project.dto.ProjectDto;
import kr.kyeoungwoon.upms.domain.project.service.ProjectService;
import kr.kyeoungwoon.upms.domain.projectApplication.dto.ProjectApplicationDto;
import kr.kyeoungwoon.upms.domain.projectApplication.service.ProjectApplicationService;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;
import kr.kyeoungwoon.upms.security.UserPrincipal;
import kr.kyeoungwoon.upms.security.annotation.ChapterLeadOnly;
import lombok.RequiredArgsConstructor;
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

@Tag(name = "프로젝트", description = "프로젝트 관련")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/projects")
public class ProjectController {

  private final ProjectService projectService;
  private final ProjectApplicationService projectApplicationService;

  @Operation(summary = "프로젝트 생성", description = "새로운 프로젝트를 생성합니다")
  @PostMapping
  public ApiResponse<ProjectDto.Response> createProject(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody ProjectDto.CreateRequest request) {
    Long requestedChallengerId = userPrincipal.challengerId();

    projectService.throwIfChallengerNotPlanOrAdmin(requestedChallengerId, request.chapterId());

    return ApiResponse.onSuccess(projectService.create(request));
  }

  @Operation(summary = "프로젝트 조회", description = "ID로 프로젝트를 조회합니다")
  @GetMapping("/{id}")
  public ApiResponse<ProjectDto.Response> getProject(@PathVariable Long id) {
    return ApiResponse.onSuccess(projectService.findById(id));
  }

  @Operation(
      summary = "프로젝트 목록 조회",
      description = "Chapter ID를 받아서 해당 지부의 프로젝트 목록을 반환합니다.")
  @GetMapping
  public ApiResponse<List<ProjectDto.Response>> getProjects(
      @Parameter(description = "챕터 ID", example = "1")
      @RequestParam Long chapterId) {
    return ApiResponse.onSuccess(projectService.findAll(chapterId));
  }

  @Operation(summary = "프로젝트 수정", description = "프로젝트 정보를 수정합니다")
  @PutMapping("/{id}")
  public ApiResponse<ProjectDto.Response> updateProject(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable Long id,
      @RequestBody ProjectDto.UpdateRequest request) {

    projectService.throwIfChallengerNotProductOwnerOrAdmin(userPrincipal.challengerId(),
        projectService.findById(id).chapterId(), id);

    return ApiResponse.onSuccess(projectService.update(id, request));
  }

  @ChapterLeadOnly
  @Operation(summary = "프로젝트 삭제", description = "프로젝트를 삭제합니다. 지부 운영진만 가능합니다.")
  @DeleteMapping("/{id}")
  public ApiResponse<Void> deleteProject(@PathVariable Long id) {
    projectService.delete(id);
    return ApiResponse.onSuccess(null);
  }

  @Operation(summary = "최소 선발 인원 조회", description = "프로젝트에서 매칭 차수와 파트에 따라서 최소 선발 인원이 몇 명인지 조회합니다.")
  @GetMapping("/{projectId}/min-selection")
  public ApiResponse<ProjectApplicationDto.MinSelectionResponse> getMinSelectionInfo(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @Parameter(description = "챌린저 파트") @RequestParam ChallengerPart part,
      @Parameter(description = "매칭 차수 ID") @RequestParam Long matchingRoundId,
      @PathVariable Long projectId) {

    // 해당 프로젝트의 PO 또는 지부 운영진인지 확인
    projectService.throwIfChallengerNotProductOwnerOrAdmin(userPrincipal.challengerId(),
        projectService.findById(projectId).chapterId(), projectId);

    return ApiResponse.onSuccess(
        projectApplicationService.getMinSelectionInMatchingRound(projectId, part, matchingRoundId));
  }
}
