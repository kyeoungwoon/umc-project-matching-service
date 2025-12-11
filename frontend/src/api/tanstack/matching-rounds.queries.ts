/**
 * 프로젝트 매칭 라운드 React Query Hooks
 */
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import { matchingRoundApi } from '@api/axios/matching-rounds.api';
import {
  ProjectMatchingRoundCreateRequest,
  ProjectMatchingRoundUpdateRequest,
} from '@api/types/matching-round';

import { matchingRoundQueryKeys } from '@common/constants/query-key.constants';

type MatchingRoundsFilter = {
  chapterId?: string;
  startTime?: Date;
  endTime?: Date;
};

// 매칭 라운드 목록 조회 (기간 내 겹치는 라운드 포함)
export const useGetMatchingRounds = ({ chapterId, startTime, endTime }: MatchingRoundsFilter) => {
  const params =
    chapterId && startTime && endTime
      ? {
          chapterId,
          startTime: startTime.toISOString(),
          endTime: endTime.toISOString(),
        }
      : undefined;

  return useQuery({
    queryKey: matchingRoundQueryKeys.list(params),
    queryFn: () => matchingRoundApi.getMatchingRoundsByTimeRange(params!),
    enabled: !!params,
  });
};

// 매칭 라운드 단건 조회
export const useGetMatchingRound = (id: string, enabled = true) => {
  return useQuery({
    queryKey: matchingRoundQueryKeys.detail(id),
    queryFn: () => matchingRoundApi.getMatchingRound(id),
    enabled,
  });
};

// 매칭 라운드 생성
export const useCreateMatchingRound = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ProjectMatchingRoundCreateRequest) =>
      matchingRoundApi.createMatchingRound(request),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: matchingRoundQueryKeys.lists() });
    },
  });
};

// 매칭 라운드 수정
export const useUpdateMatchingRound = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, request }: { id: string; request: ProjectMatchingRoundUpdateRequest }) =>
      matchingRoundApi.updateMatchingRound(id, request),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: matchingRoundQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: matchingRoundQueryKeys.detail(variables.id) });
    },
  });
};

// 매칭 라운드 삭제
export const useDeleteMatchingRound = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => matchingRoundApi.deleteMatchingRound(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: matchingRoundQueryKeys.lists() });
    },
  });
};

// 현재 또는 다가오는 매칭 라운드 조회
export const useGetCurrentOrUpcomingMatchingRound = (chapterId: string, enabled = true) => {
  return useQuery({
    queryKey: matchingRoundQueryKeys.currentOrClosest(chapterId),
    queryFn: () => matchingRoundApi.getCurrentOrUpcomingMatchingRound(chapterId),
    enabled,
    retry: false,
  });
};

// 현재 또는 다가오는 매칭 라운드 조회
export const useGetCurrentMatchingRound = (chapterId?: string, enabled = true) => {
  const isEnabled = enabled && !!chapterId;

  return useQuery({
    queryKey: matchingRoundQueryKeys.current(chapterId ?? 'disabled'),
    queryFn: () => matchingRoundApi.getCurrentMatchingRound(chapterId!),
    enabled: isEnabled,
    retry: false,
  });
};
