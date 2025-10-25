package kr.kyeoungwoon.upms.domain.project.repository;

import java.util.List;
import java.util.Optional;
import kr.kyeoungwoon.upms.domain.project.entity.Project;
import kr.kyeoungwoon.upms.domain.project.entity.ProjectTo;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectToRepository extends JpaRepository<ProjectTo, Long> {

  /**
   * 특정 프로젝트와 파트로 TO 조회
   */
  Optional<ProjectTo> findByProjectAndPart(Project project, ChallengerPart part);

  List<ProjectTo> findByProject(Project project);
}
