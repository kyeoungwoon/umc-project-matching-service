package kr.kyeoungwoon.upms.domain.infra.aws.s3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class S3FileDto {

  // Presigned URL 요청
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PresignedUrlRequest {

    private String fileName;
    private String contentType;
    private Long fileSize;
  }

  // Presigned URL 응답
  @Getter
  @Builder
  @AllArgsConstructor
  public static class PresignedUrlResponse {

    private Long fileId;
    private String presignedUrl;
    private String s3Key;
    private Integer expiresInSeconds;
  }

  // 업로드 완료 확인 요청
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UploadCompleteRequest {

    private Long fileId;
  }

  // 파일 정보 응답
  @Getter
  @Builder
  @AllArgsConstructor
  public static class FileResponse {

    private Long id;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String cdnUrl;          // CloudFront URL
    private String status;
  }
}