package kr.kyeoungwoon.upms.domain.project.repository;

import java.util.List;
import java.util.Optional;
import kr.kyeoungwoon.upms.domain.challenger.entity.Challenger;
import kr.kyeoungwoon.upms.domain.project.entity.Project;
import kr.kyeoungwoon.upms.domain.project.entity.ProjectMember;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

  /**
   * 특정 프로젝트와 챌린저로 멤버 조회
   */
  Optional<ProjectMember> findByProjectAndChallenger(Project project, Challenger challenger);

  /**
   * 특정 프로젝트와 챌린저의 멤버 존재 여부 확인
   */
  boolean existsByProjectAndChallenger(Project project, Challenger challenger);

  boolean existsByChallengerId(Long challengerId);

  /**
   * 특정 프로젝트의 특정 파트 멤버 수 조회
   */
  long countByProjectAndChallengerPart(Project project, ChallengerPart part);

  List<ProjectMember> findAllByProjectAndChallengerPart(Project project, ChallengerPart part);

  /**
   * 특정 Challenger가 속한 활성 프로젝트 멤버 조회
   */
  @Query("SELECT pm FROM ProjectMember pm "
      + "JOIN FETCH pm.project "
      + "WHERE pm.challenger.id = :challengerId AND pm.active = true")
  Optional<ProjectMember> findFirstActiveByChallengerId(@Param("challengerId") Long challengerId);
}
