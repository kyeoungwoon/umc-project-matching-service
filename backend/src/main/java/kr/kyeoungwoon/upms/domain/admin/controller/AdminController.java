package kr.kyeoungwoon.upms.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChallengerDto;
import kr.kyeoungwoon.upms.domain.challenger.service.ChallengerService;
import kr.kyeoungwoon.upms.domain.project.dto.ProjectMemberDto;
import kr.kyeoungwoon.upms.domain.project.service.ProjectMemberService;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import kr.kyeoungwoon.upms.security.annotation.ChapterLeadOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자", description = "관리자 기능입니다.")
@ChapterLeadOnly
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class AdminController {

  private final ChallengerService challengerService;
  private final ProjectMemberService projectMemberService;

  @Operation(summary = "이름으로 챌린저 검색")
  @GetMapping("/challenger")
  public ApiResponse<List<ChallengerDto.Response>> searchChallengerByName(
      @Parameter(description = "검색할 챌린저 이름", example = "경운")
      @RequestParam String name) {
    return ApiResponse.onSuccess(challengerService.findChallengerByName(name));
  }

  @Operation(summary = "관리자 권한으로 프로젝트 멤버 추가")
  @PostMapping("/project-member")
  public ApiResponse<ProjectMemberDto.Response> addProjectMember(
      @RequestBody ProjectMemberDto.CreateRequest request) {
    return ApiResponse.onSuccess(projectMemberService.create(request));
  }

  // 프로젝트 멤버 삭제도 구현, UI 고민해봅시다!
  @Operation(summary = "관리자 권한으로 프로젝트 멤버 삭제")
  @DeleteMapping("/project-member")
  public ApiResponse<String> deleteProjectMember(
      @RequestBody Long projectMemberId) {

    projectMemberService.delete(projectMemberId);

    return ApiResponse.onSuccess("관리자 권한으로 프로젝트 멤버를 삭제하였습니다.");
  }

  // 프로젝트 추가 UI도 변경
}
