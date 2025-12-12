package kr.kyeoungwoon.upms.domain.challenger.service;

import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterSchoolDto;
import kr.kyeoungwoon.upms.domain.challenger.entity.Challenger;
import kr.kyeoungwoon.upms.domain.challenger.entity.Chapter;
import kr.kyeoungwoon.upms.domain.challenger.entity.ChapterSchool;
import kr.kyeoungwoon.upms.domain.challenger.entity.School;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChallengerRepository;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChapterRepository;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChapterSchoolRepository;
import kr.kyeoungwoon.upms.domain.challenger.repository.SchoolRepository;
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
public class ChapterSchoolService {

  private final ChapterSchoolRepository chapterSchoolRepository;
  private final ChapterRepository chapterRepository;
  private final SchoolRepository schoolRepository;
  private final ChallengerRepository challengerRepository;

  @Transactional
  public ChapterSchoolDto.Response create(ChapterSchoolDto.CreateRequest request) {
    log.info("챕터-학교 연동 생성 요청 - chapterId: {}, schoolId: {}, leaderId: {}, viceLeaderId: {}",
        request.chapterId(), request.schoolId(), request.leaderId(), request.viceLeaderId());
    Chapter chapter = chapterRepository.findById(request.chapterId())
        .orElseThrow(() -> new DomainException(DomainType.CHAPTER, ErrorStatus.CHAPTER_NOT_FOUND));

    School school = schoolRepository.findById(request.schoolId())
        .orElseThrow(() -> new DomainException(DomainType.SCHOOL, ErrorStatus.SCHOOL_NOT_FOUND));

    Challenger leader = request.leaderId() != null
        ? challengerRepository.findById(request.leaderId())
        .orElseThrow(() -> new DomainException(DomainType.CHALLENGER,
            ErrorStatus.CHAPTER_LEADER_NOT_FOUND))
        : null;

    Challenger viceLeader = request.viceLeaderId() != null
        ? challengerRepository.findById(request.viceLeaderId())
        .orElseThrow(() -> new DomainException(DomainType.CHALLENGER,
            ErrorStatus.CHAPTER_VICE_LEADER_NOT_FOUND))
        : null;

    ChapterSchool chapterSchool = ChapterSchool.builder()
        .chapter(chapter)
        .school(school)
        .leader(leader)
        .viceLeader(viceLeader)
        .build();

    ChapterSchool saved = chapterSchoolRepository.save(chapterSchool);
    log.info("챕터-학교 연동 생성 완료 - id: {}", saved.getId());
    return toResponse(saved);
  }

  public ChapterSchoolDto.Response findById(Long id) {
    log.info("챕터-학교 연동 단건 조회 - id: {}", id);
    ChapterSchool chapterSchool = chapterSchoolRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.CHAPTER_SCHOOL,
            ErrorStatus.CHAPTER_SCHOOL_NOT_FOUND));
    return toResponse(chapterSchool);
  }

  /**
   * ChapterSchool 목록 조회 (chapterId로 필터링 가능)
   *
   * @param chapterId Chapter ID (optional)
   * @return ChapterSchool 목록
   */
  public List<ChapterSchoolDto.Response> findAll(Long chapterId) {
    log.info("챕터-학교 연동 목록 조회 - chapterId 필터: {}", chapterId);
    if (chapterId != null) {
      if (!chapterRepository.existsById(chapterId)) {
        throw new DomainException(DomainType.CHAPTER, ErrorStatus.CHAPTER_NOT_FOUND);
      }
      return chapterSchoolRepository.findByChapterId(chapterId).stream()
          .map(this::toResponse)
          .toList();
    }
    return chapterSchoolRepository.findAll().stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional
  public ChapterSchoolDto.Response update(Long id, ChapterSchoolDto.UpdateRequest request) {
    log.info("챕터-학교 리더 정보 수정 요청 - id: {}, leaderId: {}, viceLeaderId: {}", id,
        request.leaderId(), request.viceLeaderId());
    ChapterSchool chapterSchool = chapterSchoolRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.CHAPTER_SCHOOL,
            ErrorStatus.CHAPTER_SCHOOL_NOT_FOUND));

    Challenger leader = request.leaderId() != null
        ? challengerRepository.findById(request.leaderId())
        .orElseThrow(() -> new DomainException(DomainType.CHALLENGER,
            ErrorStatus.CHAPTER_LEADER_NOT_FOUND))
        : null;

    Challenger viceLeader = request.viceLeaderId() != null
        ? challengerRepository.findById(request.viceLeaderId())
        .orElseThrow(() -> new DomainException(DomainType.CHALLENGER,
            ErrorStatus.CHAPTER_VICE_LEADER_NOT_FOUND))
        : null;

    chapterSchool.updateLeaders(leader, viceLeader);

    return toResponse(chapterSchool);
  }

  @Transactional
  public void delete(Long id) {
    log.info("챕터-학교 연동 삭제 요청 - id: {}", id);
    if (!chapterSchoolRepository.existsById(id)) {
      throw new DomainException(DomainType.CHAPTER_SCHOOL, ErrorStatus.CHAPTER_SCHOOL_NOT_FOUND);
    }
    chapterSchoolRepository.deleteById(id);
  }

  private ChapterSchoolDto.Response toResponse(ChapterSchool chapterSchool) {
    return ChapterSchoolDto.Response.builder()
        .id(chapterSchool.getId())
        .chapterId(chapterSchool.getChapter().getId())
        .chapterName(chapterSchool.getChapter().getName())
        .schoolId(chapterSchool.getSchool().getId())
        .schoolName(chapterSchool.getSchool().getName())
        .leaderId(chapterSchool.getLeader() != null ? chapterSchool.getLeader().getId() : null)
        .leaderName(chapterSchool.getLeader() != null ? chapterSchool.getLeader().getName() : null)
        .viceLeaderId(
            chapterSchool.getViceLeader() != null ? chapterSchool.getViceLeader().getId() : null)
        .viceLeaderName(
            chapterSchool.getViceLeader() != null ? chapterSchool.getViceLeader().getName() : null)
        .createdAt(chapterSchool.getCreatedAt())
        .updatedAt(chapterSchool.getUpdatedAt())
        .build();
  }
}
