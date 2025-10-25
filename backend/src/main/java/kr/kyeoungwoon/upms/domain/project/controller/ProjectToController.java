package kr.kyeoungwoon.upms.domain.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.kyeoungwoon.upms.domain.project.dto.ProjectToDto;
import kr.kyeoungwoon.upms.domain.project.service.ProjectService;
import kr.kyeoungwoon.upms.domain.project.service.ProjectToService;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
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

@Tag(name = "프로젝트 TO", description = "프로젝트 파트별 모집 인원 관련")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/project-to")
public class ProjectToController {

  private final ProjectToService projectToService;
  private final ProjectService projectService;

  @Operation(summary = "프로젝트 To 생성", description = "프로젝트의 파트별 모집 인원을 생성합니다")
  @PostMapping
  public ApiResponse<ProjectToDto.Response> createProjectTo(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody ProjectToDto.CreateRequest request) {

    projectService.throwIfChallengerNotProductOwnerOrAdmin(userPrincipal.challengerId(),
        projectService.findById(request.projectId()).chapterId(),
        request.projectId());

    return ApiResponse.onSuccess(projectToService.create(request));
  }

  @Operation(summary = "프로젝트 To 일괄 생성", description = "프로젝트의 파트별 모집 인원을 일괄 생성합니다")
  @PostMapping("/bulk")
  public ApiResponse<List<ProjectToDto.Response>> bulkCreateProjectTo(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody ProjectToDto.BulkCreateRequest request) {

    projectService.throwIfChallengerNotProductOwnerOrAdmin(userPrincipal.challengerId(),
        projectService.findById(request.projectId()).chapterId(),
        request.projectId());

    return ApiResponse.onSuccess(projectToService.bulkCreate(request));
  }

  @Operation(summary = "프로젝트 To 조회", description = "ID로 프로젝트 To를 조회합니다")
  @GetMapping("/{id}")
  public ApiResponse<ProjectToDto.Response> getProjectTo(@PathVariable Long id) {
    return ApiResponse.onSuccess(projectToService.findById(id));
  }

  @Operation(
      summary = "프로젝트 To 목록 조회",
      description = "프로젝트 To 목록을 조회합니다. projectId를 제공하면 해당 프로젝트의 To만 조회합니다")
  @GetMapping
  public ApiResponse<List<ProjectToDto.Response>> getProjectTos(
      @io.swagger.v3.oas.annotations.Parameter(description = "프로젝트 ID (optional)", example = "1")
      @RequestParam Long projectId) {
    return ApiResponse.onSuccess(projectToService.findAll(projectId));
  }

  @ChapterLeadOnly
  @Operation(summary = "프로젝트 To 수정", description = "프로젝트 To 정보를 수정합니다")
  @PutMapping("/{id}")
  public ApiResponse<ProjectToDto.Response> updateProjectTo(
      @PathVariable Long id,
      @RequestBody ProjectToDto.UpdateRequest request) {
    return ApiResponse.onSuccess(projectToService.update(id, request));
  }

  @ChapterLeadOnly
  @Operation(summary = "프로젝트 To 삭제", description = "프로젝트 To를 삭제합니다")
  @DeleteMapping("/{id}")
  public ApiResponse<Void> deleteProjectTo(@PathVariable Long id) {
    projectToService.delete(id);
    return ApiResponse.onSuccess(null);
  }
}

