package kr.kyeoungwoon.upms.domain.projectMatchingRound.service;

import java.time.Instant;
import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.entity.Chapter;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChapterRepository;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.dto.ProjectMatchingRoundDto;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.entity.ProjectMatchingRound;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.repository.ProjectMatchingRoundRepository;
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
public class ProjectMatchingRoundService {

  private final ProjectMatchingRoundRepository projectMatchingRoundRepository;
  private final ChapterRepository chapterRepository;

  @Transactional
  public ProjectMatchingRoundDto.Response create(ProjectMatchingRoundDto.CreateRequest request) {
    log.info("매칭 라운드 생성 요청 - chapterId: {}, 기간: {} ~ {}", request.chapterId(),
        request.startAt(), request.endAt());
    Chapter chapter = chapterRepository.findById(request.chapterId())
        .orElseThrow(() -> new DomainException(DomainType.CHAPTER, ErrorStatus.CHAPTER_NOT_FOUND));

    // 시작 시간이 종료 시간보다 늦은 경우 검증
    if (request.startAt().isAfter(request.endAt()) || request.startAt().equals(request.endAt())) {
      throw new DomainException(DomainType.PROJECT_MATCHING_ROUND,
          ErrorStatus.MATCHING_ROUND_INVALID_SCHEDULE);
    }

    // 종료 시간이 Plan 결정 마감 시간보다 앞서 있어야 함
    if (request.decisionDeadlineAt() == null
        || request.endAt().isAfter(request.decisionDeadlineAt())
        || request.endAt().equals(request.decisionDeadlineAt())) {
      throw new DomainException(DomainType.PROJECT_MATCHING_ROUND,
          ErrorStatus.MATCHING_ROUND_INVALID_SCHEDULE);
    }

    // 같은 Chapter에서 기간이 중복되는 매칭 라운드가 있는지 확인
    boolean hasOverlap = projectMatchingRoundRepository.existsOverlappingRound(
        request.chapterId(),
        request.startAt(),
        request.endAt()
    );

    if (hasOverlap) {
      throw new DomainException(DomainType.PROJECT_MATCHING_ROUND,
          ErrorStatus.MATCHING_ROUND_PERIOD_OVERLAP);
    }

    ProjectMatchingRound matchingRound = ProjectMatchingRound.builder()
        .name(request.name())
        .description(request.description())
        .chapter(chapter)
        .startAt(request.startAt())
        .endAt(request.endAt())
        .decisionDeadlineAt(request.decisionDeadlineAt())
        .build();

    ProjectMatchingRound saved = projectMatchingRoundRepository.save(matchingRound);
    log.info("매칭 라운드 생성 완료 - id: {}", saved.getId());
    return toResponse(saved);
  }

