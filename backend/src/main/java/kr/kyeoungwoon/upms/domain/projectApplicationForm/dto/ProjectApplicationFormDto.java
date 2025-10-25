package kr.kyeoungwoon.upms.domain.projectApplicationForm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Builder;

public class ProjectApplicationFormDto {

  @Builder
  @Schema(name = "ProjectApplicationFormCreateRequest", description = "프로젝트 지원서 폼 생성 요청")
  public record CreateRequest(
      @Schema(description = "프로젝트 ID", example = "1")
      Long projectId,
      @Schema(description = "지원서 폼 제목", example = "7기 UMC 프로젝트 지원서")
      String title,
      @Schema(description = "지원서 폼 설명", example = "프로젝트 지원을 위한 지원서입니다")
      String description
  ) {

  }

  @Builder
  @Schema(name = "ProjectApplicationFormUpdateRequest", description = "프로젝트 지원서 폼 수정 요청")
  public record UpdateRequest(
      @Schema(description = "지원서 폼 제목", example = "7기 UMC 프로젝트 지원서")
      String title,
      @Schema(description = "지원서 폼 설명", example = "프로젝트 지원을 위한 지원서입니다")
      String description
  ) {

  }

  @Builder
  @Schema(name = "ProjectApplicationFormResponse", description = "프로젝트 지원서 폼 정보 응답")
  public record Response(
      @Schema(description = "지원서 폼 ID", example = "1")
      Long id,
      @Schema(description = "프로젝트 ID", example = "1")
      Long projectId,
      @Schema(description = "프로젝트명", example = "UMC 프로젝트 매칭 시스템")
      String projectName,
      @Schema(description = "지원서 폼 제목", example = "7기 UMC 프로젝트 지원서")
      String title,
      @Schema(description = "지원서 폼 설명", example = "프로젝트 지원을 위한 지원서입니다")
      String description,
      @Schema(description = "생성 일시")
      Instant createdAt,
      @Schema(description = "수정 일시")
      Instant updatedAt
  ) {

  }
}
