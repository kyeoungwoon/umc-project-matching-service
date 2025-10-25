package kr.kyeoungwoon.upms.domain.challenger.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.kyeoungwoon.upms.global.entity.BaseEntity;
import kr.kyeoungwoon.upms.global.enums.ChapterAdminRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChapterAdmin extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "chapter_id", nullable = false)
  private Chapter chapter;

  @ManyToOne
  @JoinColumn(name = "challenger_id", nullable = false)
  private Challenger challenger;

  @Enumerated(EnumType.STRING)
  private ChapterAdminRole role;

  @Builder
  private ChapterAdmin(Long id, Chapter chapter, Challenger challenger, ChapterAdminRole role) {
    this.id = id;
    this.chapter = chapter;
    this.challenger = challenger;
    this.role = role;
  }
}
