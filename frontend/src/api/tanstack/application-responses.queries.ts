/**
 * 프로젝트 지원서 응답 React Query Hooks
 */
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import { applicationResponseApi } from '@api/axios/application-responses.api';
import {
  ProjectApplicationResponseBulkCreateRequest,
  ProjectApplicationResponseCreateRequest,
  ProjectApplicationResponseUpdateRequest,
} from '@api/types/application';

import {
  applicationQueryKeys,
  applicationResponseQueryKeys,
} from '@common/constants/query-key.constants';

// 지원서 응답 목록 조회
export const useGetApplicationResponses = (applicationId?: string) => {
  return useQuery({
    queryKey: applicationResponseQueryKeys.list(applicationId),
    queryFn: () => applicationResponseApi.getApplicationResponses(applicationId),
  });
};

// 지원서 응답 단건 조회
export const useGetApplicationResponse = (id: string, enabled = true) => {
  return useQuery({
    queryKey: applicationResponseQueryKeys.detail(id),
    queryFn: () => applicationResponseApi.getApplicationResponse(id),
    enabled,
  });
};

// 지원서 응답 생성
export const useCreateApplicationResponse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ProjectApplicationResponseCreateRequest) =>
      applicationResponseApi.createApplicationResponse(request),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: applicationResponseQueryKeys.lists() });
      if (data.applicationId) {
        queryClient.invalidateQueries({
          queryKey: applicationResponseQueryKeys.list(data.applicationId),
        });
        queryClient.invalidateQueries({
          queryKey: applicationQueryKeys.detail(data.applicationId),
        });
      }
      queryClient.invalidateQueries({ queryKey: applicationQueryKeys.formLists() });
    },
  });
};

export const useCreateBulkApplicationResponse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ProjectApplicationResponseBulkCreateRequest) =>
      applicationResponseApi.createBulkApplicationResponse(request),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: applicationResponseQueryKeys.lists() });
      if (data.applicationId) {
        queryClient.invalidateQueries({
          queryKey: applicationResponseQueryKeys.list(data.applicationId),
        });
        queryClient.invalidateQueries({
          queryKey: applicationQueryKeys.detail(data.applicationId),
        });
      }
      queryClient.invalidateQueries({ queryKey: applicationQueryKeys.formLists() });
    },
  });
};

// 지원서 응답 수정
export const useUpdateApplicationResponse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      id,
      request,
    }: {
      id: string;
      request: ProjectApplicationResponseUpdateRequest;
    }) => applicationResponseApi.updateApplicationResponse(id, request),
    onSuccess: (data, variables) => {
      queryClient.invalidateQueries({ queryKey: applicationResponseQueryKeys.lists() });
      queryClient.invalidateQueries({
        queryKey: applicationResponseQueryKeys.detail(variables.id),
      });
      if (data.applicationId) {
        queryClient.invalidateQueries({
          queryKey: applicationResponseQueryKeys.list(data.applicationId),
        });
        queryClient.invalidateQueries({
          queryKey: applicationQueryKeys.detail(data.applicationId),
        });
      }
      queryClient.invalidateQueries({ queryKey: applicationQueryKeys.formLists() });
    },
  });
};

// 지원서 응답 삭제
export const useDeleteApplicationResponse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, applicationId }: { id: string; applicationId?: string }) =>
      applicationResponseApi.deleteApplicationResponse(id),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: applicationResponseQueryKeys.lists() });
      queryClient.invalidateQueries({
        queryKey: applicationResponseQueryKeys.detail(variables.id),
      });
      if (variables.applicationId) {
        queryClient.invalidateQueries({
          queryKey: applicationResponseQueryKeys.list(variables.applicationId),
        });
        queryClient.invalidateQueries({
          queryKey: applicationQueryKeys.detail(variables.applicationId),
        });
      }
      queryClient.invalidateQueries({ queryKey: applicationQueryKeys.formLists() });
    },
  });
};
