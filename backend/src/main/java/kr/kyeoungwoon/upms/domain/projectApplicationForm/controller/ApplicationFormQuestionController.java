package kr.kyeoungwoon.upms.domain.projectApplicationForm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.dto.ApplicationFormQuestionDto;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.service.ApplicationFormQuestionService;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "프로젝트 지원 폼 질문", description = "폼에 속한 질문을 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/form-questions")
public class ApplicationFormQuestionController {

  private final ApplicationFormQuestionService applicationFormQuestionService;

//  @Operation(summary = "질문 생성", description = "지원서 폼에 새로운 질문을 생성합니다")
//  @PostMapping
//  public ApiResponse<ApplicationFormQuestionDto.Response> createQuestion(
//      @RequestBody ApplicationFormQuestionDto.CreateRequest request) {
//    return ApiResponse.onSuccess(applicationFormQuestionService.create(request));
//  }

  @Operation(summary = "질문 대량 생성", description = "지원서 폼에 여러 질문을 한 번에 생성합니다 (bulk insert)")
  @PostMapping("/bulk")
  public ApiResponse<List<ApplicationFormQuestionDto.Response>> createQuestionsBulk(
      @RequestBody ApplicationFormQuestionDto.BulkCreateRequest request) {
    return ApiResponse.onSuccess(applicationFormQuestionService.createBulk(request));
  }


  @Operation(summary = "질문 조회", description = "ID로 질문을 조회합니다")
  @GetMapping("/{id}")
  public ApiResponse<ApplicationFormQuestionDto.Response> getQuestion(@PathVariable Long id) {
    return ApiResponse.onSuccess(applicationFormQuestionService.findById(id));
  }

  @Operation(
      summary = "질문 목록 조회",
      description = "전체 질문 목록을 조회합니다. formId를 제공하면 해당 폼의 질문만 조회합니다 (질문 번호 순으로 정렬)")
  @GetMapping
  public ApiResponse<List<ApplicationFormQuestionDto.Response>> getQuestions(
      @io.swagger.v3.oas.annotations.Parameter(description = "Form ID", example = "1")
      @RequestParam Long formId) {
    return ApiResponse.onSuccess(applicationFormQuestionService.findAllByFormId(formId));
  }

//  @Operation(summary = "질문 수정", description = "질문 정보를 수정합니다")
//  @PutMapping("/{id}")
//  public ApiResponse<ApplicationFormQuestionDto.Response> updateQuestion(
//      @PathVariable Long id,
//      @RequestBody ApplicationFormQuestionDto.UpdateRequest request) {
//    return ApiResponse.onSuccess(applicationFormQuestionService.update(id, request));
//  }
//
//  @Operation(summary = "질문 삭제", description = "질문을 삭제합니다")
//  @DeleteMapping("/{id}")
//  public ApiResponse<Void> deleteQuestion(@PathVariable Long id) {
//    applicationFormQuestionService.delete(id);
//    return ApiResponse.onSuccess(null);
//  }
}
