package kr.kyeoungwoon.upms.domain.projectApplication.repository;

import java.util.List;
import java.util.Optional;
import kr.kyeoungwoon.upms.domain.challenger.entity.Challenger;
import kr.kyeoungwoon.upms.domain.project.entity.Project;
import kr.kyeoungwoon.upms.domain.projectApplication.entity.ProjectApplication;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.entity.ProjectMatchingRound;
import kr.kyeoungwoon.upms.global.enums.ApplicationStatus;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;
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
      ChallengerPart part,
      ProjectMatchingRound excludedRound,
      ApplicationStatus status
  );

  /**
   * 특정 프로젝트, 파트, 매칭 라운드, 상태로 지원서 개수 조회
   */
  long countByFormProjectAndApplicantPartAndMatchingRoundAndStatus(
      Project project,
      ChallengerPart part,
      ProjectMatchingRound matchingRound,
      ApplicationStatus status
  );

  // 프로젝트, 파트, 매칭 라운드의 지원서 개수 조회
  long countByFormProjectAndApplicantPartAndMatchingRound(
      Project project,
      ChallengerPart part,
      ProjectMatchingRound matchingRound
  );

  // 이번 차수에 지원한 지원자 목록 조회
  @Query("SELECT pa.applicant FROM ProjectApplication pa "
      + "WHERE pa.form.project = :project "
      + "AND pa.applicant.part = :part "
      + "AND pa.matchingRound = :matchingRound")
  List<Challenger> findChallengersByProjectAndPartAndRound(
      @Param("project") Project project,
      @Param("part") ChallengerPart part,
      @Param("matchingRound") ProjectMatchingRound matchingRound
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

  /**
   * 특정 매칭 라운드와 상태에 해당하는 지원서 조회 (자동 합/불 처리를 위한 최소 정보 포함)
   */
  @Query("SELECT pa FROM ProjectApplication pa "
      + "JOIN FETCH pa.form f "
      + "JOIN FETCH f.project p "
      + "JOIN FETCH pa.applicant a "
      + "WHERE pa.matchingRound.id = :matchingRoundId AND pa.status = :status")
  List<ProjectApplication> findByMatchingRoundIdAndStatus(
      @Param("matchingRoundId") Long matchingRoundId,
      @Param("status") ApplicationStatus status);
}
