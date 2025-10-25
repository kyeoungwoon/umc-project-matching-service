'use client';

import { useEffect, useRef } from 'react';

import { useParams, useRouter } from 'next/navigation';

import { toast } from 'sonner';

import { useGetForm } from '@api/tanstack/forms.queries';
import { useGetCurrentMatchingRound } from '@api/tanstack/matching-rounds.queries';
import { useGetProjectTos } from '@api/tanstack/project-to.queries';

import { ROUTES } from '@common/constants/routes.constants';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

import { ApplicationForm } from '@features/projects/components/forms/ApplicationForm';

const ApplyPage = () => {
  const params = useParams();
  const projectId = params.projectId as string;
  const formId = params.formId as string;
  const user = useGetUser();
  const router = useRouter();
  const hasRedirectedRef = useRef(false);

  // Always call all hooks unconditionally
  const { data: form, isLoading: isFormLoading } = useGetForm(formId);
  const { data: currentMatchingRound, isLoading: isCurrentMatchingRoundLoading } =
    useGetCurrentMatchingRound(user?.info?.chapterId);
  const { data: projectTos, isLoading: isProjectToLoading } = useGetProjectTos(form?.projectId);

  useEffect(() => {
    if (!isCurrentMatchingRoundLoading && !currentMatchingRound && !hasRedirectedRef.current) {
      hasRedirectedRef.current = true;
      toast.error('현재 진행 중인 매칭 차수가 없습니다.', {
        description: '지원서 작성은 매칭 기간 내에만 가능합니다.',
        position: 'top-center',
      });
      router.replace(ROUTES.PROJECTS.FORM_LIST(projectId));
    }

    if (!isProjectToLoading && projectTos && projectTos.length > 0 && !hasRedirectedRef.current) {
      const hasUserTo = projectTos.some((tos) => tos.part === user?.info.part);

      if (!hasUserTo) {
        hasRedirectedRef.current = true;
        toast.error(`${user?.info.part}에 배정된 TO가 없는 프로젝트입니다.`, {
          description: '오류라고 생각된다면, 지부 운영진에게 문의해주세요.',
          position: 'top-center',
        });
        router.replace(ROUTES.PROJECTS.FORM_LIST(projectId));
      }
    }
  }, [currentMatchingRound, isCurrentMatchingRoundLoading, projectId, projectTos, router]);

  // 로딩 중인 경우
  if (isFormLoading || isCurrentMatchingRoundLoading) {
    return <DefaultSkeleton />;
  }

  // 데이터가 없는 경우
  if (!form || !user || !currentMatchingRound) {
    return null;
    // throw new Error('폼 정보 또는 현재 매칭 차수 정보를 불러오지 못했습니다.');
  }

  return (
    <div className="flex w-full flex-col items-center justify-center p-4">
      <ApplicationForm form={form} projectId={projectId} formId={formId} />
    </div>
  );
};

export default ApplyPage;
