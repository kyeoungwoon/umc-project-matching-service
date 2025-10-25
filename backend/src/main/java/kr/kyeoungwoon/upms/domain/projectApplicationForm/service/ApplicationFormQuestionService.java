package kr.kyeoungwoon.upms.domain.projectApplicationForm.service;

import java.util.List;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.dto.ApplicationFormQuestionDto;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.entity.ApplicationFormQuestion;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.entity.ProjectApplicationForm;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.repository.ApplicationFormQuestionRepository;
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
public class ApplicationFormQuestionService {

  private final ApplicationFormQuestionRepository applicationFormQuestionRepository;
  private final ProjectApplicationFormRepository projectApplicationFormRepository;

  @Transactional
  public ApplicationFormQuestionDto.Response create(
      ApplicationFormQuestionDto.CreateRequest request) {
    ProjectApplicationForm form = projectApplicationFormRepository.findById(request.formId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_APPLICATION_FORM,
            ErrorStatus.PROJECT_APPLICATION_FORM_NOT_FOUND));

    ApplicationFormQuestion question = buildQuestionFromRequest(form, request);
    ApplicationFormQuestion saved = applicationFormQuestionRepository.save(question);

    return toResponse(saved);
  }

  @Transactional
  public List<ApplicationFormQuestionDto.Response> createBulk(
      ApplicationFormQuestionDto.BulkCreateRequest request) {

    ProjectApplicationForm form = projectApplicationFormRepository.findById(request.formId())
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_APPLICATION_FORM,
            ErrorStatus.PROJECT_APPLICATION_FORM_NOT_FOUND));

    // 1. 기존 질문들을 모두 isDeleted = true 처리
    List<ApplicationFormQuestion> existingQuestions =
        applicationFormQuestionRepository.findByFormId(request.formId());

    List<ApplicationFormQuestion> deletedQuestions = existingQuestions.stream()
        .map(q -> ApplicationFormQuestion.builder()
            .id(q.getId())
            .form(q.getForm())
            .questionNo(q.getQuestionNo())
            .title(q.getTitle())
            .description(q.getDescription())
            .type(q.getType())
            .options(q.getOptions())
            .required(q.isRequired())
            .deleted(true)  // 삭제 처리
            .build())
        .toList();

    applicationFormQuestionRepository.saveAll(deletedQuestions);

    // 2. 새로운 질문들 생성
    List<ApplicationFormQuestion> questions = request.questions().stream()
        .map(item -> buildQuestionFromBulkItem(form, item))
        .toList();

    List<ApplicationFormQuestion> savedQuestions =
        applicationFormQuestionRepository.saveAll(questions);

    return savedQuestions.stream()
        .map(this::toResponse)
        .toList();
  }


  public ApplicationFormQuestionDto.Response findById(Long id) {
    ApplicationFormQuestion question = applicationFormQuestionRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_APPLICATION_FORM,
            ErrorStatus.APPLICATION_FORM_QUESTION_NOT_FOUND));
    return toResponse(question);
  }

  public List<ApplicationFormQuestionDto.Response> findAllByFormId(Long formId) {
    // 삭제되지 않은 질문만 조회
    return applicationFormQuestionRepository.findByFormIdAndDeletedFalseOrderByQuestionNoAsc(formId)
        .stream()
        .map(this::toResponse)
        .toList();
  }


  @Transactional
  public ApplicationFormQuestionDto.Response update(Long id,
      ApplicationFormQuestionDto.UpdateRequest request) {
    ApplicationFormQuestion question = applicationFormQuestionRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.PROJECT_APPLICATION_FORM,
            ErrorStatus.APPLICATION_FORM_QUESTION_NOT_FOUND));

    // Builder로 새 객체 생성하여 변경사항 반영
    ApplicationFormQuestion updated = ApplicationFormQuestion.builder()
        .id(question.getId())
        .form(question.getForm())
        .questionNo(request.questionNo() != null ? request.questionNo() : question.getQuestionNo())
        .title(request.title() != null ? request.title() : question.getTitle())
        .description(
            request.description() != null ? request.description() : question.getDescription())
        .type(request.type() != null ? request.type() : question.getType())
        .options(request.options() != null ? request.options() : question.getOptions())
        .required(request.required() != null ? request.required() : question.isRequired())
        .deleted(request.deleted() != null ? request.deleted() : question.isDeleted())
        .build();

    ApplicationFormQuestion saved = applicationFormQuestionRepository.save(updated);
    return toResponse(saved);
  }

  @Transactional
  public void delete(Long id) {
    if (!applicationFormQuestionRepository.existsById(id)) {
      throw new DomainException(DomainType.PROJECT_APPLICATION_FORM,
          ErrorStatus.APPLICATION_FORM_QUESTION_NOT_FOUND);
    }
    applicationFormQuestionRepository.deleteById(id);
  }

  /**
   * CreateRequest로부터 ApplicationFormQuestion 엔티티 생성
   */
  private ApplicationFormQuestion buildQuestionFromRequest(
      ProjectApplicationForm form,
      ApplicationFormQuestionDto.CreateRequest request) {
    return ApplicationFormQuestion.builder()
        .form(form)
        .questionNo(request.questionNo())
        .title(request.title())
        .description(request.description())
        .type(request.type())
        .options(request.options())
        .required(request.required())
        .build();
  }

  /**
   * BulkCreateRequest.QuestionItem으로부터 ApplicationFormQuestion 엔티티 생성
   */
  private ApplicationFormQuestion buildQuestionFromBulkItem(
      ProjectApplicationForm form,
      ApplicationFormQuestionDto.BulkCreateRequest.QuestionItem item) {
    return ApplicationFormQuestion.builder()
        .form(form)
        .questionNo(item.questionNo())
        .title(item.title())
        .description(item.description())
        .type(item.type())
        .options(item.options())
        .required(item.required())
        .build();
  }

  private ApplicationFormQuestionDto.Response toResponse(ApplicationFormQuestion question) {
    return ApplicationFormQuestionDto.Response.builder()
        .id(question.getId())
        .formId(question.getForm().getId())
        .formTitle(question.getForm().getTitle())
        .questionNo(question.getQuestionNo())
        .title(question.getTitle())
        .description(question.getDescription())
        .type(question.getType())
        .options(question.getOptions())
        .required(question.isRequired())
        .deleted(question.isDeleted())
        .createdAt(question.getCreatedAt())
        .updatedAt(question.getUpdatedAt())
        .build();
  }
}
