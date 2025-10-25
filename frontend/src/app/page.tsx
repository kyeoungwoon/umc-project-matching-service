'use client';

import { useEffect } from 'react';

import { useRouter } from 'next/navigation';

import { ROUTES } from '@common/constants/routes.constants';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

const RootPage = () => {
  const user = useGetUser();
  const router = useRouter();

  useEffect(() => {
    if (!user) {
      router.push(ROUTES.AUTH.LOGIN);
    } else {
      router.push(ROUTES.HOME);
    }
  }, [user, router]);

  return null; // 또는 로딩 스피너를 반환할 수 있습니다.
};

export default RootPage;
