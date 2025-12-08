package kr.kyeoungwoon.upms.domain.challenger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChallengerDto;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterDto;
import kr.kyeoungwoon.upms.domain.challenger.service.ChallengerService;
import kr.kyeoungwoon.upms.domain.challenger.service.ChapterService;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import kr.kyeoungwoon.upms.global.apiPayload.exception.DomainException;
import kr.kyeoungwoon.upms.security.UserPrincipal;
import kr.kyeoungwoon.upms.security.annotation.CentralAdminOnly;
import kr.kyeoungwoon.upms.security.annotation.Public;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "챌린저", description = "UMC Challengers")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/challenger")
public class ChallengerController {

  private final ChallengerService challengerService;
  private final ChapterService chapterService;

  @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 정보를 조회합니다")
  @GetMapping("/me")
  public ApiResponse<ChallengerDto.Response> me(@AuthenticationPrincipal UserPrincipal principal) {
    return ApiResponse.onSuccess(challengerService.findById(principal.challengerId()));
  }

  @Public
  @Operation(
      summary = "챌린저 조회",
      description = "챌린저 ID 또는 UMSB ID로 챌린저 공개 정보를 조회합니다 (학번 등 민감 정보 제외, 둘 중 하나는 필수)")
  @GetMapping
  public ApiResponse<ChallengerDto.PublicResponse> getChallengerById(
      @Parameter(description = "챌린저 ID", example = "1")
      @RequestParam(required = false) Long challengerId,
      @Parameter(description = "UMSB ID", example = "1")
      @RequestParam(required = false) Long umsbId
  ) {
    if (challengerId != null) {
      return ApiResponse.onSuccess(challengerService.findByIdPublic(challengerId));
    } else if (umsbId != null) {
      return ApiResponse.onSuccess(challengerService.findByUmsbIdPublic(umsbId));
    } else {
      throw new DomainException(DomainType.CHALLENGER, ErrorStatus.CHALLENGER_BAD_QUERY);
    }
  }

  @Public
  @Operation(
      summary = "챌린저의 챕터 조회",
      description = "챌린저 ID로 해당 챌린저가 속한 챕터를 조회합니다")
  @GetMapping("/chapter")
  public ApiResponse<ChapterDto.Response> getChapterByChallengerId(
      @Parameter(description = "챌린저 ID", required = true, example = "1")
      @RequestParam Long challengerId) {
    return ApiResponse.onSuccess(chapterService.findByChallengerId(challengerId));
  }

  @CentralAdminOnly
  @Operation(summary = "회원가입", description = "새로운 챌린저를 등록합니다")
  @Public
  @PostMapping("/register")
  public ApiResponse<ChallengerDto.Response> register(
      @RequestBody ChallengerDto.CreateRequest body) {
    return ApiResponse.onSuccess(challengerService.create(body));
  }

  //  @CentralAdminOnly
  @Profile("local | dev")
  @Operation(summary = "대량 회원가입", description = "여러 챌린저를 한 번에 등록합니다 (bulk insert)")
  @Public
  @PostMapping("/register/bulk")
  public ApiResponse<List<ChallengerDto.Response>> registerBulk(
      @RequestBody List<ChallengerDto.CreateRequest> body) {
    return ApiResponse.onSuccess(challengerService.createBulk(body));
  }

  @Operation(summary = "로그인", description = "학번, 학교, 기수, 비밀번호로 로그인하고 JWT 토큰을 발급받습니다")
  @Public
  @PostMapping("/login")
  public ApiResponse<ChallengerDto.LoginResponse> login(
      @RequestBody ChallengerDto.LoginRequest body) {
    return ApiResponse.onSuccess(challengerService.login(body));
  }

  @Operation(summary = "비밀번호 변경", description = "현재 비밀번호를 확인하고 새 비밀번호로 변경합니다")
  @PostMapping("/change-password")
  public ApiResponse<Void> changePassword(
      @AuthenticationPrincipal UserPrincipal principal,
      @RequestBody ChallengerDto.ChangePasswordRequest body) {
    challengerService.changePassword(principal.challengerId(), body);
    return ApiResponse.onSuccess(null);
  }
}
