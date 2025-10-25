package kr.kyeoungwoon.upms.domain.challenger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterAdminDto;
import kr.kyeoungwoon.upms.domain.challenger.service.ChapterAdminService;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import kr.kyeoungwoon.upms.global.enums.ChapterAdminRole;
import kr.kyeoungwoon.upms.security.annotation.AdminOnly;
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

@Tag(name = "지부 운영진", description = "지부를 관리할 수 있는 운영진을 생성하고 관리합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/chapter-admins")
public class ChapterAdminController {

  private final ChapterAdminService chapterAdminService;

  @Operation(summary = "챕터 관리자 생성", description = "새로운 챕터 관리자를 생성합니다")
  @AdminOnly
  @PostMapping
  public ApiResponse<ChapterAdminDto.Response> createChapterAdmin(
      @RequestBody ChapterAdminDto.CreateRequest request) {
    return ApiResponse.onSuccess(chapterAdminService.create(request));
  }

  @Operation(summary = "챕터 관리자 조회", description = "ID로 챕터 관리자를 조회합니다")
  @GetMapping("/{id}")
  public ApiResponse<ChapterAdminDto.Response> getChapterAdmin(@PathVariable Long id) {
    return ApiResponse.onSuccess(chapterAdminService.findById(id));
  }

  @Operation(
      summary = "챕터 관리자 목록 조회",
      description = "챕터 관리자 목록을 조회합니다. chapterId 또는 challengerId로 필터링 가능합니다")
  @GetMapping
  public ApiResponse<List<ChapterAdminDto.Response>> getChapterAdmins(
      @io.swagger.v3.oas.annotations.Parameter(description = "챕터 ID (optional)", example = "1")
      @RequestParam(required = false) Long chapterId,
      @io.swagger.v3.oas.annotations.Parameter(description = "챌린저 ID (optional)", example = "1")
      @RequestParam(required = false) Long challengerId) {
    return ApiResponse.onSuccess(chapterAdminService.findAll(chapterId, challengerId));
  }

  @Operation(
      summary = "챌린저의 챕터 역할 조회",
      description = "특정 챌린저의 특정 챕터에서의 역할을 조회합니다. 권한이 없으면 role은 null입니다")
  @GetMapping("/role")
  public ApiResponse<ChapterAdminDto.RoleResponse> getRole(
      @io.swagger.v3.oas.annotations.Parameter(description = "챌린저 ID", required = true, example = "1")
      @RequestParam Long challengerId,
      @io.swagger.v3.oas.annotations.Parameter(description = "챕터 ID", required = true, example = "1")
      @RequestParam Long chapterId) {
    ChapterAdminRole role = chapterAdminService.getRoleByChallengerIdAndChapterId(challengerId,
        chapterId);

    ChapterAdminDto.RoleResponse response = ChapterAdminDto.RoleResponse.builder()
        .challengerId(challengerId)
        .chapterId(chapterId)
        .role(role)
        .build();

    return ApiResponse.onSuccess(response);
  }

  @Operation(summary = "챕터 관리자 수정", description = "챕터 관리자 정보를 수정합니다")
  @PutMapping("/{id}")
  public ApiResponse<ChapterAdminDto.Response> updateChapterAdmin(
      @PathVariable Long id,
      @RequestBody ChapterAdminDto.UpdateRequest request) {
    return ApiResponse.onSuccess(chapterAdminService.update(id, request));
  }

  @Operation(summary = "챕터 관리자 삭제", description = "챕터 관리자를 삭제합니다")
  @DeleteMapping("/{id}")
  public ApiResponse<Void> deleteChapterAdmin(@PathVariable Long id) {
    chapterAdminService.delete(id);
    return ApiResponse.onSuccess(null);
  }
}

