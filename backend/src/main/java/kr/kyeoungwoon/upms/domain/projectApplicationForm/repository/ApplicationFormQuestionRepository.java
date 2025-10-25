package kr.kyeoungwoon.upms.domain.projectApplicationForm.repository;

import java.util.List;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.entity.ApplicationFormQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationFormQuestionRepository extends
    JpaRepository<ApplicationFormQuestion, Long> {

  /**
   * 특정 폼의 질문 목록 조회 (질문 번호 순으로 정렬)
   */
  List<ApplicationFormQuestion> findByFormIdOrderByQuestionNoAsc(Long formId);

  /**
   * 특정 폼의 삭제되지 않은 질문 목록 조회 (질문 번호 순으로 정렬)
   */
  List<ApplicationFormQuestion> findByFormIdAndDeletedFalseOrderByQuestionNoAsc(Long formId);

  /**
   * 특정 폼의 모든 질문 조회
   */
  List<ApplicationFormQuestion> findByFormId(Long formId);
}
