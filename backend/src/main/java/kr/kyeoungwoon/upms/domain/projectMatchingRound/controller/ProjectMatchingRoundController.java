package kr.kyeoungwoon.upms.domain.projectMatchingRound.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.dto.ProjectMatchingRoundDto;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.service.ProjectMatchingRoundService;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import kr.kyeoungwoon.upms.security.annotation.ChapterLeadOnly;
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

@Tag(name = "프로젝트 매칭 차수", description = "Project Matching Rounds")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/matching-rounds")
public class ProjectMatchingRoundController {

  private final ProjectMatchingRoundService projectMatchingRoundService;

  @ChapterLeadOnly
  @Operation(summary = "매칭 라운드 생성", description = "새로운 프로젝트 매칭 라운드를 생성합니다")
  @PostMapping
  public ApiResponse<ProjectMatchingRoundDto.Response> createMatchingRound(
      @RequestBody ProjectMatchingRoundDto.CreateRequest request) {
    return ApiResponse.onSuccess(projectMatchingRoundService.create(request));
  }

  @Operation(summary = "매칭 라운드 조회", description = "ID로 매칭 라운드를 조회합니다")
  @GetMapping("/{id}")
  public ApiResponse<ProjectMatchingRoundDto.Response> getMatchingRound(@PathVariable Long id) {
    return ApiResponse.onSuccess(projectMatchingRoundService.findById(id));
  }

  @Operation(summary = "매칭 라운드 목록 조회", description = "전체 매칭 라운드 목록을 조회합니다")
  @GetMapping
  public ApiResponse<List<ProjectMatchingRoundDto.Response>> getMatchingRounds(
      @io.swagger.v3.oas.annotations.Parameter(description = "챕터 ID", required = true, example = "1")
      @RequestParam(required = false) Long chapterId,
      @io.swagger.v3.oas.annotations.Parameter(description = "검색 시작 시간 (ISO 8601)", required = true, example = "2025-01-01T00:00:00Z")
      @RequestParam(required = false) Instant startTime,
      @io.swagger.v3.oas.annotations.Parameter(description = "검색 종료 시간 (ISO 8601)", required = true, example = "2025-12-31T23:59:59Z")
      @RequestParam(required = false) Instant endTime) {

    if (chapterId != null && startTime != null && endTime != null) {
      return ApiResponse.onSuccess(
          projectMatchingRoundService.findByChapterAndTimeBetween(chapterId, startTime, endTime));
    }

    return ApiResponse.onSuccess(projectMatchingRoundService.findAll());
  }

  @Operation(
      summary = "기간별 매칭 라운드 목록 조회",
      description = "특정 Chapter의 특정 기간 내에 있는 매칭 라운드 목록을 조회합니다. "
          + "매칭 라운드의 시작 시간이나 종료 시간이 검색 기간과 겹치는 모든 라운드를 반환합니다."
  )
  @GetMapping("/by-time-range")
  public ApiResponse<List<ProjectMatchingRoundDto.Response>> getMatchingRoundsByTimeRange(
      @io.swagger.v3.oas.annotations.Parameter(description = "챕터 ID", required = true, example = "1")
      @RequestParam Long chapterId,
      @io.swagger.v3.oas.annotations.Parameter(description = "검색 시작 시간 (ISO 8601)", required = true, example = "2025-01-01T00:00:00Z")
      @RequestParam java.time.Instant startTime,
      @io.swagger.v3.oas.annotations.Parameter(description = "검색 종료 시간 (ISO 8601)", required = true, example = "2025-12-31T23:59:59Z")
      @RequestParam java.time.Instant endTime) {
    return ApiResponse.onSuccess(
        projectMatchingRoundService.findByChapterAndTimeBetween(chapterId, startTime, endTime));
  }

  @Operation(
      summary = "현재 또는 다가오는 매칭 라운드 조회",
      description = "특정 Chapter의 현재 진행 중이거나 가장 가까운 미래의 매칭 라운드를 조회합니다. "
          + "1순위: 현재 진행 중인 라운드, 2순위: 아직 시작하지 않은 가장 가까운 라운드")
  @GetMapping("/current-or-closest")
  public ApiResponse<ProjectMatchingRoundDto.Response> getCurrentOrUpcomingMatchingRound(
      @io.swagger.v3.oas.annotations.Parameter(description = "챕터 ID", required = true, example = "1")
      @RequestParam Long chapterId) {
    return ApiResponse.onSuccess(projectMatchingRoundService.findCurrentOrUpcoming(chapterId));
  }

  @Operation(
      summary = "현재 매칭 라운드 조회",
      description = "특정 지부에서 현재 진행되고 있는 라운드를 조회합니다."
  )
  @GetMapping("/current")
  public ApiResponse<ProjectMatchingRoundDto.Response> getCurrentMatchingRound(
      @io.swagger.v3.oas.annotations.Parameter(description = "챕터 ID", required = true, example = "1")
      @RequestParam Long chapterId) {
    return ApiResponse.onSuccess(projectMatchingRoundService.findCurrent(chapterId));
  }

  @Operation(summary = "매칭 라운드 수정", description = "매칭 라운드 정보를 수정합니다")
  @PutMapping("/{id}")
  public ApiResponse<ProjectMatchingRoundDto.Response> updateMatchingRound(
      @PathVariable Long id,
      @RequestBody ProjectMatchingRoundDto.UpdateRequest request) {
    return ApiResponse.onSuccess(projectMatchingRoundService.update(id, request));
  }

  @Operation(summary = "매칭 라운드 삭제", description = "매칭 라운드를 삭제합니다")
  @DeleteMapping("/{id}")
  public ApiResponse<Void> deleteMatchingRound(@PathVariable Long id) {
    projectMatchingRoundService.delete(id);
    return ApiResponse.onSuccess(null);
  }
}
