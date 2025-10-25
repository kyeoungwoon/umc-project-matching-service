package kr.kyeoungwoon.upms.domain.challenger.service;

import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterDto;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterDto.Response;
import kr.kyeoungwoon.upms.domain.challenger.entity.Challenger;
import kr.kyeoungwoon.upms.domain.challenger.entity.Chapter;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChallengerRepository;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChapterRepository;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import kr.kyeoungwoon.upms.global.apiPayload.exception.DomainException;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChapterService {

  private final ChapterRepository chapterRepository;
  private final ChallengerRepository challengerRepository;

  @Transactional
  public ChapterDto.Response create(ChapterDto.CreateRequest request) {
    Chapter chapter = Chapter.builder()
        .name(request.name())
        .description(request.description())
        .gisu(request.gisu())
        .build();

    Chapter saved = chapterRepository.save(chapter);
    return toResponse(saved);
  }

  @Transactional
  public List<ChapterDto.Response> bulkCreate(ChapterDto.BulkCreateRequest request) {
    List<Chapter> chapters = request.chapters().stream()
        .map(req -> Chapter.builder()
            .name(req.name())
            .description(req.description())
            .gisu(req.gisu())
            .build())
        .toList();

    List<Chapter> savedChapters = chapterRepository.saveAll(chapters);

    return savedChapters.stream()
        .map(this::toResponse)
        .toList();
  }

  public ChapterDto.Response findById(Long id) {
    Chapter chapter = chapterRepository.findByIdWithSchools(id)
        .orElseThrow(() -> new DomainException(DomainType.CHAPTER, ErrorStatus.CHAPTER_NOT_FOUND));
    return toResponseWithSchools(chapter);
  }

  /**
   * 챌린저 ID로 해당 챌린저가 속한 Chapter 조회
   */
  public ChapterDto.Response findByChallengerId(Long challengerId) {
    Chapter chapter = chapterRepository.findByChallengerId(challengerId)
        .orElseThrow(() -> new DomainException(DomainType.CHAPTER,
            ErrorStatus.CHAPTER_FOR_CHALLENGER_NOT_FOUND));
    return toResponse(chapter);
  }

  public boolean isChallengerPlanInChapter(Long challengerId, Long chapterId) {
    Chapter chapter = chapterRepository.findByChallengerId(challengerId)
        .orElseThrow(() -> new DomainException(DomainType.CHAPTER,
            ErrorStatus.CHAPTER_FOR_CHALLENGER_NOT_FOUND));

    Challenger challenger = challengerRepository.findById(challengerId)
        .orElseThrow(() -> new DomainException(DomainType.CHALLENGER,
            ErrorStatus.CHALLENGER_NOT_FOUND));

    return challenger.getPart() == ChallengerPart.PLAN && chapter.getId().equals(chapterId);
  }

  public List<Response> findAll() {
    return chapterRepository.findAll().stream()
        .map(this::toResponseWithSchools)
        .toList();
  }

  @Transactional
  public ChapterDto.Response update(Long id, ChapterDto.UpdateRequest request) {
    Chapter chapter = chapterRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.CHAPTER, ErrorStatus.CHAPTER_NOT_FOUND));

    chapter.updateInfo(request.name(), request.description());

    return toResponse(chapter);
  }

  @Transactional
  public void delete(Long id) {
    if (!chapterRepository.existsById(id)) {
      throw new DomainException(DomainType.CHAPTER, ErrorStatus.CHAPTER_NOT_FOUND);
    }
    chapterRepository.deleteById(id);
  }

  private ChapterDto.Response toResponse(Chapter chapter) {
    return ChapterDto.Response.builder()
        .id(chapter.getId())
        .name(chapter.getName())
        .description(chapter.getDescription())
        .gisu(chapter.getGisu())
        .schools(null)
        .createdAt(chapter.getCreatedAt())
        .updatedAt(chapter.getUpdatedAt())
        .build();
  }

  private ChapterDto.Response toResponseWithSchools(Chapter chapter) {
    List<kr.kyeoungwoon.upms.domain.challenger.dto.SchoolDto.Response> schools =
        chapter.getChapterSchools().stream()
            .map(cs -> kr.kyeoungwoon.upms.domain.challenger.dto.SchoolDto.Response.builder()
                .id(cs.getSchool().getId())
                .name(cs.getSchool().getName())
                .logoImageUrl(cs.getSchool().getLogoImageUrl())
                .createdAt(cs.getSchool().getCreatedAt())
                .updatedAt(cs.getSchool().getUpdatedAt())
                .build())
            .toList();

    return ChapterDto.Response.builder()
        .id(chapter.getId())
        .name(chapter.getName())
        .description(chapter.getDescription())
        .gisu(chapter.getGisu())
        .schools(schools)
        .createdAt(chapter.getCreatedAt())
        .updatedAt(chapter.getUpdatedAt())
        .build();
  }
}
