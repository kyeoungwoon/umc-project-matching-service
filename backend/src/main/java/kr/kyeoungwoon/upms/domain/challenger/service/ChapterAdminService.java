package kr.kyeoungwoon.upms.domain.challenger.service;

import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterAdminDto;
import kr.kyeoungwoon.upms.domain.challenger.entity.Challenger;
import kr.kyeoungwoon.upms.domain.challenger.entity.Chapter;
import kr.kyeoungwoon.upms.domain.challenger.entity.ChapterAdmin;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChallengerRepository;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChapterAdminRepository;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChapterRepository;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import kr.kyeoungwoon.upms.global.apiPayload.exception.DomainException;
import kr.kyeoungwoon.upms.global.enums.ChapterAdminRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChapterAdminService {

  private final ChapterAdminRepository chapterAdminRepository;
  private final ChapterRepository chapterRepository;
  private final ChallengerRepository challengerRepository;

  @Transactional
  public ChapterAdminDto.Response create(ChapterAdminDto.CreateRequest request) {
    Chapter chapter = chapterRepository.findById(request.chapterId())
        .orElseThrow(() -> new DomainException(DomainType.CHAPTER, ErrorStatus.CHAPTER_NOT_FOUND));

    Challenger challenger = challengerRepository.findById(request.challengerId())
        .orElseThrow(() -> new DomainException(DomainType.CHALLENGER,
            ErrorStatus.CHALLENGER_NOT_FOUND));

    ChapterAdmin chapterAdmin = ChapterAdmin.builder()
        .chapter(chapter)
        .challenger(challenger)
        .role(request.role())
        .build();

    ChapterAdmin saved = chapterAdminRepository.save(chapterAdmin);
    return toResponse(saved);
  }

  public ChapterAdminDto.Response findById(Long id) {
    ChapterAdmin chapterAdmin = chapterAdminRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.CHAPTER_ADMIN,
            ErrorStatus.CHAPTER_ADMIN_NOT_FOUND));
    return toResponse(chapterAdmin);
  }

  /**
   * 챕터 관리자 목록 조회
   *
   * @param chapterId    챕터 ID (optional) - 제공되면 해당 챕터의 관리자만 조회
   * @param challengerId 챌린저 ID (optional) - 제공되면 해당 챌린저의 관리 권한만 조회
   */
  public List<ChapterAdminDto.Response> findAll(Long chapterId, Long challengerId) {
    List<ChapterAdmin> chapterAdmins;

    if (chapterId != null) {
      chapterAdmins = chapterAdminRepository.findByChapterId(chapterId);
    } else if (challengerId != null) {
      chapterAdmins = chapterAdminRepository.findByChallengerId(challengerId);
    } else {
      chapterAdmins = chapterAdminRepository.findAll();
    }

    return chapterAdmins.stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional
  public ChapterAdminDto.Response update(Long id, ChapterAdminDto.UpdateRequest request) {
    ChapterAdmin chapterAdmin = chapterAdminRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.CHAPTER_ADMIN,
            ErrorStatus.CHAPTER_ADMIN_NOT_FOUND));

    ChapterAdmin updated = ChapterAdmin.builder()
        .id(chapterAdmin.getId())
        .chapter(chapterAdmin.getChapter())
        .challenger(chapterAdmin.getChallenger())
        .role(request.role() != null ? request.role() : chapterAdmin.getRole())
        .build();

    ChapterAdmin saved = chapterAdminRepository.save(updated);
    return toResponse(saved);
  }

  /**
   * 특정 챌린저의 특정 챕터에서의 역할 조회
   *
   * @param challengerId 챌린저 ID
   * @param chapterId    챕터 ID
   * @return 역할(role) 문자열, 권한이 없으면 null
   */
  public ChapterAdminRole getRoleByChallengerIdAndChapterId(Long challengerId, Long chapterId) {
    return chapterAdminRepository.findByChallengerIdAndChapterId(challengerId, chapterId)
        .map(ChapterAdmin::getRole)
        .orElse(null);
  }

  public boolean isChallengerChapterAdmin(Long challengerId, Long chapterId) {
    return chapterAdminRepository.existsByChallengerIdAndChapterId(challengerId, chapterId);
  }

  @Transactional
  public void delete(Long id) {
    if (!chapterAdminRepository.existsById(id)) {
      throw new DomainException(DomainType.CHAPTER_ADMIN, ErrorStatus.CHAPTER_ADMIN_NOT_FOUND);
    }
    chapterAdminRepository.deleteById(id);
  }

  private ChapterAdminDto.Response toResponse(ChapterAdmin chapterAdmin) {
    return ChapterAdminDto.Response.builder()
        .id(chapterAdmin.getId())
        .chapterId(chapterAdmin.getChapter().getId())
        .chapterName(chapterAdmin.getChapter().getName())
        .challengerId(chapterAdmin.getChallenger().getId())
        .challengerName(chapterAdmin.getChallenger().getName())
        .challengerNickname(chapterAdmin.getChallenger().getNickname())
        .role(chapterAdmin.getRole())
        .createdAt(chapterAdmin.getCreatedAt())
        .updatedAt(chapterAdmin.getUpdatedAt())
        .build();
  }
}
