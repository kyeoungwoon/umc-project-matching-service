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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectToService {

  private final ProjectToRepository projectToRepository;
  private final ProjectRepository projectRepository;

  @Transactional
  public ProjectToDto.Response create(ProjectToDto.CreateRequest request) {
    Project project = projectRepository.findById(request.projectId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_NOT_FOUND));

    ProjectTo projectTo = ProjectTo.builder()
        .project(project)
        .part(request.part())
        .toCount(request.toCount())
        .build();

    ProjectTo saved = projectToRepository.save(projectTo);
    return toResponse(saved);
  }

  @Transactional
  public List<ProjectToDto.Response> bulkCreate(ProjectToDto.BulkCreateRequest request) {
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
    return savedList.stream()
        .map(this::toResponse)
        .toList();
  }

  public ProjectToDto.Response findById(Long id) {
    ProjectTo projectTo = projectToRepository.findById(id)
        .orElseThrow(
            () -> new DomainException(DomainType.PROJECT_TO, ErrorStatus.PROJECT_TO_NOT_FOUND));
    return toResponse(projectTo);
  }

  public List<ProjectToDto.Response> findAll(Long projectId) {
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
    return toResponse(saved);
  }

  @Transactional
  public void delete(Long id) {
    if (!projectToRepository.existsById(id)) {
      throw new DomainException(DomainType.PROJECT_TO, ErrorStatus.PROJECT_TO_NOT_FOUND);
    }
    projectToRepository.deleteById(id);
  }

  public void throwIfProjectToFull(Long projectId, ChallengerPart challengerPart) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_NOT_FOUND));
    List<ProjectTo> projectTos = projectToRepository.findByProject(project);
    List<ProjectMember> projectMembers = project.getMembers();

    for (ProjectTo projectTo : projectTos) {
      if (projectTo.getPart() == challengerPart) {
        long currentCount = projectMembers.stream()
            .filter(member -> member.getChallenger().getPart() == challengerPart)
            .count();
        if (currentCount >= projectTo.getToCount()) {
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
