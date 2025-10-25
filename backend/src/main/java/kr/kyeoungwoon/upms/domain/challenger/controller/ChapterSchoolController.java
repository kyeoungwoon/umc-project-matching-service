package kr.kyeoungwoon.upms.domain.challenger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterSchoolDto;
import kr.kyeoungwoon.upms.domain.challenger.service.ChapterSchoolService;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import kr.kyeoungwoon.upms.security.annotation.CentralAdminOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "지부 내 학교", description = "챕터-학교 매핑 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/chapter-schools")
public class ChapterSchoolController {

  private final ChapterSchoolService chapterSchoolService;

  @CentralAdminOnly
  @Operation(summary = "챕터-학교 매핑 생성", description = "챕터와 학교를 매핑하고 리더를 지정합니다")
  @PostMapping
  public ApiResponse<ChapterSchoolDto.Response> createChapterSchool(
      @RequestBody ChapterSchoolDto.CreateRequest request) {
    return ApiResponse.onSuccess(chapterSchoolService.create(request));
  }

  @Operation(summary = "챕터-학교 매핑 조회", description = "ID로 챕터-학교 매핑 정보를 조회합니다")
  @GetMapping("/{id}")
  public ApiResponse<ChapterSchoolDto.Response> getChapterSchool(@PathVariable Long id) {
    return ApiResponse.onSuccess(chapterSchoolService.findById(id));
  }

  @Operation(
      summary = "챕터-학교 매핑 목록 조회",
      description = "챕터-학교 매핑 목록을 조회합니다. chapterId를 제공하면 해당 챕터에 속한 학교 목록만 조회합니다."
  )
  @GetMapping
  public ApiResponse<List<ChapterSchoolDto.Response>> getChapterSchools(
      @io.swagger.v3.oas.annotations.Parameter(
          description = "챕터 ID (optional) - 제공 시 해당 챕터에 속한 학교 목록만 조회",
          example = "1"
      )
      @RequestParam(required = false) Long chapterId
  ) {
    return ApiResponse.onSuccess(chapterSchoolService.findAll(chapterId));
  }

  @CentralAdminOnly
  @Operation(summary = "챕터-학교 리더 수정", description = "챕터-학교의 리더 정보를 수정합니다")
  @PutMapping("/{id}")
  public ApiResponse<ChapterSchoolDto.Response> updateChapterSchool(
      @PathVariable Long id,
      @RequestBody ChapterSchoolDto.UpdateRequest request) {
    return ApiResponse.onSuccess(chapterSchoolService.update(id, request));
  }

  @CentralAdminOnly
  @Operation(summary = "챕터-학교 매핑 삭제", description = "챕터-학교 매핑을 삭제합니다")
  @DeleteMapping("/{id}")
  public ApiResponse<Void> deleteChapterSchool(@PathVariable Long id) {
    chapterSchoolService.delete(id);
    return ApiResponse.onSuccess(null);
  }
}

