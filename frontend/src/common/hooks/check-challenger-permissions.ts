import { useEffect } from 'react';

import { useRouter } from 'next/navigation';

import { toast } from 'sonner';

import { useGetChapterAdminRole, useGetChapterAdmins } from '@api/tanstack/chapter-admins.queries';
import { ChallengerPartEnum, ChapterAdminRoleType } from '@api/types/common';

import { ROUTES } from '@common/constants/routes.constants';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

export const useIsPlanChallenger = (planId?: string) => {
  const user = useGetUser();

  if (planId) {
    return user?.info?.part === ChallengerPartEnum.PLAN && user?.info?.id === planId;
  }

  return user?.info?.part === ChallengerPartEnum.PLAN;
};

export const useGetChallengerChapterRoles = () => {
  const user = useGetUser();

  const { data, isLoading } = useGetChapterAdmins({
    challengerId: user?.info.id as string,
    // chapterId: user?.info.chapterId as string,
  });

  const roles = data?.map((admin) => admin.role) || [];

  return {
    roles, // null이면 권한 없음
    isLoading: isLoading,
  };
};

export const useHasChallengerChapterRole = (roleToCheck: ChapterAdminRoleType) => {
  const { roles, isLoading } = useGetChallengerChapterRoles();

  if (isLoading) return false;
  return roles ? roles.includes(roleToCheck) : false;
};

/**
 * Plan 파트가 아닌 경우 홈으로 리다이렉트하는 Hook
 * @example
 * const MyPlanOnlyPage = () => {
 *   useRedirectToHomeIfNotPlan();
 *   return <div>Plan Only Content</div>;
 * };
 */
export const useRedirectToHomeIfNotPlan = () => {
  const router = useRouter();
  const isPlanChallenger = useIsPlanChallenger();
  const { roles: chapterAdminRole, isLoading: isRoleLoading } = useGetChallengerChapterRoles();

  useEffect(() => {
    if (isRoleLoading) return;
    // 어떤 종류의 운영진이라도 있으면 통과
    if (chapterAdminRole && chapterAdminRole.length > 0) return;
    if (!isPlanChallenger) {
      toast.error('접근 권한이 없습니다.', {
        description: 'Plan 파트 챌린저만 접근할 수 있습니다.',
      });
      router.push(ROUTES.HOME);
    }
  }, [isPlanChallenger, router]);
};

/**
 * 어떤 종류의 역할도 맡지 않고 있으면 홈으로 리다이렉트 시킴
 */
export const useRedirectToHomeIfNotAnyAdmin = () => {
  const router = useRouter();
  const { roles: chapterAdminRole, isLoading: isRoleLoading } = useGetChallengerChapterRoles();

  useEffect(() => {
    if (isRoleLoading) return;
    if (!chapterAdminRole || chapterAdminRole.length === 0) {
      toast.error('접근 권한이 없습니다.', {
        description: '관리자만 접근할 수 있습니다.',
      });
      router.push(ROUTES.HOME);
    }
  }, [chapterAdminRole, isRoleLoading, router]);
};
