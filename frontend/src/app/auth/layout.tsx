'use client';

import { ReactNode } from 'react';

import { ROUTES } from '@common/constants/routes.constants';

import { useProtectedRoute } from '@common/hooks/useProtectedRoute';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

const AuthLayout = ({ children }: { children: ReactNode }) => {
  const { isReady } = useProtectedRoute({
    requireAuth: false, // 로그인하지 않은 사용자만 접근 가능
    redirectTo: ROUTES.HOME,
    toast: {
      type: 'success',
      title: '로그인된 사용자입니다.',
      description: '메인 화면으로 이동합니다.',
    },
  });

  if (!isReady) {
    return <DefaultSkeleton />;
  }

  return <div className={'flex flex-1 flex-col items-center justify-center'}>{children}</div>;
};

export default AuthLayout;
