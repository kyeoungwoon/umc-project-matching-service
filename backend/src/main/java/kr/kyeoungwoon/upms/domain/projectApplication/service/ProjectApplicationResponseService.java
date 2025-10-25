package kr.kyeoungwoon.upms.domain.projectApplication.service;

import java.util.List;
import kr.kyeoungwoon.upms.domain.projectApplication.dto.ProjectApplicationResponseDto;
import kr.kyeoungwoon.upms.domain.projectApplication.entity.ProjectApplication;
import kr.kyeoungwoon.upms.domain.projectApplication.entity.ProjectApplicationResponse;
import kr.kyeoungwoon.upms.domain.projectApplication.repository.ProjectApplicationRepository;
import kr.kyeoungwoon.upms.domain.projectApplication.repository.ProjectApplicationResponseRepository;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.entity.ApplicationFormQuestion;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.repository.ApplicationFormQuestionRepository;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import kr.kyeoungwoon.upms.global.apiPayload.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectApplicationResponseService {

  private final ProjectApplicationResponseRepository projectApplicationResponseRepository;
  private final ProjectApplicationRepository projectApplicationRepository;
  private final ApplicationFormQuestionRepository applicationFormQuestionRepository;

  @Transactional
  public ProjectApplicationResponseDto.Response create(
      ProjectApplicationResponseDto.CreateRequest request) {
    ProjectApplication application = projectApplicationRepository.findById(request.applicationId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_APPLICATION,
            ErrorStatus.PROJECT_APPLICATION_NOT_FOUND));

    ApplicationFormQuestion question = applicationFormQuestionRepository.findById(
            request.questionId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_APPLICATION_FORM,
            ErrorStatus.APPLICATION_FORM_QUESTION_NOT_FOUND));

    ProjectApplicationResponse response = ProjectApplicationResponse.builder()
        .application(application)
        .question(question)
        .values(request.values())
        .build();

    ProjectApplicationResponse saved = projectApplicationResponseRepository.save(response);
    return toResponse(saved);
  }

  @Transactional
  public List<ProjectApplicationResponseDto.Response> bulkCreate(
      ProjectApplicationResponseDto.BulkCreateRequest request) {
    ProjectApplication application = projectApplicationRepository.findById(request.applicationId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_APPLICATION,
            ErrorStatus.PROJECT_APPLICATION_NOT_FOUND));

    List<ProjectApplicationResponse> responses = request.responses().stream()
        .map(item -> {
          ApplicationFormQuestion question = applicationFormQuestionRepository.findById(
                  item.questionId())
              .orElseThrow(() -> new DomainException(DomainType.PROJECT_APPLICATION_FORM,
                  ErrorStatus.APPLICATION_FORM_QUESTION_NOT_FOUND));

          return ProjectApplicationResponse.builder()
              .application(application)
              .question(question)
              .values(item.values())
              .build();
        })
        .toList();

    List<ProjectApplicationResponse> savedResponses =
        projectApplicationResponseRepository.saveAll(responses);

    return savedResponses.stream()
        .map(this::toResponse)
        .toList();
  }

  public ProjectApplicationResponseDto.Response findById(Long id) {
    ProjectApplicationResponse response = projectApplicationResponseRepository.findById(id)
        .orElseThrow(
            () -> new DomainException(DomainType.PROJECT_APPLICATION_RESPONSE,
                ErrorStatus.PROJECT_APPLICATION_RESPONSE_NOT_FOUND));
    return toResponse(response);
  }

  public List<ProjectApplicationResponseDto.Response> findAll(Long applicationId) {
    if (applicationId != null) {
      return projectApplicationResponseRepository.findByApplicationId(applicationId).stream()
          .map(this::toResponse)
          .toList();
    }
    return projectApplicationResponseRepository.findAll().stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional
  public ProjectApplicationResponseDto.Response update(Long id,
      ProjectApplicationResponseDto.UpdateRequest request) {
    ProjectApplicationResponse response = projectApplicationResponseRepository.findById(id)
        .orElseThrow(
            () -> new DomainException(DomainType.PROJECT_APPLICATION_RESPONSE,
                ErrorStatus.PROJECT_APPLICATION_RESPONSE_NOT_FOUND));

    ProjectApplicationResponse updated = ProjectApplicationResponse.builder()
        .id(response.getId())
        .application(response.getApplication())
        .question(response.getQuestion())
        .values(request.values() != null ? request.values() : response.getValues())
        .build();

    ProjectApplicationResponse saved = projectApplicationResponseRepository.save(updated);
    return toResponse(saved);
  }

  @Transactional
  public void delete(Long id) {
    if (!projectApplicationResponseRepository.existsById(id)) {
      throw new DomainException(DomainType.PROJECT_APPLICATION_RESPONSE,
          ErrorStatus.PROJECT_APPLICATION_RESPONSE_NOT_FOUND);
    }
    projectApplicationResponseRepository.deleteById(id);
  }

  private ProjectApplicationResponseDto.Response toResponse(ProjectApplicationResponse response) {
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
