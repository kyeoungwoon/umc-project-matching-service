package kr.kyeoungwoon.upms.domain.challenger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import lombok.Builder;

public class SchoolDto {

  @Builder
  @Schema(name = "SchoolCreateRequest", description = "학교 생성 요청")
  public record CreateRequest(
      @Schema(description = "학교명", example = "중앙대학교")
      String name,
      @Schema(description = "학교 로고 이미지 URL", example = "https://example.com/logo.png")
      String logoImageUrl
  ) {

  }

  @Builder
  @Schema(name = "SchoolBulkCreateRequest", description = "학교 대량 생성 요청")
  public record BulkCreateRequest(
      @Schema(description = "생성할 학교 목록")
      List<CreateRequest> schools
  ) {

  }

  @Builder
  @Schema(name = "SchoolUpdateRequest", description = "학교 정보 수정 요청")
  public record UpdateRequest(
      @Schema(description = "학교명", example = "중앙대학교")
      String name,
      @Schema(description = "학교 로고 이미지 URL", example = "https://example.com/logo.png")
      String logoImageUrl
  ) {

  }

  @Builder
  @Schema(name = "SchoolResponse", description = "학교 정보 응답")
  public record Response(
      @Schema(description = "학교 ID", example = "1")
      Long id,
      @Schema(description = "학교명", example = "중앙대학교")
      String name,
      @Schema(description = "학교 로고 이미지 URL", example = "https://example.com/logo.png")
      String logoImageUrl,
      @Schema(description = "생성 일시")
      Instant createdAt,
      @Schema(description = "수정 일시")
      Instant updatedAt
  ) {

  }
}
