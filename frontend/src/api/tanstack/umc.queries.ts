/**
 * UMC 공통 React Query Hooks
 */
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import { umcApi } from '@api/axios/umc.api';
import type { ChapterCreateRequest, SchoolCreateRequest } from '@api/types/umc';

import { umcQueryKeys } from '@common/constants/query-key.constants';

// 학교 목록 조회
export const useGetSchools = () => {
  return useQuery({
    queryKey: umcQueryKeys.schools(),
    queryFn: () => umcApi.getSchools(),
  });
};

// 학교 생성
export const useCreateSchool = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: SchoolCreateRequest) => umcApi.createSchool(request),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: umcQueryKeys.schools() });
    },
  });
};

// 챕터 목록 조회
export const useGetChapters = () => {
  return useQuery({
    queryKey: umcQueryKeys.chapters(),
    queryFn: () => umcApi.getChapters(),
  });
};

// 챕터 생성
export const useCreateChapter = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ChapterCreateRequest) => umcApi.createChapter(request),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: umcQueryKeys.chapters() });
    },
  });
};

// Leo 전체 생성 (테스트용)
export const useCreateLeoAll = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => umcApi.createLeoAll(),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: umcQueryKeys.all });
    },
  });
};
