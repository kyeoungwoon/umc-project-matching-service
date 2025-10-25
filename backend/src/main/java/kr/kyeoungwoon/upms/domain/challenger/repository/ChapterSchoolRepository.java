package kr.kyeoungwoon.upms.domain.challenger.repository;

import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.entity.ChapterSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterSchoolRepository extends JpaRepository<ChapterSchool, Long> {

  /**
   * 특정 Chapter에 속한 ChapterSchool 목록 조회
   */
  List<ChapterSchool> findByChapterId(Long chapterId);
}
