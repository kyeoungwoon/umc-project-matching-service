package kr.kyeoungwoon.upms.domain.project.service;

import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.entity.Challenger;
import kr.kyeoungwoon.upms.domain.challenger.entity.Chapter;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChallengerRepository;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChapterRepository;
import kr.kyeoungwoon.upms.domain.challenger.service.ChapterAdminService;
import kr.kyeoungwoon.upms.domain.challenger.service.ChapterService;
import kr.kyeoungwoon.upms.domain.project.dto.ProjectDto;
import kr.kyeoungwoon.upms.domain.project.entity.Project;
import kr.kyeoungwoon.upms.domain.project.repository.ProjectRepository;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import kr.kyeoungwoon.upms.global.apiPayload.exception.DomainException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

  private final ChapterService chapterService;
  private final ChapterAdminService chapterAdminService;

  private final ProjectRepository projectRepository;
  private final ChallengerRepository challengerRepository;
  private final ChapterRepository chapterRepository;

  @Transactional
  public ProjectDto.Response create(ProjectDto.CreateRequest request) {
    log.info("프로젝트 생성 요청 처리 - PO: {}, 챕터: {}, 이름: {}", request.productOwnerId(),
        request.chapterId(), request.name());
    Challenger productOwner = challengerRepository.findById(request.productOwnerId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT,
            ErrorStatus.PROJECT_PRODUCT_OWNER_NOT_FOUND));

    Chapter chapter = chapterRepository.findById(request.chapterId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT,
            ErrorStatus.PROJECT_CHAPTER_NOT_FOUND));

    Project project = Project.builder()
        .name(request.name())
        .description(request.description())
        .productOwner(productOwner)
        .chapter(chapter)
        .logoImageUrl(request.logoImageUrl())
        .bannerImageUrl(request.bannerImageUrl())
        .notionLink(request.notionLink())
        .build();

    Project saved = projectRepository.save(project);
    log.info("프로젝트 생성 완료 - id: {}", saved.getId());
    return toResponse(saved);
  }

  public ProjectDto.Response findById(Long id) {
    log.info("프로젝트 단건 조회 - id: {}", id);
    Project project = projectRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_NOT_FOUND));
    return toResponse(project);
  }

  public void throwIfProjectNotBelongsToChapter(Long projectId, Long chapterId) {
    log.info("프로젝트와 챕터 소속 검증 - projectId: {}, chapterId: {}", projectId, chapterId);
    if (projectRepository.existsByIdAndChapterId(projectId, chapterId)) {
      return;
    }

    throw new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_NOT_IN_CHAPTER);
  }

  /**
   * 프로젝트 목록 조회
   *
   * @param chapterId 챕터 ID (optional) - 제공되면 해당 챕터의 프로젝트만 조회
   */
  public List<ProjectDto.Response> findAll(Long chapterId) {
    List<Project> projects;

    log.info("프로젝트 목록 조회 - chapterId: {}", chapterId);
    if (chapterId != null) {
      projects = projectRepository.findByChapterId(chapterId);
    } else {
      projects = projectRepository.findAll();
    }

    return projects.stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional
  public ProjectDto.Response update(Long id, ProjectDto.UpdateRequest request) {
    log.info("프로젝트 수정 요청 - id: {}, 요청 정보: {}", id, request);
    Project project = projectRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_NOT_FOUND));

    project.updateBasicInfo(request.name(), request.description());
    project.updateImages(request.logoImageUrl(), request.bannerImageUrl());
    project.updateNotionLink(request.notionLink());

    if (request.productOwnerId() != null) {
      Challenger productOwner = challengerRepository.findById(request.productOwnerId())
          .orElseThrow(() -> new DomainException(DomainType.PROJECT,
              ErrorStatus.PROJECT_PRODUCT_OWNER_NOT_FOUND));
      project.changeOwner(productOwner);
      log.info("프로젝트 PO 변경 - projectId: {}, newPO: {}", project.getId(),
          productOwner.getId());
    }

    log.info("프로젝트 수정 완료 - id: {}", project.getId());
    return toResponse(project);
  }

  @Transactional
  public void delete(Long id) {
    log.info("프로젝트 삭제 요청 - id: {}", id);
    if (!projectRepository.existsById(id)) {
      throw new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_NOT_FOUND);
    }
    projectRepository.deleteById(id);
  }

  public boolean isChallengerProductOwner(Long projectId, Long challengerId) {
    log.info("프로젝트 PO 여부 확인 - projectId: {}, challengerId: {}", projectId, challengerId);
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_NOT_FOUND));
    return project.getProductOwner().getId().equals(challengerId);
  }

  // throwing methods

  public void throwIfChallengerNotProductOwnerOrAdmin(Long challengerId, Long chapterId,
      Long projectId) {
    log.info("PO/운영진 권한 검증 - challengerId: {}, chapterId: {}, projectId: {}", challengerId,
        chapterId, projectId);
    boolean isChallengerProductOwnerOrAdmin =
        this.isChallengerProductOwner(projectId, challengerId)
            || chapterAdminService.isChallengerChapterAdmin(challengerId, chapterId);

    if (!isChallengerProductOwnerOrAdmin) {
      throw new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_EDIT_NOT_ALLOWED);
    }
  }

  public void throwIfChallengerNotPlanOrAdmin(Long challengerId, Long chapterId) {
    log.info("Plan/운영진 권한 검증 - challengerId: {}, chapterId: {}", challengerId, chapterId);
    boolean isPlanOrAdmin =
        chapterService.isChallengerPlanInChapter(challengerId, chapterId)
            || chapterAdminService.isChallengerChapterAdmin(challengerId, chapterId);

    if (!isPlanOrAdmin) {
      throw new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_EDIT_NOT_ALLOWED);
    }
  }

  private ProjectDto.Response toResponse(Project project) {
    // ProjectTo 정보 매핑
    List<ProjectDto.ProjectToInfo> projectTos = project.getTos().stream()
        .map(to -> ProjectDto.ProjectToInfo.builder()
            .id(to.getId())
            .part(to.getPart())
            .toCount(to.getToCount())
            .build())
        .toList();

    // ProjectMember 정보 매핑
    List<ProjectDto.ProjectMemberInfo> projectMembers = project.getMembers().stream()
        .map(member -> ProjectDto.ProjectMemberInfo.builder()
            .id(member.getId())
            .challengerId(member.getChallenger().getId())
            .challengerName(member.getChallenger().getName())
            .challengerNickname(member.getChallenger().getNickname())
            .part(member.getChallenger().getPart())
            .schoolId(member.getChallenger().getSchool().getId())
            .schoolName(member.getChallenger().getSchool().getName())
            .active(member.isActive())
            .build())
        .toList();

    return ProjectDto.Response.builder()
        .id(project.getId())
        .name(project.getName())
        .description(project.getDescription())
        .productOwnerId(project.getProductOwner().getId())
        .productOwnerName(project.getProductOwner().getName())
        .productOwnerNickname(project.getProductOwner().getNickname())
        .productOwnerSchool(project.getProductOwner().getSchool().getName())
        .chapterId(project.getChapter().getId())
        .chapterName(project.getChapter().getName())
        .logoImageUrl(project.getLogoImageUrl())
        .bannerImageUrl(project.getBannerImageUrl())
        .notionLink(project.getNotionLink())
        .projectTos(projectTos)
        .projectMembers(projectMembers)
        .createdAt(project.getCreatedAt())
        .updatedAt(project.getUpdatedAt())
        .build();
  }
}
