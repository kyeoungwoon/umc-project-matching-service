package kr.kyeoungwoon.upms.domain.challenger.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import kr.kyeoungwoon.upms.domain.project.entity.Project;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.entity.ProjectMatchingRound;
import kr.kyeoungwoon.upms.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chapter", uniqueConstraints = {
    @UniqueConstraint(name = "uniq_gisu_chapter_name", columnNames = {"gisu", "name"})
})
public class Chapter extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private String description;

  @Column(nullable = false)
  private Long gisu;

  @OneToMany(mappedBy = "chapter", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Project> projects = new ArrayList<>();

  @OneToMany(mappedBy = "chapter", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<ProjectMatchingRound> matchingRounds = new ArrayList<>();

  @OneToMany(mappedBy = "chapter", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<ChapterSchool> chapterSchools = new ArrayList<>();

  @Builder
  private Chapter(String name, String description, Long gisu) {
    this.name = name;
    this.description = description;
    this.gisu = gisu;
  }

  // 비즈니스 메서드
  public void updateInfo(String name, String description) {
    if (name != null) {
      this.name = name;
    }
    if (description != null) {
      this.description = description;
    }
  }
}
