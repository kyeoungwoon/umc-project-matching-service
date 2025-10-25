package kr.kyeoungwoon.upms.domain.projectApplicationForm.repository;

import java.util.List;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.entity.ProjectApplicationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectApplicationFormRepository extends
    JpaRepository<ProjectApplicationForm, Long> {

  /**
   * 특정 프로젝트의 지원서 폼 목록 조회
   */
  List<ProjectApplicationForm> findByProjectId(Long projectId);
}
