'use client';

import { Card } from '@styles/components/ui/card';
import { Separator } from '@styles/components/ui/separator';

import { useGetProjects } from '@api/tanstack/projects.queries';
import { ChallengerPart } from '@api/types/common';
import { ProjectMember } from '@api/types/project';

import { parsePart } from '@common/utils/parse-userinfo';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

const TeamMatchingMasterSheet = () => {
  const user = useGetUser();
  const { data: chapterProjects, isLoading: isChapterProjectsLoading } = useGetProjects(
    user?.info.chapterId,
  );

  if (isChapterProjectsLoading || !chapterProjects) {
    return <DefaultSkeleton />;
  }

  // 파트별로 프로젝트 멤버를 나열
  const filterByPart = (projectMembers: ProjectMember[]) => {
    // unique 한 파트 목록 생성
    const parts = Array.from(new Set(projectMembers.map((member) => member.part)));

    // 파트별로 멤버를 그룹화
    const groupedMembers: { [key: string]: ProjectMember[] } = {};
    parts.forEach((part) => {
      groupedMembers[part] = projectMembers.filter((member) => member.part === part);
    });

    return groupedMembers;
  };

  return (
    <div className={'flex w-full flex-col gap-y-2 p-5'}>
      {chapterProjects.map((project) => {
        return (
          <Card className={'mb-6 flex flex-col gap-2 px-4 py-2'} key={project.id}>
            {/*<div className={'flex flex-row items-end gap-x-2'}>*/}
            {/*<p className={'text-gray-600'}>{project.description}</p>*/}
            {/*</div>*/}
            <div className={'flex flex-row items-center gap-x-1'}>
              <p className={'mr-5 text-2xl font-bold'}>{project.name}</p>
              <span>{project.productOwnerSchool}</span>
              <span>
                {project.productOwnerNickname}/{project.productOwnerName}
              </span>
            </div>
            <Separator />
            {project.projectMembers.length > 0 ? (
              Object.entries(filterByPart(project.projectMembers)).map(([part, members], idx) => {
                return (
                  <div className={'flex flex-row gap-x-2'} key={idx}>
                    <span className={'text-lg font-bold'}>{parsePart(part as ChallengerPart)}</span>
                    {members.map((member) => {
                      return (
                        <p className={'flex flex-row'} key={member.challengerId}>
                          <span className={'text-gray-600'}>{member.schoolName}</span>
                          <span className={'ml-2 font-semibold'}>
                            {member.challengerNickname}/{member.challengerName}
                          </span>
                        </p>
                      );
                    })}
                  </div>
                );
              })
            ) : (
              // project.projectMembers.map((member) => {
              // return (
              //   <p className={'flex flex-row'} key={member.challengerId}>
              //     <span className={'text-gray-600'}>
              //       {member.schoolName} {parsePart(member.part)}
              //     </span>
              //     <span className={'font-semibold'}>
              //       {member.challengerNickname}/{member.challengerName}
              //     </span>
              //   </p>
              // );
              // })
              <p className={'text-gray-500 italic'}>아직 매칭된 팀원이 없습니다.</p>
            )}
          </Card>
        );
      })}
    </div>
  );
};

export default TeamMatchingMasterSheet;
