package kr.kyeoungwoon.upms.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import lombok.Builder;

public class ProjectDto {

  @Builder
  @Schema(name = "ProjectCreateRequest", description = "프로젝트 생성 요청")
  public record CreateRequest(
      @Schema(description = "프로젝트명", example = "UMC 프로젝트 매칭 시스템")
      String name,
      @Schema(description = "프로젝트 설명", example = "UMC 챌린저들의 프로젝트 매칭을 위한 시스템")
      String description,
      @Schema(description = "프로덕트 오너 ID", example = "1")
      Long productOwnerId,
      @Schema(description = "챕터 ID", example = "1")
      Long chapterId,
      @Schema(description = "프로젝트 로고 이미지 URL", example = "https://example.com/logo.png")
      String logoImageUrl,
      @Schema(description = "프로젝트 배너 이미지 URL", example = "https://example.com/banner.png")
      String bannerImageUrl,
      @Schema(description = "노션 링크", example = "https://notion.so/project")
      String notionLink
  ) {

  }

  @Builder
  @Schema(name = "ProjectUpdateRequest", description = "프로젝트 정보 수정 요청")
  public record UpdateRequest(
      @Schema(description = "프로젝트명", example = "UPMS")
      String name,
      @Schema(description = "프로젝트 설명", example = "UMC 챌린저들의 프로젝트 매칭을 위한 시스템")
      String description,
      @Schema(description = "프로덕트 오너 ID", example = "1")
      Long productOwnerId,
      @Schema(description = "프로젝트 로고 이미지 URL", example = "https://example.com/logo.png")
      String logoImageUrl,
      @Schema(description = "프로젝트 배너 이미지 URL", example = "https://example.com/banner.png")
      String bannerImageUrl,
      @Schema(description = "노션 링크", example = "https://notion.so/project")
      String notionLink
  ) {

  }

  @Builder
  @Schema(name = "ProjectResponse", description = "프로젝트 정보 응답")
  public record Response(
      @Schema(description = "프로젝트 ID", example = "1")
      Long id,
      @Schema(description = "프로젝트명", example = "UMC 프로젝트 매칭 시스템")
      String name,
      @Schema(description = "프로젝트 설명", example = "UMC 챌린저들의 프로젝트 매칭을 위한 시스템")
      String description,
      @Schema(description = "프로덕트 오너 ID", example = "1")
      Long productOwnerId,
      @Schema(description = "프로덕트 오너 이름", example = "정보운")
      String productOwnerName,
      @Schema(description = "프로덕트 오너 닉네임", example = "보니")
      String productOwnerNickname,
      @Schema(description = "프로덕트 오너 학교", example = "중앙대학교")
      String productOwnerSchool,
      @Schema(description = "지부 ID", example = "1")
      Long chapterId,
      @Schema(description = "지부명", example = "Ain")
      String chapterName,
      @Schema(description = "프로젝트 로고 이미지 URL", example = "https://example.com/logo.png")
      String logoImageUrl,
      @Schema(description = "프로젝트 배너 이미지 URL", example = "https://example.com/banner.png")
      String bannerImageUrl,
      @Schema(description = "노션 링크", example = "https://notion.so/project")
      String notionLink,
      @Schema(description = "프로젝트 TO 목록 (모집 파트별 인원)")
      List<ProjectToInfo> projectTos,
      @Schema(description = "프로젝트 멤버 목록")
      List<ProjectMemberInfo> projectMembers,
      @Schema(description = "생성 일시")
      Instant createdAt,
      @Schema(description = "수정 일시")
      Instant updatedAt
  ) {

  }

  @Builder
  @Schema(name = "ProjectToInfo", description = "프로젝트 TO 정보")
  public record ProjectToInfo(
      @Schema(description = "TO ID", example = "1")
      Long id,
      @Schema(description = "파트", example = "WEB")
      kr.kyeoungwoon.upms.global.enums.ChallengerPart part,
      @Schema(description = "모집 인원", example = "2")
      Integer toCount
  ) {

  }

  @Builder
  @Schema(name = "ProjectMemberInfo", description = "프로젝트 멤버 정보")
  public record ProjectMemberInfo(
      @Schema(description = "멤버 ID", example = "1")
      Long id,
      @Schema(description = "챌린저 ID", example = "1")
      Long challengerId,
      @Schema(description = "챌린저 이름", example = "박경운")
      String challengerName,
      @Schema(description = "챌린저 닉네임", example = "하늘")
      String challengerNickname,
      @Schema(description = "파트", example = "WEB")
      kr.kyeoungwoon.upms.global.enums.ChallengerPart part,
      @Schema(description = "학교 ID", example = "1")
      Long schoolId,
      @Schema(description = "학교명", example = "중앙대학교")
      String schoolName,
      @Schema(description = "활성 상태", example = "true")
      boolean active
  ) {

  }
}
