package kr.kyeoungwoon.upms.domain.challenger.entity;

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
import kr.kyeoungwoon.upms.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "school", uniqueConstraints = {
    @UniqueConstraint(name = "uniq_school_name", columnNames = "name")})
public class School extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(name = "logo_image_url")
  private String logoImageUrl;

  @OneToMany(mappedBy = "school", fetch = FetchType.LAZY)
  private final List<Challenger> challengers = new ArrayList<>();

  @Builder
  private School(String name, String logoImageUrl) {
    this.name = name;
    this.logoImageUrl = logoImageUrl;
  }

  // 비즈니스 메서드
  public void updateInfo(String name, String logoImageUrl) {
    if (name != null) {
      this.name = name;
    }
    if (logoImageUrl != null) {
      this.logoImageUrl = logoImageUrl;
    }
  }
}
