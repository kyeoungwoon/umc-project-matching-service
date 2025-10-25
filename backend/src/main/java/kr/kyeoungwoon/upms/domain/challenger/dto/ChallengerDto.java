package kr.kyeoungwoon.upms.domain.challenger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;
import kr.kyeoungwoon.upms.global.enums.Gender;
import lombok.Builder;

public class ChallengerDto {

  @Builder
  @Schema(name = "ChallengerCreateRequest", description = "챌린저 회원가입 요청")
  public record CreateRequest(
      Long umsbId,
      Long gisu,
      ChallengerPart part,
      String name,
      String nickname,
      Gender gender,
      Long schoolId,
      String studentId,
      String password,
      String profileImageUrl
  ) {

  }

  @Builder
  @Schema(name = "ChallengerUpdateRequest", description = "챌린저 정보 수정 요청")
  public record UpdateRequest(
      String name,
      String nickname,
      Gender gender,
      ChallengerPart part,
      String profileImageUrl,
      String password
  ) {

  }

  @Builder
  @Schema(name = "ChallengerResponse", description = "챌린저 정보 응답 (전체 정보)")
  public record Response(
      Long id,
      Long umsbId,
      Long gisu,
      ChallengerPart part,
      String name,
      String nickname,
      Gender gender,
      Long schoolId,
      String schoolName,
      @Schema(description = "학교 로고 이미지 URL")
      String schoolLogoImageUrl,
      String studentId,
      String profileImageUrl,
      @Schema(description = "챕터 ID")
      Long chapterId,
      @Schema(description = "챕터명")
      String chapterName,
      Instant createdAt,
      Instant updatedAt
  ) {

  }

  @Builder
  @Schema(name = "ChallengerPublicResponse", description = "챌린저 공개 정보 응답 (민감 정보 제외)")
  public record PublicResponse(
      Long id,
      Long umsbId,
      Long gisu,
      ChallengerPart part,
      String name,
      String nickname,
      Gender gender,
      Long schoolId,
      String schoolName,
      @Schema(description = "학교 로고 이미지 URL")
      String schoolLogoImageUrl,
      // studentId 제외 (민감 정보)
      String profileImageUrl,
      @Schema(description = "챕터 ID")
      Long chapterId,
      @Schema(description = "챕터명")
      String chapterName,
      Instant createdAt,
      Instant updatedAt
  ) {

  }

  @Builder
  @Schema(name = "ChallengerLoginRequest", description = "챌린저 로그인 요청")
  public record LoginRequest(
      String studentId,
      Long schoolId,
      Long gisu,
      String password
  ) {

  }

  @Builder
  @Schema(name = "ChallengerLoginResponse", description = "챌린저 로그인 응답")
  public record LoginResponse(
      String accessToken,
      Response challengerInfo
  ) {

  }

  @Builder
  @Schema(name = "ChallengerChangePasswordRequest", description = "챌린저 비밀번호 변경 요청")
  public record ChangePasswordRequest(
      @Schema(description = "현재 비밀번호", example = "oldPassword123")
      String currentPassword,
      @Schema(description = "새 비밀번호", example = "newPassword123")
      String newPassword
  ) {

  }
}
