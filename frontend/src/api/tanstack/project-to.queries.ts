/**
 * 프로젝트 TO React Query Hooks
 */
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import { projectToApi } from '@api/axios/project-to.api';
import type {
  ProjectToBulkCreateRequest,
  ProjectToCreateRequest,
  ProjectToUpdateRequest,
} from '@api/types/project-to';

import { projectQueryKeys, projectToQueryKeys } from '@common/constants/query-key.constants';

// 프로젝트 TO 목록 조회
export const useGetProjectTos = (projectId?: string) => {
  return useQuery({
    queryKey: projectToQueryKeys.list(projectId),
    queryFn: () => projectToApi.getProjectTos(projectId),
    enabled: !!projectId,
  });
};

// 프로젝트 TO 단건 생성
export const useCreateProjectTo = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ProjectToCreateRequest) => projectToApi.createProjectTo(request),
    onSuccess: (data, variables) => {
      queryClient.invalidateQueries({ queryKey: projectToQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: projectQueryKeys.list(variables.projectId) });
      queryClient.invalidateQueries({ queryKey: projectQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: projectQueryKeys.detail(variables.projectId) });
    },
  });
};

// 프로젝트 TO 일괄 생성
export const useCreateProjectTosBulk = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ProjectToBulkCreateRequest) => projectToApi.bulkCreateProjectTos(request),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: projectToQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: projectQueryKeys.lists() });
      if (variables.projectId) {
        queryClient.invalidateQueries({ queryKey: projectToQueryKeys.list(variables.projectId) });
        queryClient.invalidateQueries({ queryKey: projectQueryKeys.detail(variables.projectId) });
      }
    },
  });
};

// 프로젝트 TO 수정
export const useUpdateProjectTo = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, request }: { id: string; request: ProjectToUpdateRequest }) =>
      projectToApi.updateProjectTo(id, request),
    onSuccess: (data, variables) => {
      queryClient.invalidateQueries({ queryKey: projectToQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: projectToQueryKeys.detail(variables.id) });
      queryClient.invalidateQueries({ queryKey: projectQueryKeys.lists() });
      if (data.projectId) {
        queryClient.invalidateQueries({ queryKey: projectQueryKeys.detail(data.projectId) });
      }
    },
  });
};

// 프로젝트 TO 삭제
export const useDeleteProjectTo = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, projectId }: { id: string; projectId?: string }) =>
      projectToApi.deleteProjectTo(id),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: projectToQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: projectToQueryKeys.detail(variables.id) });
      queryClient.invalidateQueries({ queryKey: projectQueryKeys.lists() });
      if (variables.projectId) {
        queryClient.invalidateQueries({ queryKey: projectQueryKeys.detail(variables.projectId) });
      }
    },
  });
};
