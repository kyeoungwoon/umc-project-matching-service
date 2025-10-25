/**
 * 프로젝트 React Query Hooks
 */
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import { projectApi } from '@api/axios/projects.api';
import type { ProjectCreateRequest, ProjectUpdateRequest } from '@api/types/project';

import { projectQueryKeys } from '@common/constants/query-key.constants';

// 프로젝트 목록 조회
export const useGetProjects = (chapterId?: string) => {
  return useQuery({
    queryKey: projectQueryKeys.list(chapterId),
    queryFn: () => projectApi.getProjects(chapterId),
    enabled: !!chapterId,
  });
};

// 프로젝트 단건 조회
export const useGetProject = (id: string, enabled = true) => {
  return useQuery({
    queryKey: projectQueryKeys.detail(id),
    queryFn: () => projectApi.getProject(id),
    enabled,
  });
};

// 프로젝트 생성
export const useCreateProject = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ProjectCreateRequest) => projectApi.createProject(request),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: projectQueryKeys.lists() });
    },
  });
};

// 프로젝트 수정
export const useUpdateProject = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, request }: { id: string; request: ProjectUpdateRequest }) =>
      projectApi.updateProject(id, request),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: projectQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: projectQueryKeys.detail(variables.id) });
    },
  });
};

// 프로젝트 삭제
export const useDeleteProject = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => projectApi.deleteProject(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: projectQueryKeys.lists() });
    },
  });
};
