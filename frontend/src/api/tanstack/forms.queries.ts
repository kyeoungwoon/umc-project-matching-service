/**
 * 프로젝트 지원 폼 React Query Hooks
 */
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import { formApi } from '@api/axios/forms.api';
import type {
  ProjectApplicationFormCreateRequest,
  ProjectApplicationFormUpdateRequest,
} from '@api/types/form';
import { formQueryKeys } from '@common/constants/query-key.constants';

// 지원서 폼 목록 조회
export const useGetForms = (projectId?: string) => {
  return useQuery({
    queryKey: formQueryKeys.list(projectId),
    queryFn: () => formApi.getForms(projectId),
  });
};

// 지원서 폼 단건 조회
export const useGetForm = (id: string, enabled = true) => {
  return useQuery({
    queryKey: formQueryKeys.detail(id),
    queryFn: () => formApi.getForm(id),
    enabled,
  });
};

// 지원서 폼 생성
export const useCreateForm = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ProjectApplicationFormCreateRequest) => formApi.createForm(request),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: formQueryKeys.lists() });
    },
  });
};

// 지원서 폼 수정
export const useUpdateForm = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, request }: { id: string; request: ProjectApplicationFormUpdateRequest }) =>
      formApi.updateForm(id, request),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: formQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: formQueryKeys.detail(variables.id) });
    },
  });
};

// 지원서 폼 삭제
export const useDeleteForm = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => formApi.deleteForm(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: formQueryKeys.lists() });
    },
  });
};
