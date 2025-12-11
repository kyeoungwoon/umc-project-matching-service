package kr.kyeoungwoon.upms.domain.projectApplication.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import kr.kyeoungwoon.upms.domain.challenger.entity.Challenger;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChallengerRepository;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChapterRepository;
import kr.kyeoungwoon.upms.domain.project.entity.Project;
import kr.kyeoungwoon.upms.domain.project.entity.ProjectMember;
import kr.kyeoungwoon.upms.domain.project.entity.ProjectTo;
import kr.kyeoungwoon.upms.domain.project.repository.ProjectMemberRepository;
import kr.kyeoungwoon.upms.domain.project.repository.ProjectRepository;
import kr.kyeoungwoon.upms.domain.project.repository.ProjectToRepository;
import kr.kyeoungwoon.upms.domain.projectApplication.dto.ProjectApplicationDto;
import kr.kyeoungwoon.upms.domain.projectApplication.dto.ProjectApplicationDto.Response;
import kr.kyeoungwoon.upms.domain.projectApplication.dto.ProjectApplicationResponseDto;
import kr.kyeoungwoon.upms.domain.projectApplication.entity.ProjectApplication;
import kr.kyeoungwoon.upms.domain.projectApplication.entity.ProjectApplicationResponse;
import kr.kyeoungwoon.upms.domain.projectApplication.repository.ProjectApplicationRepository;
import kr.kyeoungwoon.upms.domain.projectApplication.repository.ProjectApplicationResponseRepository;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.entity.ApplicationFormQuestion;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.entity.ProjectApplicationForm;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.repository.ApplicationFormQuestionRepository;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.repository.ProjectApplicationFormRepository;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.entity.ProjectMatchingRound;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.repository.ProjectMatchingRoundRepository;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import kr.kyeoungwoon.upms.global.apiPayload.exception.DomainException;
import kr.kyeoungwoon.upms.global.enums.ApplicationStatus;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;
import kr.kyeoungwoon.upms.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectApplicationService {

  private final ProjectApplicationRepository projectApplicationRepository;
  private final ProjectApplicationFormRepository projectApplicationFormRepository;
  private final ChallengerRepository challengerRepository;
  private final ProjectMatchingRoundRepository projectMatchingRoundRepository;
  private final ProjectRepository projectRepository;
  private final ChapterRepository chapterRepository;
  private final ProjectToRepository projectToRepository;
  private final ProjectApplicationResponseRepository projectApplicationResponseRepository;
  private final ApplicationFormQuestionRepository applicationFormQuestionRepository;
  private final ProjectMemberRepository projectMemberRepository;

  @Transactional
  public ProjectApplicationDto.Response create(ProjectApplicationDto.CreateRequest request) {
    ProjectApplicationForm form = projectApplicationFormRepository.findById(request.formId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_APPLICATION_FORM,
            ErrorStatus.PROJECT_APPLICATION_FORM_NOT_FOUND));

    Challenger applicant = challengerRepository.findById(request.applicantId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_APPLICATION,
            ErrorStatus.PROJECT_APPLICATION_APPLICANT_NOT_FOUND));

    ProjectMatchingRound matchingRound = projectMatchingRoundRepository.findById(
            request.matchingRoundId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_MATCHING_ROUND,
            ErrorStatus.MATCHING_ROUND_NOT_FOUND));

    // 동일 차수 내 중복 지원 여부 검증
    if (projectApplicationRepository.existsByApplicantAndMatchingRound(applicant,
        matchingRound)) {
      throw new DomainException(DomainType.PROJECT_APPLICATION,
          ErrorStatus.PA_ALREADY_APPLIED);
    }

    ProjectApplication application = ProjectApplication.builder()
        .form(form)
        .applicant(applicant)
        .matchingRound(matchingRound)
        .status(request.status() != null ? request.status()
            : kr.kyeoungwoon.upms.global.enums.ApplicationStatus.PENDING)
        .build();

    ProjectApplication saved = projectApplicationRepository.save(application);

    // 2. ProjectApplicationResponse 저장 (responses가 있는 경우)
    if (request.responses() != null && !request.responses().isEmpty()) {
      List<ProjectApplicationResponse> responses = request.responses().stream()
          .map(responseDto -> {
            // Question 유효성 검증
            ApplicationFormQuestion question =
                applicationFormQuestionRepository.findById(responseDto.questionId())
                    .orElseThrow(() -> new DomainException(
                        DomainType.PROJECT_APPLICATION_FORM,
                        ErrorStatus.APPLICATION_FORM_QUESTION_NOT_FOUND));

            return ProjectApplicationResponse.builder()
                .application(saved)
                .question(question)
                .values(responseDto.values())
                .build();
          })
          .toList();

      projectApplicationResponseRepository.saveAll(responses);
    }

    return toResponse(saved);
  }

  public ProjectApplicationDto.Response findById(Long id) {
    ProjectApplication application = projectApplicationRepository.findByIdWithResponses(id)
        .orElseThrow(
            () -> new DomainException(DomainType.PROJECT_APPLICATION,
                ErrorStatus.PROJECT_APPLICATION_NOT_FOUND));
    return toResponse(application);
  }

  public List<Response> findAll() {
    return projectApplicationRepository.findAllWithResponses().stream()
        .map(this::toResponse)
        .toList();
  }

  public List<ProjectApplicationDto.DetailResponse> findByFormId(Long formId) {
    if (!projectApplicationFormRepository.existsById(formId)) {
      throw new DomainException(DomainType.PROJECT_APPLICATION_FORM,
          ErrorStatus.PROJECT_APPLICATION_FORM_NOT_FOUND);
    }
    return projectApplicationRepository.findByFormIdWithResponses(formId).stream()
        .map(this::toDetailResponse)
        .toList();
  }

  /**
   * 특정 프로젝트에 대한 지원서 목록 조회
   *
   * @param projectId Project ID
   * @return 해당 프로젝트에 제출된 지원서 목록 (응답 포함)
   */
  public List<ProjectApplicationDto.DetailResponse> findByProjectId(Long projectId) {
    // 1. Project 유효성 검증
    if (!projectRepository.existsById(projectId)) {
      throw new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_NOT_FOUND);
    }

    // 2. 프로젝트의 모든 폼 ID 수집
    List<Long> formIds = projectApplicationFormRepository.findByProjectId(projectId).stream()
        .map(ProjectApplicationForm::getId)
        .toList();

    if (formIds.isEmpty()) {
      return List.of();
    }

    // 3. 모든 폼에 제출된 지원서 조회 (응답 포함)
    return projectApplicationRepository.findByFormIdInWithResponses(formIds).stream()
        .map(this::toDetailResponse)
        .toList();
  }

  /**
   * 특정 Chapter의 모든 프로젝트에 대한 지원서 목록 조회
   *
   * @param chapterId Chapter ID
   * @return 해당 Chapter의 모든 프로젝트에 제출된 지원서 목록 (응답 포함)
   */
  public List<ProjectApplicationDto.DetailResponse> findByChapterId(Long chapterId) {
    // 1. Chapter 유효성 검증
    if (!chapterRepository.existsById(chapterId)) {
      throw new DomainException(DomainType.CHAPTER, ErrorStatus.CHAPTER_NOT_FOUND);
    }

    // 2. Chapter에 속한 모든 프로젝트 조회
    List<Project> projects = projectRepository.findByChapterId(chapterId);

    if (projects.isEmpty()) {
      return List.of();
    }

    // 3. 각 프로젝트의 모든 폼 ID 수집
    List<Long> formIds = projects.stream()
        .flatMap(
            project -> projectApplicationFormRepository.findByProjectId(project.getId()).stream())
        .map(ProjectApplicationForm::getId)
        .toList();

    if (formIds.isEmpty()) {
      return List.of();
    }

    // 4. 모든 폼에 제출된 지원서 조회 (응답 포함)
    return projectApplicationRepository.findByFormIdInWithResponses(formIds).stream()
        .map(this::toDetailResponse)
        .toList();
  }

  /**
   * 특정 챌린저가 제출한 지원서 목록 조회
   *
   * @param challengerId Challenger ID
   * @return 해당 챌린저가 제출한 지원서 목록 (응답 포함)
   */
  public List<ProjectApplicationDto.DetailResponse> findByChallengerId(Long challengerId) {
    // 1. Challenger 유효성 검증
    if (!challengerRepository.existsById(challengerId)) {
      throw new DomainException(DomainType.CHALLENGER, ErrorStatus.CHALLENGER_NOT_FOUND);
    }

    // 2. 챌린저가 제출한 모든 지원서 조회 (응답 포함)
    return projectApplicationRepository.findByApplicantIdWithResponses(challengerId).stream()
        .map(this::toDetailResponse)
        .toList();
  }

  /**
   * Chapter의 모든 Challenger별 매칭 라운드 지원 현황 조회
   *
   * @param chapterId Chapter ID
   * @return 챌린저별 매칭 라운드 지원 현황
   */
  public List<ProjectApplicationDto.ChallengerApplicationSummary> findChapterApplicationSummary(
      Long chapterId) {
    // 1. Chapter 유효성 검증
    if (!chapterRepository.existsById(chapterId)) {
      throw new DomainException(DomainType.CHAPTER, ErrorStatus.CHAPTER_NOT_FOUND);
    }

    // 2. Chapter의 모든 Challenger 조회
    List<Challenger> challengers = challengerRepository.findByChapterId(chapterId);

    // 3. Chapter의 모든 MatchingRound 조회
    List<kr.kyeoungwoon.upms.domain.projectMatchingRound.entity.ProjectMatchingRound> matchingRounds =
        projectMatchingRoundRepository.findByChapterId(chapterId);

    // 4. 각 Challenger별로 매칭 라운드 지원 내역 구성
    return challengers.stream()
        .map(challenger -> buildChallengerApplicationSummary(challenger, matchingRounds))
        .toList();
  }

  private ProjectApplicationDto.ChallengerApplicationSummary buildChallengerApplicationSummary(
      Challenger challenger,
      List<kr.kyeoungwoon.upms.domain.projectMatchingRound.entity.ProjectMatchingRound> matchingRounds) {

    Optional<ProjectMember> pm = projectMemberRepository.findFirstActiveByChallengerId(
        challenger.getId());

    // 챌린저가 멤버로 속한 프로젝트 조회
    ProjectApplicationDto.MemberProject memberProjects = pm.map(projectMember ->
        ProjectApplicationDto.MemberProject.builder()
            .projectId(projectMember.getProject().getId())
            .projectName(projectMember.getProject().getName())
            .projectDescription(projectMember.getProject().getDescription())
            .build()
    ).orElse(null);

    // 각 매칭 라운드별로 지원 내역 구성
    List<ProjectApplicationDto.MatchingRoundApplications> roundApplications =
        matchingRounds.stream()
            .map(round -> {
              // 해당 챌린저가 이 라운드에 제출한 지원서 조회 (최대 1개)
              ProjectApplicationDto.ApplicationBrief application =
                  projectApplicationRepository.findByApplicantIdWithResponses(challenger.getId())
                      .stream()
                      .filter(app -> app.getMatchingRound().getId().equals(round.getId()))
                      .findFirst()
                      .map(app -> ProjectApplicationDto.ApplicationBrief.builder()
                          .applicationId(app.getId())
                          .projectId(app.getForm().getProject().getId())
                          .projectName(app.getForm().getProject().getName())
                          .status(app.getStatus())
                          .appliedAt(app.getCreatedAt())
                          .build())
                      .orElse(null); // 지원하지 않은 경우 null

              return ProjectApplicationDto.MatchingRoundApplications.builder()
                  .matchingRoundId(round.getId())
                  .matchingRoundName(round.getName())
                  .startAt(round.getStartAt())
                  .endAt(round.getEndAt())
                  .application(application)
                  .build();
            })
            .toList();

    return ProjectApplicationDto.ChallengerApplicationSummary.builder()
        .challengerId(challenger.getId())
        .challengerName(challenger.getName())
        .challengerNickname(challenger.getNickname())
        .challengerPart(challenger.getPart())
        .challengerSchoolId(challenger.getSchool().getId())
        .challengerSchoolName(challenger.getSchool().getName())
        .memberProjects(memberProjects)
        .matchingRoundApplications(roundApplications)
        .build();
  }

  public ProjectApplicationDto.MinSelectionResponse getMinSelectionInMatchingRound(Long projectId,
      ChallengerPart challengerPart,
      Long matchingRoundId) {

    Project project = projectRepository.findById(projectId).orElseThrow(() -> new DomainException(
        DomainType.PROJECT,
        ErrorStatus.PROJECT_NOT_FOUND));

    ProjectMatchingRound matchingRound = projectMatchingRoundRepository.findById(matchingRoundId)
        .orElseThrow(() -> new DomainException(
            DomainType.PROJECT_MATCHING_ROUND,
            ErrorStatus.MATCHING_ROUND_NOT_FOUND
        ));

    // 주어진 파트의 TO 정보 조회
    ProjectTo projectTo = projectToRepository.findByProject(
            project)
        .stream()
        .filter(to -> to.getPart() == challengerPart)
        .findFirst()
        .orElseThrow(() -> new DomainException(DomainType.PROJECT,
            ErrorStatus.PROJECT_TO_NOT_FOUND));

    int maxTo = projectTo.getToCount();

    log.info("프로젝트 {} 파트 {} - 최대 TO: {}",
        project.getId(), challengerPart, maxTo);

    List<ProjectMember> currentMembers = projectMemberRepository.findAllByProjectAndChallengerPart(
        project, challengerPart);
    List<Challenger> currentMatchingAppliedChallengers = projectApplicationRepository.findChallengersByProjectAndPartAndRound(
        project, challengerPart, matchingRound);

    // 기존에 팀 멤버였던 사람 수 (현재 매칭 차수가 아닌 차수에서 합격한 사람)
    // 현재 프로젝트 멤버 중에서, 이번 매칭 차수에서 지원한 사람을 필터링 함 => 이번 차수에서 합격한 사람을 제외하고, 기존 합격자 수
    long originalTeamMemberCount = currentMembers.stream()
        .filter(member -> !currentMatchingAppliedChallengers.contains(member.getChallenger()))
        .count();

    // 매칭 차수에 지원한 총 인원 수
    long currentMatchingApplicantCount = projectApplicationRepository.countByFormProjectAndApplicantPartAndMatchingRound(
        project, challengerPart, matchingRound);

    // 매칭 차수에서 합격한 인원 수
    long currentMatchingConfirmedCount = projectApplicationRepository.countByFormProjectAndApplicantPartAndMatchingRoundAndStatus(
        project, challengerPart, matchingRound, ApplicationStatus.CONFIRMED);

    // 현재 매칭 차수에 할당된 TO 계산 (최대 TO - 기존 멤버 수)
    long currentMatchingRoundTo = maxTo - originalTeamMemberCount;

    long half = (long) Math.ceil(currentMatchingRoundTo * 0.5);
    long quarter = (long) Math.ceil(currentMatchingRoundTo * 0.25);

    // === 디자이너 매칭 로직 ===
    if (challengerPart == ChallengerPart.DESIGN) {
      // 최대 TO가 1명인 경우 최소 선발 인원 없음
      if (currentMatchingApplicantCount <= 1) {
        return ProjectApplicationDto.MinSelectionResponse
            .builder()
            .minSelectionCount(0L)
            .reason("[디자인 파트] 지원자가 1명 이하인 경우 최소 선발 인원이 존재하지 않습니다. - 매칭 차수 지원 인원 :"
                + currentMatchingApplicantCount + "명")
            .build();
      }
      // 지원자 2명 이상이면 1명 이상 선택해야 함

      return ProjectApplicationDto.MinSelectionResponse
          .builder()
          .minSelectionCount(1L)
          .reason("[디자인 파트] 지원자가 2명 이상인 경우 최소 1명을 선발해야 합니다. - 매칭 차수 지원 인원: "
              + currentMatchingApplicantCount + "명")
          .build();
    } else {
      // === 개발자 및 기타 파트 매칭 로직 ===
      // 지원자 >= TO인 경우: TO의 50% 이상 선택해야 함
      if (currentMatchingApplicantCount >= currentMatchingRoundTo) {
        // 이미 현재 차수에서 합격 시킨 인원을 최소 선발 인원에서 뺌
        long minSelection = Math.max(0L, half - currentMatchingConfirmedCount);

        return ProjectApplicationDto.MinSelectionResponse
            .builder()
            .minSelectionCount(minSelection)
            .reason(
                "[개발 파트] 지원자 수가 현재 매칭 차수에 부여된 TO 이상인 경우 TO의 50% 이상을 선발하여야 합니다. - 매칭 차수 지원 인원: "
                    +
                    currentMatchingApplicantCount + "명, 매칭 차수 TO: " + currentMatchingRoundTo
                    + "명, 최소 선발 인원: " + half + "명, 추가로 합격시켜야 하는 인원: " + minSelection + "명)")
            .build();
      }
      // 지원자 > TO의 50%인 경우: TO의 25% 이상 선택해야 함
      else if (currentMatchingApplicantCount > half) {
        // 이미 현재 차수에서 합격 시킨 인원을 최소 선발 인원에서 뺌
        long minSelection = Math.max(0L, quarter - currentMatchingConfirmedCount);

        return ProjectApplicationDto.MinSelectionResponse
            .builder()
            .minSelectionCount(minSelection)
            .reason(
                "[개발 파트] 지원자 수가 현재 매칭 차수에 부여된 TO의 50% 이상인 경우 TO의 25% 이상을 선발하여야 합니다. - 매칭 차수 지원 인원: "
                    +
                    currentMatchingApplicantCount + "명, 현재 매칭 차수 TO: " + currentMatchingRoundTo
                    + "명, 최소 선발 인원: " + quarter + "명, 추가로 합격시켜야 하는 인원: " + minSelection + "명)")
            .build();
      }
      // 지원자 <= TO의 50%인 경우: 제한 없음 (minSelection = 0)

      return ProjectApplicationDto.MinSelectionResponse
          .builder()
          .minSelectionCount(0L)
          .reason(
              "[개발 파트] 지원자 수가 현재 매칭 차수에 부여된 TO의 50% 미만인 경우 최소 선발 인원이 존재하지 않습니다. - 현재 매칭 차수 지원 인원: "
                  +
                  currentMatchingApplicantCount + "명, 현재 매칭 차수 TO: " + currentMatchingRoundTo
                  + "명, 최소 선발 인원: 0명)")
          .build();
    }
  }

  /**
   * 지원서 거절 가능 여부 검증
   *
   * @param application 지원서
   * @param newStatus   변경하려는 상태
   */
  private void validateApplicationRejectable(ProjectApplication application,
      ApplicationStatus newStatus) {
    // 거절이 아니면 검증 불필요
    if (newStatus != ApplicationStatus.REJECTED) {
      return;
    }

    Project project = application.getForm().getProject();
    // 지원자 파트
    ChallengerPart applicantPart = application.getApplicant().getPart();
    ProjectMatchingRound matchingRound = application.getMatchingRound();

    // 1. 해당 파트의 TO 정보 조회
    ProjectTo projectTo = projectToRepository.findByProject(
            project)
        .stream()
        .filter(to -> to.getPart() == applicantPart)
        .findFirst()
        .orElseThrow(() -> new DomainException(DomainType.PROJECT,
            ErrorStatus.PROJECT_TO_NOT_FOUND));

    int maxTo = projectTo.getToCount();

    // 2. 이전 차수에서 이미 합격한 팀원 수 (기존 팀 멤버)
    long originalTeamMemberCount = projectApplicationRepository.countByFormProjectAndApplicantPartAndMatchingRoundNotAndStatus(
        project, applicantPart, matchingRound, ApplicationStatus.CONFIRMED);

    // 3. 현재 차수의 총 지원자 수 (PENDING 상태)
    long totalApplicants = projectApplicationRepository.countByFormProjectAndApplicantPartAndMatchingRoundAndStatus(
        project, applicantPart, matchingRound, ApplicationStatus.PENDING);

    // 4. 현재 차수에서 이미 합격한 인원 수
    long currentMatchingConfirmedCount = projectApplicationRepository.countByFormProjectAndApplicantPartAndMatchingRoundAndStatus(
        project, applicantPart, matchingRound, ApplicationStatus.CONFIRMED);

    // 5. 현재 차수에 할당된 TO
    long currentMatchingRoundTo = maxTo - originalTeamMemberCount;

    log.info(
        "거절 가능 검증 | 프로젝트: {}, 파트: {}, 최대 TO: {}, 기존 팀원: {}, 현재 차수 TO: {}, 총 지원자: {}, 현재 합격: {}",
        project.getId(), applicantPart, maxTo, originalTeamMemberCount, currentMatchingRoundTo,
        totalApplicants, currentMatchingConfirmedCount);

    // === 디자이너 매칭 로직 ===
    if (applicantPart == ChallengerPart.DESIGN) {
      // 최대 TO가 1명인 경우 무조건 거절 가능
      if (maxTo == 1) {
        return;
      }

      // 지원자 2명 이상이면 1명 이상 선택해야 함
      if (totalApplicants >= 2 && currentMatchingConfirmedCount < 1) {
        throw new DomainException(DomainType.PROJECT_APPLICATION,
            ErrorStatus.PA_REJECT_NEED_MIN_SELECTION);
      }
    }
    // === 개발자 및 기타 파트 매칭 로직 ===
    else {
      long halfTo = (long) Math.ceil(currentMatchingRoundTo * 0.5);
      long quarterTo = (long) Math.ceil(currentMatchingRoundTo * 0.25);

      // 지원자 >= TO인 경우: TO의 50% 이상 선택해야 함
      if (totalApplicants >= currentMatchingRoundTo) {
        if (currentMatchingConfirmedCount < halfTo) {
          throw new DomainException(DomainType.PROJECT_APPLICATION,
              ErrorStatus.PA_REJECT_NEED_HALF_SELECTION);
        }
      }
      // 지원자 > TO의 50%인 경우: TO의 25% 이상 선택해야 함
      else if (totalApplicants > halfTo) {
        if (currentMatchingConfirmedCount < quarterTo) {
          throw new DomainException(DomainType.PROJECT_APPLICATION,
              ErrorStatus.PA_REJECT_NEED_QUARTER_SELECTION);
        }
      }
      // 지원자 <= TO의 50%인 경우: 제한 없음
    }
  }


  @Transactional
  public ProjectApplicationDto.Response update(
      Long id,
      ProjectApplicationDto.UpdateRequest request,
      UserPrincipal userPrincipal
  ) {
    // 1. 엔티티 조회
    ProjectApplication application = projectApplicationRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_APPLICATION,
            ErrorStatus.PROJECT_APPLICATION_NOT_FOUND));

    ApplicationStatus prevStatus = application.getStatus();
    ApplicationStatus newStatus = request.status();

    // 2. 변경 사항 없으면 에러
    if (prevStatus == newStatus) {
      throw new DomainException(DomainType.PROJECT_APPLICATION,
          ErrorStatus.PROJECT_APPLICATION_SAME_STATUS_UPDATE);
    }

    // 3. 권한 확인 변수 설정
    // 주의: roles가 비어있지 않다고 무조건 관리자는 아닐 수 있음 (ROLE_USER 등).
    // 실제 서비스에서는 role.contains("ROLE_ADMIN") 등으로 명확히 하는 게 좋음.
    boolean isAdmin = !userPrincipal.roles().isEmpty();
    Project project = application.getForm().getProject();
    Challenger applicant = application.getApplicant();

    ProjectMatchingRound appliedMatchingRound = application.getMatchingRound();
    // 매칭 차수가 종료 전인 경우에는 상태 변경을 허용하지 않음.
    if (appliedMatchingRound.getEndAt().isAfter(Instant.now())) {
      // 그치만 관리자는 무적임 ~
      if (!isAdmin) {
        log.info("운영진이 아닌 사용자가 매칭 차수가 종료되지 않은 상태에서 지원서 상태 변경을 요청했습니다. 챌린저 [{}], 매칭 차수 종료일 [{}]",
            userPrincipal.challengerId(), appliedMatchingRound.getEndAt());
        throw new DomainException(DomainType.PROJECT_APPLICATION,
            ErrorStatus.PROJECT_APPLICATION_STATUS_CHANGE_MATCHING_ROUND_NOT_ENDED);
      }
      log.info("관리자가 매칭 차수가 종료되지 않은 상태에서 지원서 상태 변경을 진행합니다. 챌린저 [{}], 매칭 차수 종료일 [{}]",
          userPrincipal.challengerId(), appliedMatchingRound.getEndAt());
    }

    // 또는 매칭 차수의 합/불 결정 기간이 경과한 경우에도 상태 변경을 허용하지 않음.
    if (appliedMatchingRound.getDecisionDeadlineAt() != null &&
        appliedMatchingRound.getDecisionDeadlineAt().isBefore(Instant.now())) {
      // 그치만 관리자는 무적임 ~
      if (!isAdmin) {
        log.info("운영진이 아닌 사용자가 매칭 차수의 합/불 결정 기간이 지난 후에 지원서 상태 변경을 요청했습니다. 챌린저 [{}], 결정 마감일 [{}]",
            userPrincipal.challengerId(), appliedMatchingRound.getDecisionDeadlineAt());
        throw new DomainException(DomainType.PROJECT_APPLICATION,
            ErrorStatus.PA_STATUS_CHANGE_AFTER_DECISION_DEADLINE_NOT_ALLOWED);
      }
      log.info("관리자가 매칭 차수의 합/불 결정 기간이 지난 후에 지원서 상태 변경을 진행합니다. 챌린저 [{}], 결정 마감일 [{}]",
          userPrincipal.challengerId(), appliedMatchingRound.getDecisionDeadlineAt());
    }

    // ==========================================
    // [공통] 상태 변경 전 거절 가능 여부 검증
    // ==========================================
    if (!isAdmin) {
      validateApplicationRejectable(application, newStatus);
    }

    // ==========================================
    // [Case A] 이미 처리된 지원서(합격/불합격)를 수정하려는 경우 -> 관리자만 가능
    // ==========================================
    if (prevStatus != ApplicationStatus.PENDING) {
      if (!isAdmin) {
        log.warn("일반 챌린저가 지원서 상태 변경을 시도했습니다. 챌린저 [{}]", userPrincipal.challengerId());
        throw new DomainException(DomainType.PROJECT_APPLICATION,
            ErrorStatus.PROJECT_APPLICATION_STATUS_NOT_PENDING);
      }

      // 관리자가 '합격'이었던 사람을 다른 상태로 바꿀 때 -> 멤버에서 강제 퇴출
      if (prevStatus == ApplicationStatus.CONFIRMED) {
        ProjectMember member = projectMemberRepository.findByProjectAndChallenger(project,
                applicant)
            .orElseThrow(() -> new DomainException(DomainType.PROJECT_APPLICATION,
                ErrorStatus.PA_STAT_CHANGE_WAS_NOT_MEMBER));

        projectMemberRepository.delete(member);
      }
    }

    // ==========================================
    // [Case B] 대기 중(PENDING)인 지원서를 심사하는 경우 -> 관리자 혹은 PO만 가능
    // ==========================================
    else { // prevStatus == PENDING
      Long poId = project.getProductOwner().getId();
      boolean isProjectOwner = poId.equals(userPrincipal.challengerId());

      // [중요 수정] 합격이든 거절이든 결정권자가 아니면 막아야 함!
      if (!isAdmin && !isProjectOwner) {
        throw new DomainException(DomainType.PROJECT_APPLICATION,
            ErrorStatus.PA_STAT_CHANGE_NO_PERMISSION);
      }
    }

    // ==========================================
    // [공통] 상태 변경에 따른 후속 조치 (멤버 추가)
    // ==========================================
    if (newStatus == ApplicationStatus.CONFIRMED) {
      // 이미 멤버인지 DB 쿼리로 가볍게 확인
      boolean isAlreadyMember = projectMemberRepository.existsByProjectAndChallenger(project,
          applicant);

      if (isAlreadyMember) {
        throw new DomainException(DomainType.PROJECT_APPLICATION,
            ErrorStatus.PROJECT_APPLICATION_STATUS_CHANGE_ALREADY_MEMBER);
      }

      // 프로젝트 TO가 남아있는지 확인
      ChallengerPart applicantPart = applicant.getPart();

      // 1. 해당 파트의 TO 조회
      ProjectTo projectTo =
          projectToRepository.findByProjectAndPart(project, applicantPart)
              .orElseThrow(() -> new DomainException(DomainType.PROJECT,
                  ErrorStatus.PROJECT_TO_NOT_FOUND));

      int maxTo = projectTo.getToCount();

      // 2. 현재 해당 파트의 멤버 수 조회
      long currentMemberCount = projectMemberRepository.countByProjectAndChallengerPart(
          project, applicantPart);

      // 3. TO 초과 여부 확인
      if (currentMemberCount >= maxTo) {
        throw new DomainException(DomainType.PROJECT_APPLICATION,
            ErrorStatus.PA_CANNOT_EXCEED_TO_COUNT);
      }

      log.info("프로젝트 {} 파트 {} - 현재 멤버: {}/{}",
          project.getId(), applicantPart, currentMemberCount, maxTo);

      ProjectMember newMember = ProjectMember.builder()
          .project(project)
          .challenger(applicant)
          .build();
      projectMemberRepository.save(newMember);
    }

    // ==========================================
    // [최종] 상태 업데이트 (Dirty Checking 사용)
    // ==========================================
    // 굳이 save()를 다시 호출하거나 객체를 새로 만들 필요 없음
    application.updateStatus(newStatus);

    return toResponse(application);
  }

  @Transactional
  public void delete(Long id) {
    if (!projectApplicationRepository.existsById(id)) {
      throw new DomainException(DomainType.PROJECT_APPLICATION,
          ErrorStatus.PROJECT_APPLICATION_NOT_FOUND);
    }
    projectApplicationRepository.deleteById(id);
  }

  private ProjectApplicationDto.Response toResponse(ProjectApplication application) {
    return ProjectApplicationDto.Response.builder()
        .id(application.getId())
        .formId(application.getForm().getId())
        .formTitle(application.getForm().getTitle())
        .projectId(application.getForm().getProject().getId())
        .projectName(application.getForm().getProject().getName())
        .applicantId(application.getApplicant().getId())
        .applicantName(application.getApplicant().getName())
        .applicantNickname(application.getApplicant().getNickname())
        .applicantPart(application.getApplicant().getPart())
        .applicantSchoolId(application.getApplicant().getSchool().getId())
        .applicantSchoolName(application.getApplicant().getSchool().getName())
        .matchingRoundId(application.getMatchingRound().getId())
        .matchingRoundName(application.getMatchingRound().getName())
        .status(application.getStatus())
        .createdAt(application.getCreatedAt())
        .updatedAt(application.getUpdatedAt())
        .build();
  }

  private ProjectApplicationDto.DetailResponse toDetailResponse(ProjectApplication application) {
    List<ProjectApplicationResponseDto.Response> responses =
        application.getApplicationResponses().stream()
            .map(this::toResponseDto)
            .toList();

    return ProjectApplicationDto.DetailResponse.builder()
        .id(application.getId())
        .formId(application.getForm().getId())
        .formTitle(application.getForm().getTitle())
        .projectId(application.getForm().getProject().getId())
        .projectName(application.getForm().getProject().getName())
        .applicantId(application.getApplicant().getId())
        .applicantName(application.getApplicant().getName())
        .applicantNickname(application.getApplicant().getNickname())
        .applicantPart(application.getApplicant().getPart())
        .applicantSchoolId(application.getApplicant().getSchool().getId())
        .applicantSchoolName(application.getApplicant().getSchool().getName())
        .matchingRoundId(application.getMatchingRound().getId())
        .matchingRoundName(application.getMatchingRound().getName())
        .status(application.getStatus())
        .responses(responses)
        .createdAt(application.getCreatedAt())
        .updatedAt(application.getUpdatedAt())
        .build();
  }

  private ProjectApplicationResponseDto.Response toResponseDto(
      ProjectApplicationResponse response) {
    return ProjectApplicationResponseDto.Response.builder()
        .id(response.getId())
        .applicationId(response.getApplication().getId())
        .questionId(response.getQuestion().getId())
        .questionTitle(response.getQuestion().getTitle())
        .values(response.getValues())
        .createdAt(response.getCreatedAt())
        .updatedAt(response.getUpdatedAt())
        .build();
  }
}
