package kr.kyeoungwoon.upms.domain.projectApplication.repository;

import java.util.List;
import kr.kyeoungwoon.upms.domain.projectApplication.entity.ProjectApplication;
import kr.kyeoungwoon.upms.domain.projectApplication.entity.ProjectApplicationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectApplicationResponseRepository extends
    JpaRepository<ProjectApplicationResponse, Long> {

  /**
   * 특정 지원서의 응답 목록 조회
   */
  List<ProjectApplicationResponse> findByApplication(ProjectApplication application);

  /**
   * 특정 지원서의 응답 목록 조회 (applicationId로)
   */
  List<ProjectApplicationResponse> findByApplicationId(Long applicationId);
}
