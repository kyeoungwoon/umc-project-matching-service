/**
 * 프로젝트 지원 React Query Hooks
 */
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import { applicationApi } from '@api/axios/applications.api';
import type {
  ProjectApplicationCreateRequest,
  ProjectApplicationUpdateRequest,
} from '@api/types/application';

import { applicationQueryKeys } from '@common/constants/query-key.constants';

// 지원서 목록 조회
export const useGetApplications = (
  chapterId?: string,
  projectId?: string,
  challengerId?: string,
) => {
  return useQuery({
    queryKey: [
      ...applicationQueryKeys.list(),
      {
        chapterId: chapterId ?? null,
        projectId: projectId ?? null,
        challengerId: challengerId ?? null,
      },
    ],
    queryFn: () => applicationApi.getApplications(chapterId, projectId, challengerId),
  });
};

export const useGetMyApplications = () => {
  return useQuery({
    queryKey: applicationQueryKeys.my(),
    queryFn: () => applicationApi.getMyApplications(),
  });
};

// 폼별 지원서 상세 목록 조회
export const useGetApplicationsByForm = (formId: string, enabled = true) => {
  return useQuery({
    queryKey: applicationQueryKeys.formList(formId),
    queryFn: () => applicationApi.getApplicationsByForm(formId),
    enabled,
  });
};

// 챕터별 지원 현황 요약 조회
export const useGetChapterApplicationSummary = (chapterId: string, enabled = true) => {
  return useQuery({
    queryKey: applicationQueryKeys.chapterSummary(chapterId),
    queryFn: () => applicationApi.getChapterApplicationSummary(chapterId),
    enabled,
  });
};

// 지원서 단건 조회
export const useGetApplication = (id: string, enabled = true) => {
  return useQuery({
    queryKey: applicationQueryKeys.detail(id),
    queryFn: () => applicationApi.getApplication(id),
    enabled,
  });
};

// 지원서 제출
export const useCreateApplication = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ProjectApplicationCreateRequest) =>
      applicationApi.createApplication(request),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: applicationQueryKeys.lists() });
      if (variables.formId) {
        queryClient.invalidateQueries({
          queryKey: applicationQueryKeys.formList(variables.formId),
        });
      }
      queryClient.invalidateQueries({ queryKey: applicationQueryKeys.formLists() });
    },
  });
};

// 지원서 상태 수정
export const useUpdateApplication = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, request }: { id: string; request: ProjectApplicationUpdateRequest }) =>
      applicationApi.updateApplication(id, request),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: applicationQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: applicationQueryKeys.formLists() });
      queryClient.invalidateQueries({
        queryKey: applicationQueryKeys.detail(variables.id),
      });
    },
  });
};

// 지원서 삭제
export const useDeleteApplication = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => applicationApi.deleteApplication(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: applicationQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: applicationQueryKeys.formLists() });
      queryClient.invalidateQueries({ queryKey: applicationQueryKeys.my() });
    },
  });
};
