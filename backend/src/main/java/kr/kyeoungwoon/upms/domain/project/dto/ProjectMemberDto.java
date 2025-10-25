package kr.kyeoungwoon.upms.domain.project.dto;

import java.time.Instant;
import lombok.Builder;

public class ProjectMemberDto {

  @Builder
  public record CreateRequest(
      Long projectId,
      Long challengerId,
      boolean active
  ) {

  }

  @Builder
  public record UpdateRequest(
      boolean active
  ) {

  }

  @Builder
  public record Response(
      Long id,
      Long projectId,
      String projectName,
      Long challengerId,
      String challengerName,
      boolean active,
      Instant createdAt,
      Instant updatedAt
  ) {

  }
}
