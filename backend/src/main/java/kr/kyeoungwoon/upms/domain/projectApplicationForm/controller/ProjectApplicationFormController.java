package kr.kyeoungwoon.upms.domain.projectApplicationForm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.kyeoungwoon.upms.domain.project.service.ProjectService;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.dto.ProjectApplicationFormDto;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.service.ProjectApplicationFormService;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import kr.kyeoungwoon.upms.security.UserPrincipal;
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

@Tag(name = "프로젝트 지원 폼", description = "")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/form")
public class ProjectApplicationFormController {

  private final ProjectApplicationFormService projectApplicationFormService;
  private final ProjectService projectService;

  @Operation(summary = "지원서 폼 생성", description = "새로운 프로젝트 지원서 폼을 생성합니다")
  @PostMapping
  public ApiResponse<ProjectApplicationFormDto.Response> createForm(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody ProjectApplicationFormDto.CreateRequest request) {

    Long projectChapterId = projectService.findById(request.projectId()).chapterId();

    projectService.throwIfChallengerNotProductOwnerOrAdmin(userPrincipal.challengerId(),
        projectChapterId, request.projectId());

    return ApiResponse.onSuccess(projectApplicationFormService.create(request));
  }

  @Operation(summary = "지원서 폼 조회", description = "ID로 지원서 폼을 조회합니다")
  @GetMapping("/{id}")
  public ApiResponse<ProjectApplicationFormDto.Response> getForm(@PathVariable Long id) {
    return ApiResponse.onSuccess(projectApplicationFormService.findById(id));
  }

  @Operation(
      summary = "지원서 폼 목록 조회",
      description = "제공된 Project ID에 따라서 해당 프로젝트의 폼만 조회합니다")
  @GetMapping
  public ApiResponse<List<ProjectApplicationFormDto.Response>> getForms(
      @io.swagger.v3.oas.annotations.Parameter(description = "프로젝트 ID", example = "1")
      @RequestParam Long projectId) {
    return ApiResponse.onSuccess(projectApplicationFormService.findAll(projectId));
  }

  @Operation(summary = "지원서 폼 수정", description = "지원서 폼 정보를 수정합니다")
  @PutMapping("/{id}")
  public ApiResponse<ProjectApplicationFormDto.Response> updateForm(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable Long id,
      @RequestBody ProjectApplicationFormDto.UpdateRequest request) {
    Long formProjectId = projectApplicationFormService.findById(id).projectId();
    Long formProjectChapterId = projectService.findById(formProjectId).chapterId();

    projectService.throwIfChallengerNotProductOwnerOrAdmin(userPrincipal.challengerId(),
        formProjectChapterId, formProjectId);

    return ApiResponse.onSuccess(projectApplicationFormService.update(id, request));
  }

  @Operation(summary = "지원서 폼 삭제", description = "지원서 폼을 삭제합니다")
  @DeleteMapping("/{id}")
  public ApiResponse<Void> deleteForm(@AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable Long id) {
    Long formProjectId = projectApplicationFormService.findById(id).projectId();
    Long formProjectChapterId = projectService.findById(formProjectId).chapterId();

    projectService.throwIfChallengerNotProductOwnerOrAdmin(userPrincipal.challengerId(),
        formProjectChapterId, formProjectId);

    projectApplicationFormService.delete(id);
    return ApiResponse.onSuccess(null);
  }
}
