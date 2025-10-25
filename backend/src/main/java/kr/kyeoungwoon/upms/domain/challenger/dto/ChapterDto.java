package kr.kyeoungwoon.upms.domain.challenger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import lombok.Builder;

public class ChapterDto {

  @Builder
  @Schema(name = "ChapterCreateRequest", description = "챕터 생성 요청")
  public record CreateRequest(
      @Schema(description = "챕터명", example = "Ain")
      String name,
      @Schema(description = "챕터 설명", example = "중앙대학교, 숭실대학교, 가천대학교, 한양대학교 ERICA, 명지대학교가 함께하는 Ain 지부입니다.")
      String description,
      @Schema(description = "기수", example = "7")
      Long gisu
  ) {

  }

  @Builder
  @Schema(name = "BulkChapterCreateRequest", description = "지부 대량 생성 요청")
  public record BulkCreateRequest(
      @Schema(description = "생성할 지부 목록")
      List<CreateRequest> chapters
  ) {

  }

  @Builder
  @Schema(name = "ChapterUpdateRequest", description = "챕터 정보 수정 요청")
  public record UpdateRequest(
      @Schema(description = "챕터명", example = "Ain")
      String name,
      @Schema(description = "챕터 설명", example = "중앙대학교, 숭실대학교, 가천대학교, 한양대학교 ERICA, 명지대학교가 함께하는 Ain 지부입니다.")
      String description
  ) {

  }

  @Builder
  @Schema(name = "ChapterResponse", description = "챕터 정보 응답")
  public record Response(
      @Schema(description = "챕터 ID", example = "1")
      Long id,
      @Schema(description = "챕터명", example = "Ain")
      String name,
      @Schema(description = "챕터 설명", example = "중앙대학교, 숭실대학교, 가천대학교, 한양대학교 ERICA, 명지대학교가 함께하는 Ain 지부입니다.")
      String description,
      @Schema(description = "기수", example = "8")
      Long gisu,
      @Schema(description = "소속 학교 목록")
      List<SchoolDto.Response> schools,
      @Schema(description = "생성 일시")
      Instant createdAt,
      @Schema(description = "수정 일시")
      Instant updatedAt
  ) {

  }
}
