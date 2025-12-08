/**
 * S3 파일 업로드 API
 */
import { BaseApi } from '@api/base/BaseApi';
import type { FileResponse, PresignedUrlRequest, PresignedUrlResponse } from '@api/types/s3';

class S3Api extends BaseApi {
  // Presigned URL 생성
  async getPresignedUrl(request: PresignedUrlRequest): Promise<PresignedUrlResponse> {
    return this.post<PresignedUrlResponse>('/infra/s3/files/presigned-url', request);
  }

  // 파일 업로드 확정
  async confirmUpload(fileId: number): Promise<FileResponse> {
    return this.post<FileResponse>(`/infra/s3/files/${fileId}/confirm`);
  }

  // 파일 정보 조회
  async getFile(fileId: number): Promise<FileResponse> {
    return this.get<FileResponse>(`/infra/s3/files/${fileId}`);
  }

  // S3에 파일 업로드 (Presigned URL 사용)
  async uploadToS3(presignedUrl: string, file: File): Promise<void> {
    // S3 업로드는 BaseApi를 사용하지 않고 직접 fetch 사용
    const response = await fetch(presignedUrl, {
      method: 'PUT',
      body: file,
      headers: {
        'Content-Type': file.type,
      },
    });

    if (!response.ok) {
      throw new Error('파일 업로드에 실패하였습니다.');
    }
  }
}

export const s3Api = new S3Api();
