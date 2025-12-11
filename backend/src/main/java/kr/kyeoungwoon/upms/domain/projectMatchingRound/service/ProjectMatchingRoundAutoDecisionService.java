package kr.kyeoungwoon.upms.domain.projectMatchingRound.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kr.kyeoungwoon.upms.domain.project.entity.Project;
import kr.kyeoungwoon.upms.domain.project.entity.ProjectMember;
import kr.kyeoungwoon.upms.domain.project.entity.ProjectTo;
import kr.kyeoungwoon.upms.domain.project.repository.ProjectMemberRepository;
import kr.kyeoungwoon.upms.domain.project.repository.ProjectToRepository;
import kr.kyeoungwoon.upms.domain.projectApplication.entity.ProjectApplication;
import kr.kyeoungwoon.upms.domain.projectApplication.repository.ProjectApplicationRepository;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.entity.ProjectMatchingRound;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.repository.ProjectMatchingRoundRepository;
import kr.kyeoungwoon.upms.global.enums.ApplicationStatus;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectMatchingRoundAutoDecisionService {

  private final ProjectMatchingRoundRepository projectMatchingRoundRepository;
  private final ProjectApplicationRepository projectApplicationRepository;
  private final ProjectToRepository projectToRepository;
  private final ProjectMemberRepository projectMemberRepository;

  /**
   * 마감이 지난 매칭 라운드에 대해 자동 합/불 처리를 실행합니다.
   */
  @Transactional
  public void processExpiredRounds(Instant now) {
    List<ProjectMatchingRound> rounds = projectMatchingRoundRepository.findRoundsToAutoDecide(now);
    if (rounds.isEmpty()) {
      return;
    }

    log.info("[매칭 차수 종료 Scheduler] Plan 결정 마감 시간이 지난 매칭 라운드가 {}개 있습니다.", rounds.size());
    for (ProjectMatchingRound round : rounds) {
      try {
        processSingleRound(round.getId());
      } catch (Exception e) {
        log.error("[매칭 차수 종료 Scheduler] 매칭 라운드 {}에 대한 처리를 실패하였습니다.", round.getId(), e);
      }
    }
  }

  @Transactional
  protected void processSingleRound(Long matchingRoundId) {
    ProjectMatchingRound round = projectMatchingRoundRepository.findById(matchingRoundId)
        .orElse(null);
    if (round == null || Boolean.TRUE.equals(round.getIsAutoDecisionExecuted())) {
      return;
    }

    // 아직 상태변경을 하지 않은 지원서를 조회합니다
    List<ProjectApplication> pendingApplications =
        projectApplicationRepository.findByMatchingRoundIdAndStatus(
            matchingRoundId, ApplicationStatus.PENDING);

    // 처리해야할 지원서가 없다면 바로 종료합니다
    if (pendingApplications.isEmpty()) {
      round.markAutoDecisionExecuted();
      return;
    }

    Map<Project, List<ProjectTo>> projectToCache = new HashMap<>();

    Map<Project, Map<ChallengerPart, List<ProjectApplication>>> groupedApplications =
        pendingApplications.stream()
            // 프로젝트 별로 1차 분류를 하고
            .collect(Collectors.groupingBy(
                pa -> pa.getForm().getProject(),
                // 그 안에서 파트별로 2차 분류
                Collectors.groupingBy(pa -> pa.getApplicant().getPart())
            ));

    groupedApplications.forEach((project, partMap) -> {
      // groupedApplication은 결국 프로젝트 - (파트, 파트별 지원서) map 형태
      // 각 프로젝트에 대해서 나열하게 됨

      // HashMap 캐시를 통해서 프로젝트 TO 목록을 가져옴
      List<ProjectTo> tos = projectToCache.computeIfAbsent(project,
          projectToRepository::findByProject);

      // 파트별 지원서 처리
      partMap.forEach((part, applications) -> {
        ProjectTo projectTo = tos.stream()
            .filter(to -> Objects.equals(to.getPart(), part))
            .findFirst()
            .orElse(null);

        // projectTo가 존재하지 않으면 모두 불합격 처리 (이러면 안됨)
        if (projectTo == null) {
          log.error(
              "[매칭 차수 종료 Scheduler] 접수된 지원서에 대한 파트가 프로젝트 TO 목록에 존재하지 않습니다. projectId={}, part={}",
              project.getId(),
              part);
          rejectAll(applications);
          return;
        }

        handlePartDecisions(round, project, part, projectTo, applications);
      });
    });

    round.markAutoDecisionExecuted();
  }

  private void handlePartDecisions(
      ProjectMatchingRound round,
      Project project,
      ChallengerPart part,
      ProjectTo projectTo,
      List<ProjectApplication> applications
  ) {
    // 기존 라운드에서 이미 합격한 인원
    long previouslyConfirmed = projectApplicationRepository
        .countByFormProjectAndApplicantPartAndMatchingRoundNotAndStatus(
            project, part, round, ApplicationStatus.CONFIRMED);

    // 현재 라운드에서 이미 확정된 인원
    long currentConfirmed = projectApplicationRepository
        .countByFormProjectAndApplicantPartAndMatchingRoundAndStatus(
            project, part, round, ApplicationStatus.CONFIRMED);

    // 현재 라운드에서 지원한 총 인원
    long currentApplied = projectApplicationRepository
        .countByFormProjectAndApplicantPartAndMatchingRound(
            project, part, round);

    // 프로젝트 TO
    int maxTo = projectTo.getToCount();
    // 남은 TO 계산, 음수면 0으로 처리함 (TO- 예전에 합격한 사람 - 현재 합격한 사람)

    // 이번 매칭 차수에 할당된 TO
    long currentMatchingRoundTo = Math.max(0, maxTo - previouslyConfirmed);

    // 최소 선발 인원 계산
    long minSelectCount;
    // 디자인 파트 매칭의 경우
    if (part == ChallengerPart.DESIGN) {
      // 만약에 디자인 파트 TO가 1명인 경우 최소 선발 인원 없음
      if (maxTo == 1) {
        minSelectCount = 0;
      } else {
        // 디자인 파트 TO가 2명 이상인 경우, 최소 한 명은 선발하여야 함
        minSelectCount = 1;
      }
    }
    // 개발자 매칭의 경우
    else {
      long halfTo = (long) Math.ceil(currentMatchingRoundTo * 0.5);
      long quarterTo = (long) Math.ceil(currentMatchingRoundTo * 0.25);

      // 현재 매칭 차수의 TO보다 지원자가 많은 경우 최소 선발 인원은 50%
      if (currentApplied >= currentMatchingRoundTo) {
        minSelectCount = halfTo;
      } else if (currentApplied > halfTo) {
        // 지원자가 TO의 50% 이상인 경우 최소 선발은 25%
        minSelectCount = quarterTo;
      } else {
        // 아니면 없음
        minSelectCount = 0;
      }
    }

    // 지원서들을 무작위로 섞어서 반환
    // 결정 마감 기한까지 지원서에 대한 합/불 결정을 하지 않으면 지원자 중에서 최소 선발 인원만큼 랜덤으로 합격 처리 합니다
    List<ProjectApplication> randomList = new ArrayList<>(applications);
    Collections.shuffle(randomList);

    // 합격시킬 인원 계산 (최소 선발 인원 vs 지원서 개수)
    // 최소 선발 인원 "만큼" 은 뽑아야 하니까, 현재 상황에서 추가로 선발해야 하는 인원을 고려함.
    // 즉, minSelectCount에서 currentConfirmed를 뺀 값이 됨 (0 이하로는 떨어지면 안되므로 0과 max 비교)

    // 추가: 최소 선발인원만큼 이미 뽑은 상황도 생각해야함. 따라서 minSelectCount - currentConfirmed와 비교하여야 함
    int toConfirm = (int) Math.max(minSelectCount - currentConfirmed, 0);

    List<ProjectApplication> confirmTargets = randomList.subList(0, toConfirm);
    List<ProjectApplication> rejectTargets = randomList.subList(toConfirm, randomList.size());

    log.info(
        "[매칭 차수 종료 Scheduler] MatchingRoundId {}, 프로젝트 ID {}, Part {}: 총 지원자 {}, 기존 합격 인원 {}, 현재 매칭 차수 합격 인원 {}, 최소 선발 인원 {}, 추가로 합격 처리해야 하는 인원 {}",
        round.getId(), project.getId(), part, applications.size(),
        previouslyConfirmed, currentConfirmed, minSelectCount, toConfirm);

    confirmTargets.forEach(application -> confirmApplication(application, projectTo));
    rejectTargets.forEach(this::rejectApplication);
  }

  private void confirmApplication(ProjectApplication application, ProjectTo projectTo) {
    Project project = application.getForm().getProject();
    ChallengerPart applicantPart = application.getApplicant().getPart();

    long currentMembers = projectMemberRepository.countByProjectAndChallengerPart(
        project, applicantPart);
    if (currentMembers >= projectTo.getToCount()) {
      log.warn(
          "[매칭 차수 종료 Scheduler] 합격 처리 시도 중 프로젝트 TO가 모두 찼습니다. projectId={}, part={}, applicantId={}",
          project.getId(), applicantPart, application.getApplicant().getId());
      rejectApplication(application);
      return;
    }

    // 이미 해당 프로젝트의 멤버가 아닌 경우, 멤버로 추가하기
    if (!projectMemberRepository.existsByProjectAndChallenger(project,
        application.getApplicant())) {
      ProjectMember member = ProjectMember.builder()
          .project(project)
          .challenger(application.getApplicant())
          .build();
      projectMemberRepository.save(member);
    }
    // dirty checking으로 status update
    application.updateStatus(ApplicationStatus.CONFIRMED);

    log.info(
        "[매칭 차수 종료 Scheduler] 지원서를 합격 처리하였습니다. applicationId={}, projectId={}, applicantId={}",
        application.getId(), project.getId(), application.getApplicant().getId());
  }

  private void rejectApplication(ProjectApplication application) {
    application.updateStatus(ApplicationStatus.REJECTED);

    log.info(
        "[매칭 차수 종료 Scheduler] 지원서를 불합격 처리하였습니다. applicationId={}, projectId={}, applicantId={}",
        application.getId(),
        application.getForm().getProject().getId(),
        application.getApplicant().getId());
  }

  private void rejectAll(List<ProjectApplication> applications) {
    applications.forEach(this::rejectApplication);

    log.info(
        "[매칭 차수 종료 Scheduler] 지원서 파트 전체를 불합격 처리하였습니다. applicationCount={}",
        applications.size());
  }
}
