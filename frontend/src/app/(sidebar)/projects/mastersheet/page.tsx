'use client';

import { Users } from 'lucide-react';

import { Badge } from '@styles/components/ui/badge';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@styles/components/ui/card';
import { Separator } from '@styles/components/ui/separator';

import { useGetProjects } from '@api/tanstack/projects.queries';
import { ChallengerPart } from '@api/types/common';
import { ProjectMember } from '@api/types/project';

import { parsePart } from '@common/utils/parse-userinfo';

import DefaultSkeleton from '@common/components/DefaultSkeleton';
import UpmsHeader from '@common/components/upms/UpmsHeader';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

const TeamMatchingMasterSheet = () => {
  const user = useGetUser();
  const { data: chapterProjects, isLoading: isChapterProjectsLoading } = useGetProjects(
    user?.info.chapterId,
  );

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

  const section = {
    title: '팀 매칭 현황',
    description: '지부 내 모든 프로젝트의 팀 구성원을 한눈에 확인할 수 있습니다.',
  };

  if (isChapterProjectsLoading || !chapterProjects) {
    return <DefaultSkeleton />;
  }

  if (chapterProjects.length === 0) {
    return (
      <div className="container space-y-6 p-6">
        <UpmsHeader section={section} />
        <div className="flex h-full items-center justify-center">
          <div className="text-center">
            <h2 className="text-2xl font-bold">등록된 프로젝트가 없습니다.</h2>
            <p className="text-muted-foreground">프로젝트를 생성해주세요.</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="container space-y-6 p-6">
      <UpmsHeader section={section} />

      <div className="space-y-4">
        {chapterProjects.map((project) => {
          const memberCount = project.projectMembers.length;
          const activeMemberCount = project.projectMembers.filter((m) => m.active).length;

          return (
            <Card key={project.id} className="overflow-hidden">
              <CardHeader className="bg-muted/50 pb-4">
                <div className="flex items-start justify-between">
                  <div className="space-y-1">
                    <CardTitle className="text-2xl">{project.name}</CardTitle>
                    <CardDescription className="flex items-center gap-2 text-sm">
                      <span className="font-medium">PO:</span>
                      <span>
                        {project.productOwnerSchool} {project.productOwnerNickname}/
                        {project.productOwnerName}
                      </span>
                    </CardDescription>
                  </div>
                  <div className="flex items-center gap-2">
                    <Badge variant="secondary" className="gap-1">
                      <Users className="h-3 w-3" />
                      {activeMemberCount}/{memberCount}명
                    </Badge>
                  </div>
                </div>
              </CardHeader>

              <CardContent className="pt-6">
                {project.projectMembers.length > 0 ? (
                  <div className="space-y-4">
                    {Object.entries(filterByPart(project.projectMembers)).map(([part, members], idx) => {
                      return (
                        <div key={idx} className="space-y-2">
                          <div className="flex items-center gap-2">
                            <Badge variant="outline" className="font-semibold">
                              {parsePart(part as ChallengerPart)}
                            </Badge>
                            <span className="text-muted-foreground text-xs">
                              {members.length}명
                            </span>
                          </div>
                          <div className="grid gap-2 sm:grid-cols-2 lg:grid-cols-3">
                            {members.map((member) => {
                              return (
                                <div
                                  key={member.challengerId}
                                  className={`rounded-lg border p-3 transition-colors ${
                                    member.active
                                      ? 'bg-background hover:bg-muted/50'
                                      : 'bg-muted/30 opacity-60'
                                  }`}
                                >
                                  <div className="flex items-start justify-between gap-2">
                                    <div className="min-w-0 flex-1">
                                      <p className="truncate font-semibold">
                                        {member.challengerNickname}
                                        <span className="text-muted-foreground ml-1 text-sm font-normal">
                                          / {member.challengerName}
                                        </span>
                                      </p>
                                      <p className="text-muted-foreground mt-0.5 text-xs">
                                        {member.schoolName}
                                      </p>
                                    </div>
                                    {!member.active && (
                                      <Badge variant="secondary" className="h-5 text-xs">
                                        비활성
                                      </Badge>
                                    )}
                                  </div>
                                </div>
                              );
                            })}
                          </div>
                        </div>
                      );
                    })}
                  </div>
                ) : (
                  <div className="text-muted-foreground flex h-24 items-center justify-center rounded-lg border border-dashed">
                    <p className="text-sm">아직 매칭된 팀원이 없습니다.</p>
                  </div>
                )}
              </CardContent>
            </Card>
          );
        })}
      </div>
    </div>
  );
};

export default TeamMatchingMasterSheet;
