package kr.kyeoungwoon.upms.domain.infra.aws.s3.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.kyeoungwoon.upms.domain.infra.aws.s3.dto.S3FileDto;
import kr.kyeoungwoon.upms.domain.infra.aws.s3.dto.S3FileDto.PresignedUrlResponse;
import kr.kyeoungwoon.upms.domain.infra.aws.s3.service.S3FileService;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Infra: S3", description = "AWS S3")
@RestController
@RequestMapping("/infra/s3/files")
@RequiredArgsConstructor
public class FileController {

  private final S3FileService s3FileService;

  /**
   * Presigned URL 발급 클라이언트는 이 URL로 직접 S3에 파일을 업로드
   */
  @PostMapping("/presigned-url")
  public ApiResponse<PresignedUrlResponse> getPresignedUrl(
      @RequestBody S3FileDto.PresignedUrlRequest request) {

    S3FileDto.PresignedUrlResponse response = s3FileService.generatePresignedUrl(request);
    return ApiResponse.onSuccess(response);
  }

  /**
   * 업로드 완료 확인 클라이언트가 S3 업로드 완료 후 호출
   */
  @PostMapping("/{fileId}/confirm")
  public ApiResponse<S3FileDto.FileResponse> confirmUpload(@PathVariable Long fileId) {
    S3FileDto.FileResponse response = s3FileService.confirmUpload(fileId);
    return ApiResponse.onSuccess(response);
  }

  /**
   * 파일 정보 조회 (CloudFront URL 포함)
   */
  @GetMapping("/{fileId}")
  public ApiResponse<S3FileDto.FileResponse> getFile(@PathVariable Long fileId) {
    S3FileDto.FileResponse response = s3FileService.getFile(fileId);
    return ApiResponse.onSuccess(response);
  }
}