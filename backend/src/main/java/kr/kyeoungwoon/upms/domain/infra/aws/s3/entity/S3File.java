package kr.kyeoungwoon.upms.domain.infra.aws.s3.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.kyeoungwoon.upms.domain.infra.aws.s3.enums.FileStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "files")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class S3File {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String originalFileName;  // 원본 파일명

  @Column(nullable = false, unique = true)
  private String storedFileName;    // S3에 저장된 파일명 (UUID)

  @Column(nullable = false)
  private String s3Key;             // S3 객체 경로 (예: uploads/2024/01/uuid.jpg)

  @Column(nullable = false)
  private String contentType;       // MIME 타입

  private Long fileSize;            // 파일 크기 (bytes)

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FileStatus status;        // 업로드 상태

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public void updateStatus(FileStatus status) {
    this.status = status;
  }
}