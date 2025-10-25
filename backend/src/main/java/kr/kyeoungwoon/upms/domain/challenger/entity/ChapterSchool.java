package kr.kyeoungwoon.upms.domain.challenger.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.kyeoungwoon.upms.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chapter_school", uniqueConstraints = {
    @UniqueConstraint(name = "uniq_chapter_school", columnNames = {"chapter_id", "school_id"})
})
public class ChapterSchool extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chapter_id", nullable = false)
  private Chapter chapter;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "school_id", nullable = false)
  private School school;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "leader_id")
  private Challenger leader;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "vice_leader_id")
  private Challenger viceLeader;

  @Builder
  private ChapterSchool(Chapter chapter, School school, Challenger leader, Challenger viceLeader) {
    this.chapter = chapter;
    this.school = school;
    this.leader = leader;
    this.viceLeader = viceLeader;
  }

  // 비즈니스 메서드
  public void updateLeaders(Challenger leader, Challenger viceLeader) {
    if (leader != null) {
      this.leader = leader;
    }
    if (viceLeader != null) {
      this.viceLeader = viceLeader;
    }
  }
}
