'use client';

import { ReactNode, useEffect, useState } from 'react';

import { router } from 'next/client';
import { useRouter } from 'next/navigation';

import { toast } from 'sonner';

import { SidebarProvider, SidebarTrigger } from '@styles/components/ui/sidebar';

import { ROUTES } from '@common/constants/routes.constants';

import { useProtectedRoute } from '@common/hooks/useProtectedRoute';

import DefaultSkeleton from '@common/components/DefaultSkeleton';
import UpmsSideBar from '@common/components/upms/UpmsSidebar';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

const UpmsHomeLayout = ({ children }: { children?: ReactNode }) => {
  const { isReady } = useProtectedRoute({
    requireAuth: true,
    redirectTo: ROUTES.AUTH.LOGIN,
    toast: {
      type: 'error',
      title: '로그인이 필요한 서비스입니다.',
      description: '로그인 페이지로 이동합니다.',
    },
  });

  return (
    <>
      <SidebarProvider>
        <UpmsSideBar />
        <main className={'w-full'}>
          {/* 모바일 햄버거 메뉴 버튼 */}
          <div className="bg-background sticky top-0 z-10 border-b p-4 md:hidden">
            <SidebarTrigger />
          </div>
          {children}
        </main>
      </SidebarProvider>
    </>
  );
};

export default UpmsHomeLayout;
