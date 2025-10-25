import { useRouter } from 'next/navigation';

import { toast } from 'sonner';

import { ROUTES } from '@common/constants/routes.constants';

export const useRedirectToLogin = () => {
  const router = useRouter();

  return () => {
    toast.error('로그인되어 있지 않은 사용자입니다.', {
      description: '로그인 페이지로 이동합니다.',
      duration: 1000,
      position: 'top-center',
      onAutoClose: () => router.push(ROUTES.AUTH.LOGIN),
    });
  };
};
