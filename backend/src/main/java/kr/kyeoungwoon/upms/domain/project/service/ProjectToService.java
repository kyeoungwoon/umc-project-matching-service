package kr.kyeoungwoon.upms.domain.project.service;

import java.util.List;
import kr.kyeoungwoon.upms.domain.project.dto.ProjectToDto;
import kr.kyeoungwoon.upms.domain.project.entity.Project;
import kr.kyeoungwoon.upms.domain.project.entity.ProjectMember;
import kr.kyeoungwoon.upms.domain.project.entity.ProjectTo;
import kr.kyeoungwoon.upms.domain.project.repository.ProjectRepository;
import kr.kyeoungwoon.upms.domain.project.repository.ProjectToRepository;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import kr.kyeoungwoon.upms.global.apiPayload.exception.DomainException;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectToService {

  private final ProjectToRepository projectToRepository;
  private final ProjectRepository projectRepository;

  @Transactional
  public ProjectToDto.Response create(ProjectToDto.CreateRequest request) {
    log.info("프로젝트 TO 생성 요청 - projectId: {}, part: {}, toCount: {}", request.projectId(),
        request.part(), request.toCount());
    Project project = projectRepository.findById(request.projectId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_NOT_FOUND));

    ProjectTo projectTo = ProjectTo.builder()
        .project(project)
        .part(request.part())
        .toCount(request.toCount())
        .build();

    ProjectTo saved = projectToRepository.save(projectTo);
    log.info("프로젝트 TO 생성 완료 - id: {}", saved.getId());
    return toResponse(saved);
  }

  @Transactional
  public List<ProjectToDto.Response> bulkCreate(ProjectToDto.BulkCreateRequest request) {
    log.info("프로젝트 TO 일괄 생성 요청 - projectId: {}, 건수: {}", request.projectId(),
        request.toItems().size());
    Project project = projectRepository.findById(request.projectId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_NOT_FOUND));

    List<ProjectTo> projectTos = request.toItems().stream()
        .map(item -> ProjectTo.builder()
            .project(project)
            .part(item.part())
            .toCount(item.toCount())
            .build())
        .toList();

    List<ProjectTo> savedList = projectToRepository.saveAll(projectTos);
    log.info("프로젝트 TO 일괄 생성 완료 - 저장 개수: {}", savedList.size());
    return savedList.stream()
        .map(this::toResponse)
        .toList();
  }

  public ProjectToDto.Response findById(Long id) {
    log.info("프로젝트 TO 조회 - id: {}", id);
    ProjectTo projectTo = projectToRepository.findById(id)
        .orElseThrow(
            () -> new DomainException(DomainType.PROJECT_TO, ErrorStatus.PROJECT_TO_NOT_FOUND));
    return toResponse(projectTo);
  }

  public List<ProjectToDto.Response> findAll(Long projectId) {
    log.info("프로젝트 TO 목록 조회 - projectId 필터: {}", projectId);
    if (projectId != null) {
      Project project = projectRepository.findById(projectId)
          .orElseThrow(
              () -> new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_NOT_FOUND));
      return projectToRepository.findByProject(project).stream()
          .map(this::toResponse)
          .toList();
    }
    return projectToRepository.findAll().stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional
  public ProjectToDto.Response update(Long id, ProjectToDto.UpdateRequest request) {
    log.info("프로젝트 TO 수정 요청 - id: {}, toCount: {}", id, request.toCount());
    ProjectTo projectTo = projectToRepository.findById(id)
        .orElseThrow(
            () -> new DomainException(DomainType.PROJECT_TO, ErrorStatus.PROJECT_TO_NOT_FOUND));

    ProjectTo updated = ProjectTo.builder()
        .id(projectTo.getId())
        .project(projectTo.getProject())
        .part(projectTo.getPart())
        .toCount(request.toCount() != null ? request.toCount() : projectTo.getToCount())
        .build();

    ProjectTo saved = projectToRepository.save(updated);
    log.info("프로젝트 TO 수정 완료 - id: {}", saved.getId());
    return toResponse(saved);
  }

  @Transactional
  public void delete(Long id) {
    log.info("프로젝트 TO 삭제 요청 - id: {}", id);
    if (!projectToRepository.existsById(id)) {
      throw new DomainException(DomainType.PROJECT_TO, ErrorStatus.PROJECT_TO_NOT_FOUND);
    }
    projectToRepository.deleteById(id);
  }

  public void throwIfProjectToFull(Long projectId, ChallengerPart challengerPart) {
    log.info("프로젝트 TO 여유 검증 - projectId: {}, part: {}", projectId, challengerPart);
    // id로 프로젝트를 가져옵니다.
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_NOT_FOUND));

    // 프로젝트의 TO를 가져옵니다.
    List<ProjectTo> projectTos = projectToRepository.findByProject(project);
    // 프로젝트의 멤버를 가져옵니다.
    List<ProjectMember> projectMembers = project.getMembers();

    // 프로젝트 TO 들에 대해서 for loop
    // to와 member를 비교하면서 count
    for (ProjectTo projectTo : projectTos) {
      if (projectTo.getPart() == challengerPart) {
        long currentCount = projectMembers.stream()
            .filter(member -> member.getChallenger().getPart() == challengerPart)
            .count();
        if (currentCount >= projectTo.getToCount()) {
          log.warn("프로젝트 TO 초과 - projectId: {}, part: {}, 현재인원: {}, 최대인원: {}", projectId,
              challengerPart, currentCount, projectTo.getToCount());
          throw new DomainException(DomainType.PROJECT_TO, ErrorStatus.APPLICATION_PROJECT_TO_FULL);
        }
      }
    }
  }

  private ProjectToDto.Response toResponse(ProjectTo projectTo) {
    return ProjectToDto.Response.builder()
        .id(projectTo.getId())
        .projectId(projectTo.getProject().getId())
        .projectName(projectTo.getProject().getName())
        .part(projectTo.getPart())
        .toCount(projectTo.getToCount())
        .createdAt(projectTo.getCreatedAt())
        .updatedAt(projectTo.getUpdatedAt())
        .build();
  }
}
