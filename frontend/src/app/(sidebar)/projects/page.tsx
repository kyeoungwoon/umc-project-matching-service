'use client';

import { useGetProjects } from '@api/tanstack/projects.queries';

import ProjectInfoCard from '@common/components/ProjectInfoCard';
import UpmsHeader from '@common/components/upms/UpmsHeader';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

import ProjectCardSkeleton from '@/skeletons/components/ProjectCardSkeleton';

const ProjectListPage = () => {
  const user = useGetUser();
  const { data: projectList, isLoading: isProjectListLoading } = useGetProjects(
    user?.info.chapterId,
  );

  if (isProjectListLoading || !projectList) {
    return <ProjectCardSkeleton />;
  }

  const headerInfo = {
    title: `${user?.info.chapterName} 지부 프로젝트`,
    description: `지부 내 ${projectList.length}개의 프로젝트 목록을 확인해보세요.`,
  };

  return (
    <>
      <UpmsHeader section={headerInfo} />
      <div className="grid w-full grid-cols-1 gap-6 p-10 md:grid-cols-2 lg:grid-cols-3">
        {projectList.map((proj, idx) => (
          <ProjectInfoCard key={idx} {...proj} />
        ))}
      </div>
    </>
  );
};

export default ProjectListPage;
