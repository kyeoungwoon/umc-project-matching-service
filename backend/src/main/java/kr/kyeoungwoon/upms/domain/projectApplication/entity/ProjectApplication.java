package kr.kyeoungwoon.upms.domain.projectApplication.entity;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.entity.Challenger;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.entity.ProjectApplicationForm;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.entity.ProjectMatchingRound;
import kr.kyeoungwoon.upms.global.entity.BaseEntity;
import kr.kyeoungwoon.upms.global.enums.ApplicationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "project_application", uniqueConstraints = {
    @UniqueConstraint(name = "block_duplicate_application_in_same_round", columnNames = {
        "applicant_id", "matching_round_id"
    })
})
public class ProjectApplication extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "form_id", nullable = false)
  private ProjectApplicationForm form;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "applicant_id", nullable = false)
  private Challenger applicant;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "matching_round_id", nullable = false)
  private ProjectMatchingRound matchingRound;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ApplicationStatus status = ApplicationStatus.PENDING;

  @Builder.Default
  @OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<ProjectApplicationResponse> applicationResponses = new ArrayList<>();

  // 비즈니스 메서드
  public void updateStatus(ApplicationStatus newStatus) {
    this.status = newStatus;
  }
}
