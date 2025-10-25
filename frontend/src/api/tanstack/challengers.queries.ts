/**
 * 챌린저(회원) React Query Hooks
 */
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import { challengerApi } from '@api/axios/challengers.api';
import {
  ChallengerCreateRequest,
  ChallengerLoginRequest,
  ChangePasswordRequest,
} from '@api/types/challenger';

import { challengerQueryKeys } from '@common/constants/query-key.constants';

type ChallengerQueryParams = {
  challengerId?: string;
  umsbId?: string;
};

// 내 정보 조회
export const useGetMe = (enabled = true) => {
  return useQuery({
    queryKey: challengerQueryKeys.me(),
    queryFn: () => challengerApi.getMe(),
    staleTime: 24 * 60 * 60 * 1000, // 24시간 동안 유효
    enabled,
  });
};

// 챌린저 정보 조회 (ID 또는 UMSB ID)
export const useGetChallenger = (params: ChallengerQueryParams, enabled = true) => {
  return useQuery({
    queryKey: challengerQueryKeys.info(params),
    queryFn: () => challengerApi.getChallenger(params),
    enabled: enabled && Boolean(params?.challengerId || params?.umsbId),
  });
};

// 챌린저의 챕터 조회
export const useGetChallengerChapter = (challengerId: string, enabled = true) => {
  return useQuery({
    queryKey: challengerQueryKeys.chapter(challengerId),
    queryFn: () => challengerApi.getChallengerChapter(challengerId),
    enabled: enabled && Boolean(challengerId),
  });
};

// 회원가입
export const useRegister = () => {
  return useMutation({
    mutationFn: (request: ChallengerCreateRequest) => challengerApi.register(request),
  });
};

// 대량 회원가입
export const useRegisterBulk = () => {
  return useMutation({
    mutationFn: (requests: ChallengerCreateRequest[]) => challengerApi.registerBulk(requests),
  });
};

// 로그인
export const useLogin = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ChallengerLoginRequest) => challengerApi.login(request),
    onSuccess: (data) => {
      // 토큰 저장 (BaseApi에서 이미 언래핑됨)
      // if (data.accessToken) {
      //   localStorage.setItem('accessToken', data.accessToken);
      // }
      // 사용자 정보 캐시에 저장 (이미 언래핑된 Challenger 객체 사용)
      queryClient.setQueryData(challengerQueryKeys.me(), data.challengerInfo);
    },
  });
};

// 로그아웃
export const useLogout = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async () => {
      // 로그아웃 API가 있다면 호출
      return Promise.resolve();
    },
    onSuccess: () => {
      // 캐시 초기화
      queryClient.clear();
    },
  });
};

export const useChangePassword = () => {
  return useMutation({
    mutationFn: (request: ChangePasswordRequest) => challengerApi.changePassword(request),
  });
};
