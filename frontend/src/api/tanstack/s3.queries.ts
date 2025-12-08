/**
 * S3 파일 업로드 React Query Hooks
 */
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import { s3Api } from '@api/axios/s3.api';
import type { PresignedUrlRequest } from '@api/types/s3';

import { s3QueryKeys } from '@common/constants/query-key.constants';

// 파일 정보 조회
export const useGetFile = (fileId: number, enabled = true) => {
  return useQuery({
    queryKey: s3QueryKeys.file(fileId),
    queryFn: () => s3Api.getFile(fileId),
    enabled,
  });
};

// Presigned URL 생성
export const useGetPresignedUrl = () => {
  return useMutation({
    mutationFn: (request: PresignedUrlRequest) => s3Api.getPresignedUrl(request),
  });
};

// S3에 파일 업로드
export const useUploadToS3 = () => {
  return useMutation({
    mutationFn: ({ presignedUrl, file }: { presignedUrl: string; file: File }) =>
      s3Api.uploadToS3(presignedUrl, file),
  });
};

// 파일 업로드 확정
export const useConfirmUpload = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (fileId: number) => s3Api.confirmUpload(fileId),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: s3QueryKeys.file(data.id) });
    },
  });
};

// 전체 파일 업로드 프로세스 (Presigned URL 생성 → S3 업로드 → 확정)
export const useUploadFile = () => {
  const getPresignedUrl = useGetPresignedUrl();
  const uploadToS3 = useUploadToS3();
  const confirmUpload = useConfirmUpload();

  return useMutation({
    mutationFn: async (file: File) => {
      // 1. Presigned URL 생성
      const presignedUrlResponse = await getPresignedUrl.mutateAsync({
        fileName: file.name,
        contentType: file.type,
        fileSize: file.size,
      });

      // 2. S3에 파일 업로드
      await uploadToS3.mutateAsync({
        presignedUrl: presignedUrlResponse.presignedUrl,
        file,
      });

      // 3. 업로드 확정
      return await confirmUpload.mutateAsync(presignedUrlResponse.fileId);
    },
  });
};
