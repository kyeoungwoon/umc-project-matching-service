package kr.kyeoungwoon.upms.domain.projectApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import lombok.Builder;

public class ProjectApplicationResponseDto {

  @Builder
  @Schema(name = "ProjectApplicationSingleQuestionResponse", description = "지원서 응답 생성 요청")
  public record SingleQuestionResponse(
      @Schema(description = "질문 ID", example = "1")
      Long questionId,
      @Schema(description = "응답 값 (배열)", example = "[\"답변 내용\"]")
      String[] values
  ) {

  }

  @Builder
  @Schema(name = "ProjectApplicationResponseCreateRequest", description = "지원서 응답 생성 요청")
  public record CreateRequest(
      @Schema(description = "지원서 ID", example = "1")
      Long applicationId,
      @Schema(description = "질문 ID", example = "1")
      Long questionId,
      @Schema(description = "응답 값 (배열)", example = "[\"답변 내용\"]")
      String[] values
  ) {

  }

  @Builder
  @Schema(name = "ProjectApplicationResponseBulkCreateRequest", description = "지원서 응답 bulk create")
  public record BulkCreateRequest(
      @Schema(description = "지원서 ID", example = "1")
      Long applicationId,
      @Schema(description = "응답 리스트")
      List<SingleQuestionResponse> responses
  ) {

  }

  @Builder
  @Schema(name = "ProjectApplicationResponseUpdateRequest", description = "지원서 응답 수정 요청")
  public record UpdateRequest(
      @Schema(description = "응답 값 (배열)", example = "[\"수정된 답변\"]")
      String[] values
  ) {

  }

  @Builder
  @Schema(name = "ProjectApplicationResponseResponse", description = "지원서 응답")
  public record Response(
      @Schema(description = "응답 ID", example = "1")
      Long id,
      @Schema(description = "지원서 ID", example = "1")
      Long applicationId,
      @Schema(description = "질문 ID", example = "1")
      Long questionId,
      @Schema(description = "질문 제목", example = "지원 동기를 작성해주세요")
      String questionTitle,
      @Schema(description = "응답 값 (배열)", example = "[\"답변 내용\"]")
      String[] values,
      @Schema(description = "생성 시간")
      Instant createdAt,
      @Schema(description = "수정 시간")
      Instant updatedAt
  ) {

  }
}
