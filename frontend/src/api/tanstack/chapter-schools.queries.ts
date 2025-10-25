/**
 * 챕터-학교 매핑 React Query Hooks
 */
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import { chapterSchoolApi } from '@api/axios/chapter-schools.api';
import type {
  ChapterSchoolCreateRequest,
  ChapterSchoolListParams,
  ChapterSchoolUpdateRequest,
} from '@api/types/chapter-school';
import { chapterSchoolQueryKeys } from '@common/constants/query-key.constants';

// 챕터-학교 목록 조회
export const useGetChapterSchools = (params?: ChapterSchoolListParams) => {
  return useQuery({
    queryKey: chapterSchoolQueryKeys.list(params),
    queryFn: () => chapterSchoolApi.getChapterSchools(params),
  });
};

// 챕터-학교 단건 조회
export const useGetChapterSchool = (id: string, enabled = true) => {
  return useQuery({
    queryKey: chapterSchoolQueryKeys.detail(id),
    queryFn: () => chapterSchoolApi.getChapterSchool(id),
    enabled,
  });
};

// 챕터-학교 생성
export const useCreateChapterSchool = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ChapterSchoolCreateRequest) =>
      chapterSchoolApi.createChapterSchool(request),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: chapterSchoolQueryKeys.lists() });
      if (variables.chapterId) {
        queryClient.invalidateQueries({
          queryKey: chapterSchoolQueryKeys.list({ chapterId: variables.chapterId }),
        });
      }
    },
  });
};

// 챕터-학교 수정
export const useUpdateChapterSchool = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      id,
      request,
    }: {
      id: string;
      request: ChapterSchoolUpdateRequest;
    }) => chapterSchoolApi.updateChapterSchool(id, request),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: chapterSchoolQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: chapterSchoolQueryKeys.detail(variables.id) });
    },
  });
};

// 챕터-학교 삭제
export const useDeleteChapterSchool = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      id,
      chapterId,
    }: {
      id: string;
      chapterId?: string;
    }) => chapterSchoolApi.deleteChapterSchool(id),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: chapterSchoolQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: chapterSchoolQueryKeys.detail(variables.id) });
      if (variables.chapterId) {
        queryClient.invalidateQueries({
          queryKey: chapterSchoolQueryKeys.list({ chapterId: variables.chapterId }),
        });
      }
    },
  });
};
