package kr.kyeoungwoon.upms.domain.project.repository;

import java.util.List;
import kr.kyeoungwoon.upms.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

  boolean existsByProjectIdAndChapterId(Long projectId, Long chapterId);

  /**
   * 특정 Chapter의 프로젝트 목록 조회
   */
  List<Project> findByChapterId(Long chapterId);
}
