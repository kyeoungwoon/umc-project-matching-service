package kr.kyeoungwoon.upms.domain.challenger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Builder;

public class ChapterSchoolDto {

  @Builder
  @Schema(name = "ChapterSchoolCreateRequest", description = "챕터-학교 매핑 생성 요청")
  public record CreateRequest(
      @Schema(description = "챕터 ID", example = "1")
      Long chapterId,
      @Schema(description = "학교 ID", example = "1")
      Long schoolId,
      @Schema(description = "학교 리더(회장) ID", example = "1")
      Long leaderId,
      @Schema(description = "학교 부리더(부회장) ID", example = "2")
      Long viceLeaderId
  ) {

  }

  @Builder
  @Schema(name = "ChapterSchoolUpdateRequest", description = "챕터-학교 리더 수정 요청")
  public record UpdateRequest(
      @Schema(description = "학교 리더(회장) ID", example = "1")
      Long leaderId,
      @Schema(description = "학교 부리더(부회장) ID", example = "2")
      Long viceLeaderId
  ) {

  }

  @Builder
  @Schema(name = "ChapterSchoolResponse", description = "챕터-학교 정보 응답")
  public record Response(
      @Schema(description = "챕터-학교 매핑 ID", example = "1")
      Long id,
      @Schema(description = "챕터 ID", example = "1")
      Long chapterId,
      @Schema(description = "챕터명", example = "Ain")
      String chapterName,
      @Schema(description = "학교 ID", example = "1")
      Long schoolId,
      @Schema(description = "학교명", example = "중앙대학교")
      String schoolName,
      @Schema(description = "학교 리더(회장) ID", example = "1")
      Long leaderId,
      @Schema(description = "학교 리더(회장) 이름", example = "박경운")
      String leaderName,
      @Schema(description = "학교 부리더(부회장) ID", example = "2")
      Long viceLeaderId,
      @Schema(description = "학교 부리더(부회장) 이름", example = "이해승")
      String viceLeaderName,
      @Schema(description = "생성 일시")
      Instant createdAt,
      @Schema(description = "수정 일시")
      Instant updatedAt
  ) {

  }
}
