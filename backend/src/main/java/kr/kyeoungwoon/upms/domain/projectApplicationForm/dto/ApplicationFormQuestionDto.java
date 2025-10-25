package kr.kyeoungwoon.upms.domain.projectApplicationForm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import kr.kyeoungwoon.upms.global.enums.QuestionType;
import lombok.Builder;

public class ApplicationFormQuestionDto {

  @Builder
  @Schema(name = "ApplicationFormQuestionCreateRequest", description = "지원서 질문 생성 요청")
  public record CreateRequest(
      @Schema(description = "지원서 폼 ID", example = "1")
      Long formId,
      @Schema(description = "질문 순서", example = "1")
      Integer questionNo,
      @Schema(description = "질문 제목", example = "지원 동기를 작성해주세요")
      String title,
      @Schema(description = "질문 설명", example = "프로젝트에 지원하게 된 동기를 자유롭게 작성해주세요")
      String description,
      @Schema(description = "질문 유형", example = "SINGLE_CHOICE")
      QuestionType type,
      @Schema(description = "선택지 목록 (MULTIPLE_CHOICE, CHECKBOX 타입인 경우)", example = "[\"옵션1\", \"옵션2\", \"옵션3\"]")
      String[] options,
      @Schema(description = "필수 여부", example = "true")
      boolean required
  ) {

  }

  @Builder
  @Schema(name = "ApplicationFormQuestionBulkCreateRequest", description = "지원서 질문 대량 생성 요청")
  public record BulkCreateRequest(
      @Schema(description = "지원서 폼 ID", example = "1")
      Long formId,
      @Schema(description = "질문 목록")
      java.util.List<QuestionItem> questions
  ) {

    @Builder
    @Schema(description = "질문 항목")
    public record QuestionItem(
        @Schema(description = "질문 순서", example = "1")
        Integer questionNo,
        @Schema(description = "질문 제목", example = "지원 동기를 작성해주세요")
        String title,
        @Schema(description = "질문 설명", example = "프로젝트에 지원하게 된 동기를 자유롭게 작성해주세요")
        String description,
        @Schema(description = "질문 유형", example = "SINGLE_CHOICE")
        QuestionType type,
        @Schema(description = "선택지 목록 (MULTIPLE_CHOICE 타입인 경우)", example = "[\"옵션1\", \"옵션2\", \"옵션3\"]")
        String[] options,
        @Schema(description = "필수 여부", example = "true")
        boolean required
    ) {

    }
  }


  @Builder
  @Schema(name = "ApplicationFormQuestionUpdateRequest", description = "지원서 질문 수정 요청")
  public record UpdateRequest(
      @Schema(description = "질문 순서", example = "1")
      Integer questionNo,
      @Schema(description = "질문 제목", example = "지원 동기를 작성해주세요")
      String title,
      @Schema(description = "질문 설명", example = "프로젝트에 지원하게 된 동기를 자유롭게 작성해주세요")
      String description,
      @Schema(description = "질문 유형", example = "SINGLE_CHOICE")
      QuestionType type,
      @Schema(description = "선택지 목록 (MULTIPLE_CHOICE, CHECKBOX 타입인 경우)", example = "[\"옵션1\", \"옵션2\", \"옵션3\"]")
      String[] options,
      @Schema(description = "필수 여부", example = "true")
      Boolean required,
      @Schema(description = "삭제 여부", example = "false")
      Boolean deleted
  ) {

  }

  @Builder
  @Schema(name = "ApplicationFormQuestionResponse", description = "지원서 질문 정보 응답")
  public record Response(
      @Schema(description = "질문 ID", example = "1")
      Long id,
      @Schema(description = "지원서 폼 ID", example = "1")
      Long formId,
      @Schema(description = "지원서 폼 제목", example = "7기 UMC 프로젝트 지원서")
      String formTitle,
      @Schema(description = "질문 순서", example = "1")
      Integer questionNo,
      @Schema(description = "질문 제목", example = "지원 동기를 작성해주세요")
      String title,
      @Schema(description = "질문 설명", example = "프로젝트에 지원하게 된 동기를 자유롭게 작성해주세요")
      String description,
      @Schema(description = "질문 유형", example = "SINGLE_CHOICE")
      QuestionType type,
      @Schema(description = "선택지 목록", example = "[\"옵션1\", \"옵션2\", \"옵션3\"]")
      String[] options,
      @Schema(description = "필수 여부", example = "true")
      boolean required,
      @Schema(description = "삭제 여부", example = "false")
      boolean deleted,
      @Schema(description = "생성 일시")
      Instant createdAt,
      @Schema(description = "수정 일시")
      Instant updatedAt
  ) {

  }
}
