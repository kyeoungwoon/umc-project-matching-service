/**
 * 챕터 관리자 React Query Hooks
 */
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import { chapterAdminApi } from '@api/axios/chapter-admins.api';
import type {
  ChapterAdminCreateRequest,
  ChapterAdminListParams,
  ChapterAdminRoleParams,
  ChapterAdminUpdateRequest,
} from '@api/types/chapter-admin';
import { chapterAdminQueryKeys } from '@common/constants/query-key.constants';

// 챕터 관리자 목록 조회
export const useGetChapterAdmins = (params?: ChapterAdminListParams, enabled = true) => {
  return useQuery({
    queryKey: chapterAdminQueryKeys.list(params),
    queryFn: () => chapterAdminApi.getChapterAdmins(params),
    enabled,
  });
};

// 챌린저의 챕터 역할 조회
export const useGetChapterAdminRole = (params: ChapterAdminRoleParams, enabled = true) => {
  return useQuery({
    queryKey: chapterAdminQueryKeys.role(params),
    queryFn: () => chapterAdminApi.getChapterAdminRole(params),
    enabled,
  });
};

// 챕터 관리자 단건 조회
export const useGetChapterAdmin = (id: string, enabled = true) => {
  return useQuery({
    queryKey: chapterAdminQueryKeys.detail(id),
    queryFn: () => chapterAdminApi.getChapterAdmin(id),
    enabled,
  });
};

// 챕터 관리자 생성
export const useCreateChapterAdmin = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ChapterAdminCreateRequest) => chapterAdminApi.createChapterAdmin(request),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: chapterAdminQueryKeys.lists() });
    },
  });
};

// 챕터 관리자 수정
export const useUpdateChapterAdmin = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, request }: { id: string; request: ChapterAdminUpdateRequest }) =>
      chapterAdminApi.updateChapterAdmin(id, request),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: chapterAdminQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: chapterAdminQueryKeys.detail(variables.id) });
    },
  });
};

// 챕터 관리자 삭제
export const useDeleteChapterAdmin = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => chapterAdminApi.deleteChapterAdmin(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: chapterAdminQueryKeys.lists() });
    },
  });
};
