package kr.kyeoungwoon.upms.domain.projectMatchingRound.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Builder;

public class ProjectMatchingRoundDto {

  @Builder
  @Schema(name = "ProjectMatchingRoundCreateRequest", description = "프로젝트 매칭 라운드 생성 요청")
  public record CreateRequest(
      @Schema(description = "매칭 라운드명", example = "9th Leo 기획-디자인 1차 매칭")
      String name,
      @Schema(description = "매칭 라운드 설명", example = "Leo 지부의 기획-디자인 1차 매칭 기간입니다.")
      String description,
      @Schema(description = "챕터 ID", example = "1")
      Long chapterId,
      @Schema(description = "시작 일시", example = "2024-01-01T00:00:00Z")
      Instant startAt,
      @Schema(description = "종료 일시", example = "2024-01-31T23:59:59Z")
      Instant endAt,
      @Schema(description = "합/불 결정 마감 일시", example = "2024-02-05T23:59:59Z")
      Instant decisionDeadlineAt
  ) {

  }

  @Builder
  @Schema(name = "ProjectMatchingRoundUpdateRequest", description = "프로젝트 매칭 라운드 수정 요청")
  public record UpdateRequest(
      @Schema(description = "매칭 라운드명", example = "9th Leo 기획-디자인 1차 매칭")
      String name,
      @Schema(description = "매칭 라운드 설명", example = "Leo 지부의 기획-디자인 1차 매칭 기간입니다.")
      String description,
      @Schema(description = "시작 일시", example = "2024-01-01T00:00:00Z")
      Instant startAt,
      @Schema(description = "종료 일시", example = "2024-01-31T23:59:59Z")
      Instant endAt,
      @Schema(description = "합/불 결정 마감 일시", example = "2024-02-05T23:59:59Z")
      Instant decisionDeadlineAt
  ) {

  }

  @Builder
  @Schema(name = "ProjectMatchingRoundResponse", description = "프로젝트 매칭 라운드 정보 응답")
  public record Response(
      @Schema(description = "매칭 라운드 ID", example = "1")
      Long id,
      @Schema(description = "매칭 라운드명", example = "9th Leo 기획-디자인 1차 매칭")
      String name,
      @Schema(description = "매칭 라운드 설명", example = "Leo 지부의 기획-디자인 1차 매칭 기간입니다.")
      String description,
      @Schema(description = "챕터 ID", example = "1")
      Long chapterId,
      @Schema(description = "챕터명", example = "Leo")
      String chapterName,
      @Schema(description = "시작 일시")
      Instant startAt,
      @Schema(description = "종료 일시")
      Instant endAt,
      @Schema(description = "합/불 결정 마감 일시")
      Instant decisionDeadlineAt,
      @Schema(description = "생성 일시")
      Instant createdAt,
      @Schema(description = "수정 일시")
      Instant updatedAt
  ) {

  }
}
