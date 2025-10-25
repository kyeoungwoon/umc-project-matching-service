package kr.kyeoungwoon.upms.domain.challenger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterDto;
import kr.kyeoungwoon.upms.domain.challenger.dto.SchoolDto;
import kr.kyeoungwoon.upms.domain.challenger.service.ChallengerService;
import kr.kyeoungwoon.upms.domain.challenger.service.ChapterService;
import kr.kyeoungwoon.upms.domain.challenger.service.SchoolService;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import kr.kyeoungwoon.upms.security.annotation.CentralAdminOnly;
import kr.kyeoungwoon.upms.security.annotation.Public;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UMC", description = "School & Chapter")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/umc")
public class UmcController {

  private final ChallengerService challengerService;
  private final SchoolService schoolService;
  private final ChapterService chapterService;

  @Public
  @Operation(summary = "학교 목록 조회", description = "전체 학교 목록을 조회합니다")
  @GetMapping("school")
  public ApiResponse<List<SchoolDto.Response>> getSchoolList() {
    return ApiResponse.onSuccess(schoolService.findAll());
  }

  @Public
  @Operation(summary = "지부 목록 조회", description = "전체 지부 목록을 조회합니다")
  @GetMapping("chapter")
  public ApiResponse<List<ChapterDto.Response>> getChapterList() {
    return ApiResponse.onSuccess(chapterService.findAll());
  }

  @CentralAdminOnly
  @Operation(summary = "지부 생성", description = "새로운 지부를 생성합니다")
  @PostMapping("chapter")
  public ApiResponse<ChapterDto.Response> createChapter(
      @RequestBody ChapterDto.CreateRequest body) {
    return ApiResponse.onSuccess(chapterService.create(body));
  }
}
