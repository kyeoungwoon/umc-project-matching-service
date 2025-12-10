package kr.kyeoungwoon.upms.domain.challenger.repository;

import java.util.List;
import java.util.Optional;
import kr.kyeoungwoon.upms.domain.challenger.entity.Challenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengerRepository extends JpaRepository<Challenger, Long> {

  // Unique constraint (school_id, student_id, gisu)로 조회
  Optional<Challenger> findByStudentIdAndSchoolIdAndGisu(String studentId, Long schoolId,
      Long gisu);

  // UMSB ID로 조회
  Optional<Challenger> findByUmsbId(Long umsbId);

  //  해당 이름을 포함하고 있는 모든 챌린저 검색
  List<Challenger> findByNameContaining(String name);

  // Chapter ID로 모든 Challenger 조회 (ChapterSchool을 통해)
  @org.springframework.data.jpa.repository.Query(
      "SELECT DISTINCT c FROM Challenger c "
          + "JOIN ChapterSchool cs ON c.school.id = cs.school.id "
          + "WHERE cs.chapter.id = :chapterId")
  List<Challenger> findByChapterId(
      @org.springframework.data.repository.query.Param("chapterId") Long chapterId);
}
