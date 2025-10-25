package kr.kyeoungwoon.upms.domain.infra.aws.s3.enums;

public enum FileStatus {
  PENDING,    // presigned URL 발급됨, 업로드 대기중
  UPLOADED,   // 업로드 완료
  FAILED,     // 업로드 실패
  DELETED     // 삭제됨
}