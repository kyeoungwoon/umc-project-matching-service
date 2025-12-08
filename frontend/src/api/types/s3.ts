/**
 * S3 파일 업로드 관련 타입 정의
 */

export interface PresignedUrlRequest {
  fileName: string;
  contentType: string;
  fileSize: number;
}

export interface PresignedUrlResponse {
  fileId: number;
  presignedUrl: string;
  s3Key: string;
  expiresInSeconds: number;
}

export interface FileResponse {
  id: number;
  originalFileName: string;
  contentType: string;
  fileSize: number;
  cdnUrl: string;
  status: string;
}
