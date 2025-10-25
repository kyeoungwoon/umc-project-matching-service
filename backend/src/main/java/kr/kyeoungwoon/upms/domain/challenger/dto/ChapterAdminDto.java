package kr.kyeoungwoon.upms.domain.challenger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import kr.kyeoungwoon.upms.global.enums.ChapterAdminRole;
import lombok.Builder;

public class ChapterAdminDto {

  @Builder
  @Schema(name = "ChapterAdminCreateRequest", description = "챕터 관리자 생성 요청")
  public record CreateRequest(
      @Schema(description = "챕터 ID", example = "1")
      Long chapterId,
      @Schema(description = "챌린저 ID", example = "1")
      Long challengerId,
      @Schema(description = "역할", example = "ADMIN")
      ChapterAdminRole role
  ) {

  }

  @Builder
  @Schema(name = "ChapterAdminUpdateRequest", description = "챕터 관리자 수정 요청")
  public record UpdateRequest(
      @Schema(description = "역할", example = "SCHOOL_LEAD")
      ChapterAdminRole role
  ) {

  }

  @Builder
  @Schema(name = "ChapterAdminResponse", description = "챕터 관리자 정보 응답")
  public record Response(
      @Schema(description = "챕터 관리자 ID", example = "1")
      Long id,
      @Schema(description = "챕터 ID", example = "1")
      Long chapterId,
      @Schema(description = "챕터명", example = "Leo")
      String chapterName,
      @Schema(description = "챌린저 ID", example = "1")
      Long challengerId,
      @Schema(description = "챌린저 이름", example = "홍길동")
      String challengerName,
      @Schema(description = "챌린저 닉네임", example = "gildong")
      String challengerNickname,
      @Schema(description = "역할", example = "ADMIN")
      ChapterAdminRole role,
      @Schema(description = "생성 일시")
      Instant createdAt,
      @Schema(description = "수정 일시")
      Instant updatedAt
  ) {

  }

  @Builder
  @Schema(name = "ChapterAdminRoleResponse", description = "챕터 관리자 역할 응답")
  public record RoleResponse(
      @Schema(description = "챌린저 ID", example = "1")
      Long challengerId,
      @Schema(description = "챕터 ID", example = "1")
      Long chapterId,
      @Schema(description = "역할 (권한이 없으면 null)", example = "ADMIN")
      ChapterAdminRole role
  ) {

  }
}

