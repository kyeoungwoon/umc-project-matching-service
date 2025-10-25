package kr.kyeoungwoon.upms.domain.projectApplicationForm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.kyeoungwoon.upms.global.entity.BaseEntity;
import kr.kyeoungwoon.upms.global.enums.QuestionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "form_question")
public class ApplicationFormQuestion extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "form_id", nullable = false)
  private ProjectApplicationForm form;

  @Column(name = "question_no", nullable = false)
  private Integer questionNo;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "text")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private QuestionType type;

  @JdbcTypeCode(SqlTypes.ARRAY)
  @Column(name = "options", columnDefinition = "text[]")
  private String[] options;

  @Column(name = "is_required")
  private boolean required;

  @Column(name = "is_deleted")
  private boolean deleted;
}