  public ProjectMatchingRoundDto.Response findById(Long id) {
    log.info("매칭 라운드 단건 조회 - id: {}", id);
    ProjectMatchingRound matchingRound = projectMatchingRoundRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_MATCHING_ROUND,
            ErrorStatus.MATCHING_ROUND_NOT_FOUND));
    return toResponse(matchingRound);
  }

  /**
   * 전체 매칭 라운드 조회
   */
  public List<ProjectMatchingRoundDto.Response> findAll() {
    log.info("전체 매칭 라운드 목록 조회");
    return projectMatchingRoundRepository.findAll().stream()
        .map(this::toResponse)
        .toList();
  }

  /**
   * 특정 Chapter의 현재 진행 중이거나 가장 가까운 매칭 라운드 조회
   */
  public ProjectMatchingRoundDto.Response findCurrentOrUpcoming(Long chapterId) {
    log.info("챕터의 진행/예정 매칭 라운드 조회 - chapterId: {}", chapterId);
    ProjectMatchingRound matchingRound = projectMatchingRoundRepository
        .findCurrentOrUpcomingByChapterId(chapterId, Instant.now())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_MATCHING_ROUND,
            ErrorStatus.MATCHING_ROUND_NOT_AVAILABLE_FOR_CHAPTER));
    return toResponse(matchingRound);
  }

  /**
   * 특정 Chapter의 현재 진행 중이거나 가장 가까운 매칭 라운드 조회
   */
  public ProjectMatchingRoundDto.Response findCurrent(Long chapterId) {
    log.info("챕터의 현재 매칭 라운드 조회 - chapterId: {}", chapterId);
    ProjectMatchingRound matchingRound = projectMatchingRoundRepository
        .findCurrentMatchingRound(chapterId, Instant.now())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_MATCHING_ROUND,
            ErrorStatus.MATCHING_ROUND_NOT_AVAILABLE_FOR_CHAPTER));
    return toResponse(matchingRound);
  }

  /**
   * 특정 Chapter의 특정 기간 내 매칭 라운드 목록 조회
   */
  public List<ProjectMatchingRoundDto.Response> findByChapterAndTimeBetween(
      Long chapterId, Instant startTime, Instant endTime) {
    log.info("챕터 매칭 라운드 기간 검색 - chapterId: {}, 기간: {} ~ {}", chapterId, startTime,
        endTime);
    if (!chapterRepository.existsById(chapterId)) {
      throw new DomainException(DomainType.CHAPTER, ErrorStatus.CHAPTER_NOT_FOUND);
    }
    return projectMatchingRoundRepository
        .findByChapterIdAndTimeBetween(chapterId, startTime, endTime).stream()
        .map(this::toResponse)
        .toList();
  }


  @Transactional
  public ProjectMatchingRoundDto.Response update(Long id,
      ProjectMatchingRoundDto.UpdateRequest request) {
    log.info("매칭 라운드 수정 요청 - id: {}, 변경 기간: {} ~ {}", id, request.startAt(),
        request.endAt());
    ProjectMatchingRound matchingRound = projectMatchingRoundRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_MATCHING_ROUND,
            ErrorStatus.MATCHING_ROUND_NOT_FOUND));

    Instant newStartAt = request.startAt() != null ? request.startAt() : matchingRound.getStartAt();
    Instant newEndAt = request.endAt() != null ? request.endAt() : matchingRound.getEndAt();
    Instant newDecisionDeadlineAt = request.decisionDeadlineAt() != null
        ? request.decisionDeadlineAt()
        : matchingRound.getDecisionDeadlineAt();

    // 시작 시간이 종료 시간보다 늦은 경우 검증
    if (newStartAt.isAfter(newEndAt) || newStartAt.equals(newEndAt)) {
      throw new DomainException(DomainType.PROJECT_MATCHING_ROUND,
          ErrorStatus.MATCHING_ROUND_INVALID_SCHEDULE);
    }

    // 종료 시간 < 결정 마감 시간 이여야 함.
    if (newEndAt.isAfter(newDecisionDeadlineAt) || newEndAt.equals(newDecisionDeadlineAt)) {
      throw new DomainException(DomainType.PROJECT_MATCHING_ROUND,
          ErrorStatus.MATCHING_ROUND_INVALID_SCHEDULE);
    }

    // 기간이 변경되는 경우에만 중복 체크
    if (request.startAt() != null || request.endAt() != null) {
      boolean hasOverlap = projectMatchingRoundRepository.existsOverlappingRoundExcludingSelf(
          matchingRound.getChapter().getId(),
          id,
          newStartAt,
          newEndAt
      );

      if (hasOverlap) {
        throw new DomainException(DomainType.PROJECT_MATCHING_ROUND,
            ErrorStatus.MATCHING_ROUND_PERIOD_OVERLAP);
      }
    }

    ProjectMatchingRound updated = ProjectMatchingRound.builder()
        .id(matchingRound.getId())
        .name(request.name() != null ? request.name() : matchingRound.getName())
        .description(
            request.description() != null ? request.description() : matchingRound.getDescription())
        .chapter(matchingRound.getChapter())
        .startAt(newStartAt)
        .endAt(newEndAt)
        .decisionDeadlineAt(newDecisionDeadlineAt)
        .isAutoDecisionExecuted(matchingRound.getIsAutoDecisionExecuted())
        .applications(matchingRound.getApplications())
        .build();

    ProjectMatchingRound saved = projectMatchingRoundRepository.save(updated);
    log.info("매칭 라운드 수정 완료 - id: {}", saved.getId());
    return toResponse(saved);
  }

  @Transactional
  public void delete(Long id) {
    log.info("매칭 라운드 삭제 요청 - id: {}", id);
    if (!projectMatchingRoundRepository.existsById(id)) {
      throw new DomainException(DomainType.PROJECT_MATCHING_ROUND,
          ErrorStatus.MATCHING_ROUND_NOT_FOUND);
    }
    projectMatchingRoundRepository.deleteById(id);
  }

  private ProjectMatchingRoundDto.Response toResponse(ProjectMatchingRound matchingRound) {
    return ProjectMatchingRoundDto.Response.builder()
        .id(matchingRound.getId())
        .name(matchingRound.getName())
        .description(matchingRound.getDescription())
        .chapterId(matchingRound.getChapter().getId())
        .chapterName(matchingRound.getChapter().getName())
        .startAt(matchingRound.getStartAt())
        .endAt(matchingRound.getEndAt())
        .decisionDeadlineAt(matchingRound.getDecisionDeadlineAt())
        .createdAt(matchingRound.getCreatedAt())
        .updatedAt(matchingRound.getUpdatedAt())
        .build();
  }
}
