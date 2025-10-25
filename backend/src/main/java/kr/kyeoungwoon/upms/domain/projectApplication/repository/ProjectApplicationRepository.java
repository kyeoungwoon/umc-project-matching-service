package kr.kyeoungwoon.upms.domain.projectApplication.repository;

import java.util.List;
import java.util.Optional;
import kr.kyeoungwoon.upms.domain.challenger.entity.Challenger;
import kr.kyeoungwoon.upms.domain.project.entity.Project;
import kr.kyeoungwoon.upms.domain.projectApplication.entity.ProjectApplication;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.entity.ProjectMatchingRound;
import kr.kyeoungwoon.upms.global.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {

  boolean existsByApplicantAndMatchingRound(
      Challenger applicant,
      ProjectMatchingRound matchingRound
  );

  /**
   * 특정 프로젝트, 파트, 매칭 라운드(제외), 상태로 지원서 개수 조회
   */
  long countByFormProjectAndApplicantPartAndMatchingRoundNotAndStatus(
      Project project,
      kr.kyeoungwoon.upms.global.enums.ChallengerPart part,
      ProjectMatchingRound excludedRound,
      ApplicationStatus status
  );

  /**
   * 특정 프로젝트, 파트, 매칭 라운드, 상태로 지원서 개수 조회
   */
  long countByFormProjectAndApplicantPartAndMatchingRoundAndStatus(
      Project project,
      kr.kyeoungwoon.upms.global.enums.ChallengerPart part,
      ProjectMatchingRound matchingRound,
      ApplicationStatus status
  );

  @Query("SELECT pa FROM ProjectApplication pa "
      + "LEFT JOIN FETCH pa.applicationResponses "
      + "WHERE pa.id = :id")
  Optional<ProjectApplication> findByIdWithResponses(@Param("id") Long id);

  @Query("SELECT DISTINCT pa FROM ProjectApplication pa "
      + "LEFT JOIN FETCH pa.applicationResponses")
  List<ProjectApplication> findAllWithResponses();

  @Query("SELECT DISTINCT pa FROM ProjectApplication pa "
      + "LEFT JOIN FETCH pa.applicationResponses "
      + "WHERE pa.form.id = :formId")
  List<ProjectApplication> findByFormIdWithResponses(@Param("formId") Long formId);

  @Query("SELECT DISTINCT pa FROM ProjectApplication pa "
      + "LEFT JOIN FETCH pa.applicationResponses "
      + "WHERE pa.form.id IN :formIds")
  List<ProjectApplication> findByFormIdInWithResponses(@Param("formIds") List<Long> formIds);

  @Query("SELECT DISTINCT pa FROM ProjectApplication pa "
      + "LEFT JOIN FETCH pa.applicationResponses "
      + "WHERE pa.applicant.id = :challengerId")
  List<ProjectApplication> findByApplicantIdWithResponses(@Param("challengerId") Long challengerId);
}
