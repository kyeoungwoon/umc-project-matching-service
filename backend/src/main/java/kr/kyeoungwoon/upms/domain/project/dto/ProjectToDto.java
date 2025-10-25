package kr.kyeoungwoon.upms.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;
import lombok.Builder;

public class ProjectToDto {

  @Builder
  @Schema(name = "ProjectToCreateRequest", description = "프로젝트 To 생성 요청")
  public record CreateRequest(
      @Schema(description = "프로젝트 ID", example = "1")
      Long projectId,
      @Schema(description = "파트", example = "WEB")
      ChallengerPart part,
      @Schema(description = "모집 인원", example = "3")
      Integer toCount
  ) {

  }

  @Builder
  @Schema(name = "ProjectToBulkCreateRequest", description = "프로젝트 To 일괄 생성 요청")
  public record BulkCreateRequest(
      @Schema(description = "프로젝트 ID", example = "1")
      Long projectId,
      @Schema(description = "파트별 모집 인원 목록")
      List<ToItem> toItems
  ) {

    @Builder
    @Schema(name = "ProjectToItem", description = "파트별 모집 인원")
    public record ToItem(
        @Schema(description = "파트", example = "WEB")
        ChallengerPart part,
        @Schema(description = "모집 인원", example = "3")
        Integer toCount
    ) {

    }
  }

  @Builder
  @Schema(name = "ProjectToUpdateRequest", description = "프로젝트 To 수정 요청")
  public record UpdateRequest(
      @Schema(description = "모집 인원", example = "5")
      Integer toCount
  ) {

  }

  @Builder
  @Schema(name = "ProjectToResponse", description = "프로젝트 To 응답")
  public record Response(
      @Schema(description = "프로젝트 To ID", example = "1")
      Long id,
      @Schema(description = "프로젝트 ID", example = "1")
      Long projectId,
      @Schema(description = "프로젝트 이름", example = "북리플")
      String projectName,
      @Schema(description = "파트", example = "WEB")
      ChallengerPart part,
      @Schema(description = "모집 인원", example = "3")
      Integer toCount,
      @Schema(description = "생성 시간")
      Instant createdAt,
      @Schema(description = "수정 시간")
      Instant updatedAt
  ) {

  }
}
