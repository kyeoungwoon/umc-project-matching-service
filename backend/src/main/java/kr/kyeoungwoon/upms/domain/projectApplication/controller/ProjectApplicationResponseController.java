package kr.kyeoungwoon.upms.domain.projectApplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.kyeoungwoon.upms.domain.projectApplication.dto.ProjectApplicationResponseDto;
import kr.kyeoungwoon.upms.domain.projectApplication.service.ProjectApplicationResponseService;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "프로젝트 지원서 응답", description = "프로젝트 지원서 응답 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/application-responses")
public class ProjectApplicationResponseController {

  private final ProjectApplicationResponseService projectApplicationResponseService;

  @Operation(summary = "지원서 응답 생성", description = "지원서의 특정 질문에 대한 응답을 생성합니다")
  @PostMapping
  public ApiResponse<ProjectApplicationResponseDto.Response> createApplicationResponse(
      @RequestBody ProjectApplicationResponseDto.CreateRequest request) {
    return ApiResponse.onSuccess(projectApplicationResponseService.create(request));
  }

  @Operation(summary = "지원서 응답 일괄 생성", description = "지원서의 여러 질문에 대한 응답을 한 번에 생성합니다")
  @PostMapping("/bulk")
  public ApiResponse<List<ProjectApplicationResponseDto.Response>> bulkCreateApplicationResponses(
      @RequestBody ProjectApplicationResponseDto.BulkCreateRequest request) {
    return ApiResponse.onSuccess(projectApplicationResponseService.bulkCreate(request));
  }

  @Operation(summary = "지원서 응답 조회", description = "ID로 지원서 응답을 조회합니다")
  @GetMapping("/{id}")
  public ApiResponse<ProjectApplicationResponseDto.Response> getApplicationResponse(
      @PathVariable Long id) {
    return ApiResponse.onSuccess(projectApplicationResponseService.findById(id));
  }

  @Operation(
      summary = "지원서 응답 목록 조회",
      description = "지원서 응답 목록을 조회합니다. applicationId를 제공하면 해당 지원서의 응답만 조회합니다")
  @GetMapping
  public ApiResponse<List<ProjectApplicationResponseDto.Response>> getApplicationResponses(
      @io.swagger.v3.oas.annotations.Parameter(description = "지원서 ID (optional)", example = "1")
      @RequestParam(required = false) Long applicationId) {
    return ApiResponse.onSuccess(projectApplicationResponseService.findAll(applicationId));
  }

//  @Operation(summary = "지원서 응답 수정", description = "지원서 응답 정보를 수정합니다")
//  @PutMapping("/{id}")
//  public ApiResponse<ProjectApplicationResponseDto.Response> updateApplicationResponse(
//      @PathVariable Long id,
//      @RequestBody ProjectApplicationResponseDto.UpdateRequest request) {
//    return ApiResponse.onSuccess(projectApplicationResponseService.update(id, request));
//  }

  @Operation(summary = "지원서 응답 삭제", description = "지원서 응답을 삭제합니다")
  @DeleteMapping("/{id}")
  public ApiResponse<Void> deleteApplicationResponse(@PathVariable Long id) {
    projectApplicationResponseService.delete(id);
    return ApiResponse.onSuccess(null);
  }
}

