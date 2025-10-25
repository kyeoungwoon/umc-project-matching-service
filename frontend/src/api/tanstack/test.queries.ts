/**
 * 테스트 React Query Hooks
 */
import { useQuery } from '@tanstack/react-query';

import { testApi } from '@api/axios/test.api';
import { testQueryKeys } from '@common/constants/query-key.constants';

// 테스트용 토큰 발급
export const useGetTestToken = (challengerId: string, enabled = false) => {
  return useQuery({
    queryKey: testQueryKeys.token(challengerId),
    queryFn: () => testApi.getTestToken(challengerId),
    enabled,
  });
};
