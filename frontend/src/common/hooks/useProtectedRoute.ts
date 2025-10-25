import { useEffect, useState } from 'react';

import { useRouter } from 'next/navigation';

import { toast } from 'sonner';

import { useGetMe } from '@api/tanstack/challengers.queries';
import { Challenger, ChallengerLoginResponse } from '@api/types/challenger';

import { useClearUser, useGetUser } from '@features/auth/hooks/useAuthStore';

interface UseProtectedRouteOptions {
  /** 로그인이 필요한 페이지인지 (기본: true) */
  requireAuth?: boolean;
  /** 리다이렉트할 경로 */
  redirectTo: string;
  /** 토스트 메시지 */
  toast?: {
    type: 'success' | 'error';
    title: string;
    description?: string;
  };
}

interface UseProtectedRouteReturn {
  /** 체크가 완료되었는지 */
  isReady: boolean;
}

/**
 * Protected Route Hook
 *
 * @example
 * // 로그인 필요한 페이지
 * const { isReady } = useProtectedRoute({
 *   requireAuth: true,
 *   redirectTo: '/auth/login',
 *   toast: {
 *     type: 'error',
 *     title: '로그인이 필요한 서비스입니다.',
 *     description: '로그인 페이지로 이동합니다.'
 *   }
 * });
 *
 * if (!isReady) return <LoadingSpinner />;
 *
 * @example
 * // 로그인 시 접근 불가 페이지 (로그인 페이지 등)
 * const { isReady } = useProtectedRoute({
 *   requireAuth: false,
 *   redirectTo: '/home',
 *   toast: {
 *     type: 'success',
 *     title: '이미 로그인되어 있습니다.',
 *     description: '메인 화면으로 이동합니다.'
 *   }
 * });
 */
export const useProtectedRoute = ({
  requireAuth = true,
  redirectTo,
  toast: toastOptions,
}: UseProtectedRouteOptions): UseProtectedRouteReturn => {
  const user = useGetUser();
  const router = useRouter();
  const [isReady, setIsReady] = useState(false);
  const [isMounted, setIsMounted] = useState(false);

  // 클라이언트 마운트 체크 (hydration 문제 해결)
  useEffect(() => {
    setIsMounted(true);
  }, []);

  useEffect(() => {
    // 클라이언트에서만 실행
    if (isMounted) {
      const shouldRedirect = requireAuth ? !user : !!user;

      if (shouldRedirect) {
        // 토스트 메시지 표시
        if (toastOptions) {
          const toastFn = toastOptions.type === 'error' ? toast.error : toast.success;
          toastFn(toastOptions.title, {
            description: toastOptions.description,
            position: 'top-center',
          });
        }
        // 리다이렉트
        router.push(redirectTo);
      } else {
        setIsReady(true);
      }
    }
  }, [user, router, isMounted, requireAuth, redirectTo, toastOptions]);

  return {
    isReady: isMounted && isReady,
  };
};
