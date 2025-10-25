'use client';

import Link from 'next/link';

import { InfoIcon } from 'lucide-react';

import { Button } from '@styles/components/ui/button';

import { useGetProjects } from '@api/tanstack/projects.queries';

import { ROUTES } from '@common/constants/routes.constants';

import { useRedirectToLogin } from '@common/hooks/useRedirectToLogin';

import DefaultSkeleton from '@common/components/DefaultSkeleton';
import ProjectInfoCard from '@common/components/ProjectInfoCard';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

const MyProjectsPage = () => {
  const user = useGetUser();

  const { data: projects, isLoading: areProjectsLoading } = useGetProjects(user?.info.chapterId);

  const redirectToLogin = useRedirectToLogin();
  if (!user) {
    return redirectToLogin();
  }

  if (areProjectsLoading || !projects) {
    return <DefaultSkeleton />;
  }

  // Filter projects where the current user is the planner.
  const myProjects = projects.filter((p) => p.productOwnerId === user.info.id);

  return (
    <div className="flex w-full flex-col items-center justify-center p-4">
      <div className="mt-5 w-full max-w-4xl">
        {/* 상단 목록 */}
        <div className="mb-4 flex flex-row items-center justify-between gap-x-2">
          <span className={'flex flex-row items-center gap-x-1 text-sm text-gray-700'}>
            <InfoIcon className={'w-5'} />
            지원자는 폼 별로 [지원 가능한 폼 보기 &gt; 지원자 보기] 에서 확인하실 수 있습니다.
          </span>
          <Link href={ROUTES.PROJECTS.CREATE}>
            <Button>새 프로젝트 생성</Button>
          </Link>
        </div>
        <div className="space-y-4">
          {myProjects.length > 0 ? (
            myProjects.map((proj, idx) => <ProjectInfoCard key={idx} {...proj} />)
          ) : (
            <div className="text-center text-gray-500">
              <p>아직 생성한 프로젝트가 없습니다.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default MyProjectsPage;
