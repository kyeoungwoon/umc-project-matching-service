package kr.kyeoungwoon.upms.domain.projectApplicationForm.service;

import java.util.List;
import kr.kyeoungwoon.upms.domain.project.entity.Project;
import kr.kyeoungwoon.upms.domain.project.repository.ProjectRepository;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.dto.ProjectApplicationFormDto;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.entity.ProjectApplicationForm;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.repository.ProjectApplicationFormRepository;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import kr.kyeoungwoon.upms.global.apiPayload.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectApplicationFormService {

  private final ProjectApplicationFormRepository projectApplicationFormRepository;
  private final ProjectRepository projectRepository;

  @Transactional
  public ProjectApplicationFormDto.Response create(
      ProjectApplicationFormDto.CreateRequest request) {
    Project project = projectRepository.findById(request.projectId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT, ErrorStatus.PROJECT_NOT_FOUND));

    ProjectApplicationForm form = ProjectApplicationForm.builder()
        .project(project)
        .title(request.title())
        .description(request.description())
        .build();

    ProjectApplicationForm saved = projectApplicationFormRepository.save(form);
    return toResponse(saved);
  }

  public ProjectApplicationFormDto.Response findById(Long id) {
    ProjectApplicationForm form = projectApplicationFormRepository.findById(id)
        .orElseThrow(
            () -> new DomainException(DomainType.PROJECT_APPLICATION_FORM,
                ErrorStatus.PROJECT_APPLICATION_FORM_NOT_FOUND));
    return toResponse(form);
  }

  /**
   * 지원서 폼 목록 조회
   *
   * @param projectId 프로젝트 ID (optional) - 제공되면 해당 프로젝트의 폼만 조회
   */
  public List<ProjectApplicationFormDto.Response> findAll(Long projectId) {
    List<ProjectApplicationForm> forms;

    if (projectId != null) {
      forms = projectApplicationFormRepository.findByProjectId(projectId);
    } else {
      forms = projectApplicationFormRepository.findAll();
    }

    return forms.stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional
  public ProjectApplicationFormDto.Response update(Long id,
      ProjectApplicationFormDto.UpdateRequest request) {
    ProjectApplicationForm form = projectApplicationFormRepository.findById(id)
        .orElseThrow(
            () -> new DomainException(DomainType.PROJECT_APPLICATION_FORM,
                ErrorStatus.PROJECT_APPLICATION_FORM_NOT_FOUND));

    ProjectApplicationForm updated = ProjectApplicationForm.builder()
        .id(form.getId())
        .project(form.getProject())
        .title(request.title() != null ? request.title() : form.getTitle())
        .description(request.description() != null ? request.description() : form.getDescription())
        .questions(form.getQuestions())
        .applications(form.getApplications())
        .build();

    ProjectApplicationForm saved = projectApplicationFormRepository.save(updated);
    return toResponse(saved);
  }

  @Transactional
  public void delete(Long id) {
    if (!projectApplicationFormRepository.existsById(id)) {
      throw new DomainException(DomainType.PROJECT_APPLICATION_FORM,
          ErrorStatus.PROJECT_APPLICATION_FORM_NOT_FOUND);
    }
    projectApplicationFormRepository.deleteById(id);
  }
  
  private ProjectApplicationFormDto.Response toResponse(ProjectApplicationForm form) {
    return ProjectApplicationFormDto.Response.builder()
        .id(form.getId())
        .projectId(form.getProject().getId())
        .projectName(form.getProject().getName())
        .title(form.getTitle())
        .description(form.getDescription())
        .createdAt(form.getCreatedAt())
        .updatedAt(form.getUpdatedAt())
        .build();
  }
}
