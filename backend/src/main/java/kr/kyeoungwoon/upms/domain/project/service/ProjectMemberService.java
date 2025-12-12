package kr.kyeoungwoon.upms.domain.project.service;

import kr.kyeoungwoon.upms.domain.challenger.entity.Challenger;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChallengerRepository;
import kr.kyeoungwoon.upms.domain.project.dto.ProjectMemberDto;
import kr.kyeoungwoon.upms.domain.project.entity.Project;
import kr.kyeoungwoon.upms.domain.project.entity.ProjectMember;
import kr.kyeoungwoon.upms.domain.project.repository.ProjectMemberRepository;
import kr.kyeoungwoon.upms.domain.project.repository.ProjectRepository;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import kr.kyeoungwoon.upms.global.apiPayload.exception.DomainException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectMemberService {

  private final ProjectMemberRepository projectMemberRepository;
  private final ProjectRepository projectRepository;
  private final ChallengerRepository challengerRepository;

  @Transactional
  public ProjectMemberDto.Response create(ProjectMemberDto.CreateRequest request) {
    log.info("프로젝트 멤버 추가 요청 - projectId: {}, challengerId: {}, active: {}",
        request.projectId(), request.challengerId(), request.active());
    Project project = projectRepository.findById(request.projectId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_NOT_FOUND));

    Challenger challenger = challengerRepository.findById(request.challengerId())
        .orElseThrow(() -> new DomainException(DomainType.CHALLENGER,
            ErrorStatus.CHALLENGER_NOT_FOUND));

    ProjectMember projectMember = ProjectMember.builder()
        .project(project)
        .challenger(challenger)
        .active(request.active())
        .build();

    ProjectMember saved = projectMemberRepository.save(projectMember);
    log.info("프로젝트 멤버 추가 완료 - id: {}", saved.getId());
    return toResponse(saved);
  }

  public ProjectMemberDto.Response findById(Long id) {
    log.info("프로젝트 멤버 조회 - id: {}", id);
    ProjectMember projectMember = projectMemberRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_MEMBER,
            ErrorStatus.PROJECT_MEMBER_NOT_FOUND));
    return toResponse(projectMember);
  }

  public Page<ProjectMemberDto.Response> findAll(Pageable pageable) {
    log.info("프로젝트 멤버 페이징 조회 - page: {}, size: {}", pageable.getPageNumber(),
        pageable.getPageSize());
    return projectMemberRepository.findAll(pageable)
        .map(this::toResponse);
  }

  @Transactional
  public ProjectMemberDto.Response update(Long id, ProjectMemberDto.UpdateRequest request) {
    log.info("프로젝트 멤버 수정 요청 - id: {}, active: {}", id, request.active());
    ProjectMember projectMember = projectMemberRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_MEMBER,
            ErrorStatus.PROJECT_MEMBER_NOT_FOUND));

    ProjectMember updated = ProjectMember.builder()
        .id(projectMember.getId())
        .project(projectMember.getProject())
        .challenger(projectMember.getChallenger())
        .active(request.active())
        .build();

    ProjectMember saved = projectMemberRepository.save(updated);
    log.info("프로젝트 멤버 수정 완료 - id: {}", saved.getId());
    return toResponse(saved);
  }

  @Transactional
  public void delete(Long id) {
    log.info("프로젝트 멤버 삭제 요청 - id: {}", id);
    if (!projectMemberRepository.existsById(id)) {
      throw new DomainException(DomainType.PROJECT_MEMBER, ErrorStatus.PROJECT_MEMBER_NOT_FOUND);
    }
    projectMemberRepository.deleteById(id);
  }

  public void throwIfAlreadyProjectMember(Long challengerId) {
    log.info("챌린저 중복 멤버 여부 검증 - challengerId: {}", challengerId);
    boolean exists = projectMemberRepository.existsByChallengerId(challengerId);
    if (exists) {
      log.warn("이미 프로젝트 멤버로 등록된 챌린저 - challengerId: {}", challengerId);
      throw new DomainException(DomainType.PROJECT_MEMBER,
          ErrorStatus.ALREADY_PROJECT_MEMBER);
    }
  }

  private ProjectMemberDto.Response toResponse(ProjectMember projectMember) {
    return ProjectMemberDto.Response.builder()
        .id(projectMember.getId())
        .projectId(projectMember.getProject().getId())
        .projectName(projectMember.getProject().getName())
        .challengerId(projectMember.getChallenger().getId())
        .challengerName(projectMember.getChallenger().getName())
        .active(projectMember.isActive())
        .createdAt(projectMember.getCreatedAt())
        .updatedAt(projectMember.getUpdatedAt())
        .build();
  }
}
