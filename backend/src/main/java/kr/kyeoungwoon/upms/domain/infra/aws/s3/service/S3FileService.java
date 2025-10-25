package kr.kyeoungwoon.upms.domain.infra.aws.s3.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import kr.kyeoungwoon.upms.domain.infra.aws.s3.dto.S3FileDto;
import kr.kyeoungwoon.upms.domain.infra.aws.s3.entity.S3File;
import kr.kyeoungwoon.upms.domain.infra.aws.s3.enums.FileStatus;
import kr.kyeoungwoon.upms.domain.infra.aws.s3.repository.S3FileRepository;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import kr.kyeoungwoon.upms.global.apiPayload.exception.DomainException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class S3FileService {

  private final S3FileRepository s3FileRepository;
  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.cloudfront.domain}")
  private String cloudFrontDomain;

  private static final int PRESIGNED_URL_EXPIRATION_MINUTES = 2;

  /**
   * Presigned URL 생성 (업로드용)
   */
  @Transactional
  public S3FileDto.PresignedUrlResponse generatePresignedUrl(
      S3FileDto.PresignedUrlRequest request) {
    // 고유한 파일명 생성
    String storedFileName = UUID.randomUUID().toString();
    String extension = extractExtension(request.getFileName());
    if (!extension.isEmpty()) {
      storedFileName += "." + extension;
    }

    // S3 Key 생성 (날짜 기반 경로)
    String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    String s3Key = String.format("uploads/%s/%s", datePath, storedFileName);

    // Presigned URL 생성
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(s3Key)
        .contentType(request.getContentType())
        .build();

    PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(PRESIGNED_URL_EXPIRATION_MINUTES))
        .putObjectRequest(putObjectRequest)
        .build();

    PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
    String presignedUrl = presignedRequest.url().toString();

    // DB에 파일 메타데이터 저장 (PENDING 상태)
    S3File s3FileEntity = S3File.builder()
        .originalFileName(request.getFileName())
        .storedFileName(storedFileName)
        .s3Key(s3Key)
        .contentType(request.getContentType())
        .fileSize(request.getFileSize())
        .status(FileStatus.PENDING)
        .build();

    S3File savedS3File = s3FileRepository.save(s3FileEntity);

    return S3FileDto.PresignedUrlResponse.builder()
        .fileId(savedS3File.getId())
        .presignedUrl(presignedUrl)
        .s3Key(s3Key)
        .expiresInSeconds(PRESIGNED_URL_EXPIRATION_MINUTES * 60)
        .build();
  }

  /**
   * 업로드 완료 확인 및 상태 업데이트
   */
  @Transactional
  public S3FileDto.FileResponse confirmUpload(Long fileId) {
    S3File s3FileEntity = s3FileRepository.findById(fileId)
        .orElseThrow(() -> new DomainException(DomainType.INFRA_S3, ErrorStatus.FILE_NOT_FOUND));

    // S3에 실제로 파일이 존재하는지 확인
    if (checkFileExistsInS3(s3FileEntity.getS3Key())) {
      s3FileEntity.updateStatus(FileStatus.UPLOADED);
      return toFileResponse(s3FileEntity);
    } else {
      s3FileEntity.updateStatus(FileStatus.FAILED);
      throw new DomainException(DomainType.INFRA_S3, ErrorStatus.FILE_NOT_UPLOADED);
    }
  }

  /**
   * 파일 조회 (CloudFront URL 반환)
   */
  public S3FileDto.FileResponse getFile(Long fileId) {
    S3File s3FileEntity = s3FileRepository.findById(fileId)
        .orElseThrow(() -> new DomainException(DomainType.INFRA_S3, ErrorStatus.FILE_NOT_FOUND));

    return toFileResponse(s3FileEntity);
  }

  /**
   * S3 Key로 CloudFront URL 생성
   */
  public String getCloudFrontUrl(String s3Key) {
    return cloudFrontDomain + "/" + s3Key;
  }

  /**
   * S3에 파일 존재 여부 확인
   */
  private boolean checkFileExistsInS3(String s3Key) {
    try {
      HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
          .bucket(bucket)
          .key(s3Key)
          .build();
      s3Client.headObject(headObjectRequest);
      return true;
    } catch (NoSuchKeyException e) {
      return false;
    }
  }

  private S3FileDto.FileResponse toFileResponse(S3File entity) {
    String cdnUrl = null;
    if (entity.getStatus() == FileStatus.UPLOADED) {
      cdnUrl = getCloudFrontUrl(entity.getS3Key());
    }

    return S3FileDto.FileResponse.builder()
        .id(entity.getId())
        .originalFileName(entity.getOriginalFileName())
        .contentType(entity.getContentType())
        .fileSize(entity.getFileSize())
        .cdnUrl(cdnUrl)
        .status(entity.getStatus().name())
        .build();
  }

  private String extractExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf('.');
    if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
      return fileName.substring(dotIndex + 1).toLowerCase();
    }
    return "";
  }
}
