/**
 * 지원서 질문 React Query Hooks
 */
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import { questionApi } from '@api/axios/questions.api';
import type {
  ApplicationFormQuestionBulkCreateRequest,
  ApplicationFormQuestionCreateRequest,
  ApplicationFormQuestionUpdateRequest,
} from '@api/types/question';
import { formQueryKeys, questionQueryKeys } from '@common/constants/query-key.constants';

// 질문 목록 조회
export const useGetQuestions = (formId?: string) => {
  return useQuery({
    queryKey: questionQueryKeys.list(formId),
    queryFn: () => questionApi.getQuestions(formId),
  });
};

// 질문 단건 조회
export const useGetQuestion = (id: string, enabled = true) => {
  return useQuery({
    queryKey: questionQueryKeys.detail(id),
    queryFn: () => questionApi.getQuestion(id),
    enabled,
  });
};

// 질문 생성
export const useCreateQuestion = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ApplicationFormQuestionCreateRequest) =>
      questionApi.createQuestion(request),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: questionQueryKeys.lists() });
      // 해당 폼의 캐시도 무효화 (BaseApi에서 이미 언래핑됨)
      if (data.formId) {
        queryClient.invalidateQueries({
          queryKey: formQueryKeys.detail(data.formId),
        });
      }
    },
  });
};

// 질문 대량 생성
export const useCreateQuestionsBulk = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: ApplicationFormQuestionBulkCreateRequest) =>
      questionApi.createQuestionsBulk(request),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: questionQueryKeys.lists() });
      // 해당 폼의 캐시도 무효화
      if (variables.formId) {
        queryClient.invalidateQueries({
          queryKey: formQueryKeys.detail(variables.formId),
        });
      }
    },
  });
};

// 질문 수정
export const useUpdateQuestion = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, request }: { id: string; request: ApplicationFormQuestionUpdateRequest }) =>
      questionApi.updateQuestion(id, request),
    onSuccess: (data, variables) => {
      queryClient.invalidateQueries({ queryKey: questionQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: questionQueryKeys.detail(variables.id) });
      // 해당 폼의 캐시도 무효화 (BaseApi에서 이미 언래핑됨)
      if (data.formId) {
        queryClient.invalidateQueries({
          queryKey: formQueryKeys.detail(data.formId),
        });
      }
    },
  });
};

// 질문 삭제
export const useDeleteQuestion = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => questionApi.deleteQuestion(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: questionQueryKeys.lists() });
    },
  });
};
