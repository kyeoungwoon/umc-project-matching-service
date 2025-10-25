package kr.kyeoungwoon.upms.domain.project.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import kr.kyeoungwoon.upms.domain.challenger.entity.Chapter;
import kr.kyeoungwoon.upms.domain.projectApplicationForm.entity.ProjectApplicationForm;
import kr.kyeoungwoon.upms.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "project", uniqueConstraints = {
    @UniqueConstraint(name = "uniq_chapter_project_name", columnNames = {"chapter_id", "name"})
})
public class Project extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "text")
  private String description;

  @JoinColumn(name = "product_owner_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Challenger productOwner;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chapter_id", nullable = false)
  private Chapter chapter;

  @Column(name = "logo_image_url")
  private String logoImageUrl;

  @Column(name = "banner_image_url")
  private String bannerImageUrl;

  @Column(name = "notion_link")
  private String notionLink;

  @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<ProjectTo> tos = new ArrayList<>();

  @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<ProjectMember> members = new ArrayList<>();

  @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<ProjectApplicationForm> applicationForms = new ArrayList<>();

  @Builder
  private Project(String name, String description, Challenger productOwner,
      Chapter chapter, String logoImageUrl, String bannerImageUrl, String notionLink) {
    this.name = name;
    this.description = description;
    this.productOwner = productOwner;
    this.chapter = chapter;
    this.logoImageUrl = logoImageUrl;
    this.bannerImageUrl = bannerImageUrl;
    this.notionLink = notionLink;
  }

  // 비즈니스 메서드
  public void updateBasicInfo(String name, String description) {
    if (name != null) {
      this.name = name;
    }
    if (description != null) {
      this.description = description;
    }
  }

  public void changeOwner(Challenger productOwner) {
    if (productOwner != null) {
      this.productOwner = productOwner;
    }
  }

  public void updateImages(String logoImageUrl, String bannerImageUrl) {
    if (logoImageUrl != null) {
      this.logoImageUrl = logoImageUrl;
    }
    if (bannerImageUrl != null) {
      this.bannerImageUrl = bannerImageUrl;
    }
  }

  public void updateNotionLink(String notionLink) {
    if (notionLink != null) {
      this.notionLink = notionLink;
    }
  }
}
