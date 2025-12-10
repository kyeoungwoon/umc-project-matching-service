/**
 * 관리자 React Query Hooks
 */
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import { adminApi } from '@api/axios/admin.api';
import type { ProjectMemberCreateRequest } from '@api/types/admin';
import { adminQueryKeys, projectQueryKeys } from '@common/constants/query-key.constants';

// 관리자 권한으로 프로젝트 멤버 추가
export const useAddProjectMember = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ProjectMemberCreateRequest) => adminApi.addProjectMember(request),
    onSuccess: (_, variables) => {
      // 해당 프로젝트 상세 정보 무효화
      queryClient.invalidateQueries({
        queryKey: projectQueryKeys.detail(String(variables.projectId)),
      });
    },
  });
};

// 관리자 권한으로 프로젝트 멤버 삭제
export const useDeleteProjectMember = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (projectMemberId: number) => adminApi.deleteProjectMember(projectMemberId),
    onSuccess: () => {
      // 모든 프로젝트 목록 무효화 (어느 프로젝트의 멤버인지 알 수 없으므로)
      queryClient.invalidateQueries({ queryKey: projectQueryKeys.lists() });
    },
  });
};

// 이름으로 챌린저 검색
export const useSearchChallengerByName = (name: string, enabled = true) => {
  return useQuery({
    queryKey: adminQueryKeys.challengerSearch(name),
    queryFn: () => adminApi.searchChallengerByName(name),
    enabled: enabled && name.length > 0,
  });
};
