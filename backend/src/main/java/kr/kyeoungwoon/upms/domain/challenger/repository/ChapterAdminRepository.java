package kr.kyeoungwoon.upms.domain.challenger.repository;

import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.entity.ChapterAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterAdminRepository extends JpaRepository<ChapterAdmin, Long> {

  /**
   * 특정 챌린저가 어떤 챕터의 관리자인지 확인
   */
  boolean existsByChallengerId(Long challengerId);

  /**
   * 특정 챌린저가 특정 챕터의 관리자 권한을 가지고 있는지 확인
   */
  boolean existsByChallengerIdAndChapterId(Long challengerId, Long chapterId);

  /**
   * 특정 챕터의 관리자 목록 조회
   */
  List<ChapterAdmin> findByChapterId(Long chapterId);

  /**
   * 특정 챌린저의 관리자 권한 목록 조회
   */
  List<ChapterAdmin> findByChallengerId(Long challengerId);

  /**
   * 특정 챌린저의 특정 챕터에서의 관리자 권한 조회
   */
  java.util.Optional<ChapterAdmin> findByChallengerIdAndChapterId(Long challengerId,
      Long chapterId);
}

