package kr.kyeoungwoon.upms.domain.challenger.repository;

import java.util.Optional;
import kr.kyeoungwoon.upms.domain.challenger.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

  // Chapter 조회 시 ChapterSchool과 School을 함께 fetch join (N+1 문제 방지)
  @Query("SELECT DISTINCT c FROM Chapter c "
      + "LEFT JOIN FETCH c.chapterSchools cs "
      + "LEFT JOIN FETCH cs.school "
      + "WHERE c.id = :id")
  Optional<Chapter> findByIdWithSchools(@Param("id") Long id);

  /**
   * 챌린저 ID로 해당 챌린저가 속한 Chapter 조회 Challenger -> School -> ChapterSchool -> Chapter 경로로 조회
   */
  @Query("SELECT DISTINCT c FROM Chapter c "
      + "JOIN c.chapterSchools cs "
      + "JOIN cs.school s "
      + "JOIN Challenger ch ON ch.school = s "
      + "WHERE ch.id = :challengerId AND ch.gisu = c.gisu")
  Optional<Chapter> findByChallengerId(@Param("challengerId") Long challengerId);
}
