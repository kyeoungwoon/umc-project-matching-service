'use client';

import { useParams } from 'next/navigation';

import { useGetProject } from '@api/tanstack/projects.queries';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

import ProjectEditForm from '@features/admin/components/project/ProjectEditForm';

export default function ProjectEditPage() {
  const params = useParams();
  const projectId = params.projectId as string;

  const { data: project, isLoading, error } = useGetProject(projectId);

  if (isLoading) {
    return (
      <div className="container mx-auto py-8">
        <DefaultSkeleton />
      </div>
    );
  }

  if (error || !project) {
    return (
      <div className="container mx-auto py-8">
        <div className="rounded-lg bg-red-50 p-6 text-center">
          <h2 className="mb-2 text-xl font-semibold text-red-800">프로젝트를 불러올 수 없습니다</h2>
          <p className="text-red-600">프로젝트 정보를 가져오는 중 오류가 발생했습니다.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex flex-col p-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold">프로젝트 수정</h1>
        <p className="text-muted-foreground mt-2">프로젝트의 상세 정보를 수정할 수 있습니다.</p>
      </div>

      <ProjectEditForm project={project} />
    </div>
  );
}
