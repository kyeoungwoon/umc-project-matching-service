package kr.kyeoungwoon.upms.domain.challenger.entity;

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
import kr.kyeoungwoon.upms.domain.projectApplication.entity.ProjectApplication;
import kr.kyeoungwoon.upms.global.entity.BaseEntity;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;
import kr.kyeoungwoon.upms.global.enums.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "challenger", uniqueConstraints = {
    @UniqueConstraint(name = "uniq_school_student", columnNames = {"school_id", "student_id",
        "gisu"})
})
public class Challenger extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "umsb_id")
  private Long umsbId;

  @Column(nullable = false)
  private Long gisu;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChallengerPart part;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String nickname;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Gender gender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "school_id", nullable = false)
  private School school;

  @Column(name = "student_id", nullable = false)
  private String studentId;

  @Column(nullable = false)
  private String password;

  @Column(name = "profile_image_url")
  private String profileImageUrl;

  @OneToMany(mappedBy = "applicant", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<ProjectApplication> challengerApplications = new ArrayList<>();

  @Builder
  private Challenger(Long umsbId, Long gisu, ChallengerPart part, String name,
      String nickname, Gender gender, School school, String studentId,
      String password, String profileImageUrl) {
    this.umsbId = umsbId;
    this.gisu = gisu;
    this.part = part;
    this.name = name;
    this.nickname = nickname;
    this.gender = gender;
    this.school = school;
    this.studentId = studentId;
    this.password = password;
    this.profileImageUrl = profileImageUrl;
  }

  // 비즈니스 메서드
  public void updateProfile(String name, String nickname, String profileImageUrl) {
    if (name != null) {
      this.name = name;
    }
    if (nickname != null) {
      this.nickname = nickname;
    }
    if (profileImageUrl != null) {
      this.profileImageUrl = profileImageUrl;
    }
  }

  public void changePassword(String password) {
    if (password != null) {
      this.password = password;
    }
  }

  public void changePart(ChallengerPart part) {
    if (part != null) {
      this.part = part;
    }
  }

  public void changeGender(Gender gender) {
    if (gender != null) {
      this.gender = gender;
    }
  }
}
