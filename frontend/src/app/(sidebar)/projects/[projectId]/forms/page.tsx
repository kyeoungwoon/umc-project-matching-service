'use client';

import { useState } from 'react';

import Image from 'next/image';
import { useParams, useRouter } from 'next/navigation';

import { CircleAlertIcon, InboxIcon, PencilIcon, UsersIcon } from 'lucide-react';
import { toast } from 'sonner';

import { Badge } from '@styles/components/ui/badge';
import { Button } from '@styles/components/ui/button';
import { Card, CardContent } from '@styles/components/ui/card';
import { HoverCard, HoverCardContent, HoverCardTrigger } from '@styles/components/ui/hover-card';
import { Label } from '@styles/components/ui/label';
import { Separator } from '@styles/components/ui/separator';

import { useGetForms } from '@api/tanstack/forms.queries';
import { useGetProject, useUpdateProject } from '@api/tanstack/projects.queries';
import { ChapterAdminRoleEnum } from '@api/types/common';

import { ROUTES } from '@common/constants/routes.constants';

import { parsePart } from '@common/utils/parse-userinfo';
import { calculateCurrentAndMaxProjectTo } from '@common/utils/project-response-card';

import {
  useGetChallengerChapterRoles,
  useHasChallengerChapterRole,
  useIsPlanChallenger,
} from '@common/hooks/check-challenger-permissions';

import DefaultSkeleton from '@common/components/DefaultSkeleton';
import { FileUploader } from '@common/components/FileUploader';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

import { FormInfoCard } from '@features/projects/components/FormInfoCard';

const ProjectFormsPage = () => {
  const params = useParams();
  const projectId = params.projectId as string;

  const { data: project, isLoading: isProjectLoading } = useGetProject(projectId);
  const { data: projectForms, isLoading: isProjectFormsLoading } = useGetForms(projectId);
  const { mutate: updateProject } = useUpdateProject();

  const user = useGetUser();

  // 이미지 편집 상태
  const [isEditingBanner, setIsEditingBanner] = useState(false);
  const [isEditingLogo, setIsEditingLogo] = useState(false);

  // 권한 체크
  const isSchoolLead = useHasChallengerChapterRole(ChapterAdminRoleEnum.SCHOOL_LEAD);
  const router = useRouter();
  const handleCreateFormClick = () => {
    router.push(ROUTES.PROJECTS.CREATE_FORM(projectId));
  };

  const handleImageUpdate = (type: 'logo' | 'banner', url: string) => {
    if (!project) return;

    const updateData = {
      name: project.name,
      description: project.description,
      productOwnerId: project.productOwnerId,
      notionLink: project.notionLink,
      logoImageUrl: type === 'logo' ? url : project.logoImageUrl,
      bannerImageUrl: type === 'banner' ? url : project.bannerImageUrl,
    };

    updateProject(
      { id: projectId, request: updateData },
      {
        onSuccess: () => {
          toast.success(`${type === 'logo' ? '로고' : '배너'} 이미지가 업데이트되었습니다.`);
          if (type === 'logo') setIsEditingLogo(false);
          if (type === 'banner') setIsEditingBanner(false);
        },
        onError: () => {
          toast.error(`${type === 'logo' ? '로고' : '배너'} 이미지 업데이트에 실패했습니다.`);
        },
      },
    );
  };

  if (isProjectLoading || !project || isProjectFormsLoading || !projectForms) {
    return <DefaultSkeleton />;
  }

  const isProductOwner = project?.productOwnerId === user?.info.id;
  const hasPerms = isProductOwner || isSchoolLead;

  return (
    <div className="container mx-auto max-w-6xl space-y-8 p-6">
      {/* Header Section */}
      <div className="group relative">
        <Image
          src={project.bannerImageUrl || 'https://placehold.co/600x400'}
          alt={`${project.name} 배너 이미지`}
          width={1800}
          height={300}
          className={'h-300pxr w-full bg-gray-200 object-cover object-center'}
        />
        {isProductOwner && !isEditingBanner && (
          <button
            onClick={() => setIsEditingBanner(true)}
            className="absolute inset-0 flex items-center justify-center bg-black/50 opacity-0 transition-opacity group-hover:opacity-100"
          >
            <div className="flex flex-col items-center gap-2 text-white">
              <PencilIcon className="h-8 w-8" />
              <span className="text-lg font-medium">배너 이미지 수정</span>
            </div>
          </button>
        )}
        {isEditingBanner && (
          <div className="absolute inset-0 flex items-center justify-center bg-black/70 p-8">
            <div className="flex flex-col items-center gap-4 rounded-lg bg-white p-6">
              <h3 className="text-xl font-bold">배너 이미지 변경</h3>
              <FileUploader
                accept="image/*"
                maxSize={10 * 1024 * 1024}
                buttonText="새 배너 업로드"
                onUploadComplete={(url) => handleImageUpdate('banner', url)}
              />
              <Button variant="outline" onClick={() => setIsEditingBanner(false)}>
                취소
              </Button>
            </div>
          </div>
        )}
      </div>

      <div className={'flex flex-row items-center gap-6'}>
        <div className="group relative">
          <Image
            src={project.logoImageUrl || 'https://placehold.co/360x360'}
            alt={`${project.name} 로고 이미지`}
            width={360}
            height={360}
            className={'h-90pxr w-90pxr rounded-full bg-gray-200 object-cover object-center'}
          />
          {isProductOwner && !isEditingLogo && (
            <button
              onClick={() => setIsEditingLogo(true)}
              className="absolute inset-0 flex items-center justify-center rounded-full bg-black/50 opacity-0 transition-opacity group-hover:opacity-100"
            >
              <div className="flex flex-col items-center gap-1 text-white">
                <PencilIcon className="h-6 w-6" />
                <span className="text-sm font-medium">수정</span>
              </div>
            </button>
          )}
          {isEditingLogo && (
            <div className="absolute inset-0 z-50 flex items-center justify-center">
              <div className="flex flex-col items-center gap-4 rounded-lg bg-white p-6 shadow-xl">
                <h3 className="text-lg font-bold">로고 이미지 변경</h3>
                <FileUploader
                  accept="image/*"
                  maxSize={5 * 1024 * 1024}
                  buttonText="새 로고 업로드"
                  onUploadComplete={(url) => handleImageUpdate('logo', url)}
                />
                <Button variant="outline" onClick={() => setIsEditingLogo(false)}>
                  취소
                </Button>
              </div>
            </div>
          )}
        </div>
        {/*프로젝트명*/}
        <div className={'text-30pxr flex flex-col items-start justify-start gap-2'}>
          <span className="max-w-full flex-grow-1 font-bold break-words">{project.name}</span>
          <span className="text-muted-foreground mt-auto mb-2 min-w-0 flex-grow text-2xl break-words whitespace-pre-line">
            {project.description}
          </span>
        </div>
      </div>

      {/*TODO: 안내멘트, 삭제 필요*/}
      {/*TODO: 중복된 내용 컴포넌트화하기*/}
      <div className="mb-5 flex flex-row items-center gap-x-2 text-base tracking-tight text-black">
        {/*<InfoIcon className={'h-5 w-5'} />*/}
        {/*<span className={'text-gray-600'}>파트별 TO | </span>*/}
        형식 : (현재 지원자 수) TO / 사람 아이콘에 마우스 올리면 팀원 목록이 표시됩니다.
      </div>

      <div className="mt-2 mb-5 flex w-full flex-col items-center gap-y-1">
        {calculateCurrentAndMaxProjectTo(project.projectTos, project.projectMembers).map(
          (partTo, index) => {
            const { part, currentTo, maxTo } = partTo;
            return (
              <div key={index} className="flex w-full flex-row items-center justify-between">
                <div
                  className={'mr-5 flex flex-shrink flex-row items-center text-lg text-gray-800'}
                >
                  <span className="min-w-35">{part}</span>
                  <span className={'mr-5 tracking-widest'}>
                    ({currentTo}){maxTo}
                  </span>
                  <HoverCard openDelay={0.1} closeDelay={1}>
                    <HoverCardTrigger asChild>
                      <UsersIcon className={'h-5'} />
                    </HoverCardTrigger>
                    <HoverCardContent className={'flex max-w-70 flex-row flex-wrap gap-2'}>
                      {(() => {
                        const filteredMembers = project.projectMembers.filter(
                          (mem) => mem.part === part,
                        );

                        return filteredMembers.length > 0 ? (
                          filteredMembers.map((member) => (
                            <Badge
                              variant={'outline'}
                              className={'flex-shrink-0 text-gray-700'}
                              key={member.id}
                            >
                              {member.schoolName} {member.challengerNickname}/
                              {member.challengerName}
                            </Badge>
                          ))
                        ) : (
                          // 4. length가 0일 때 "멤버 없음" 텍스트를 표시합니다.
                          <div
                            className={
                              'flex w-full flex-row items-center justify-center font-semibold'
                            }
                          >
                            <CircleAlertIcon className={'mr-2 h-5'} />
                            {parsePart(part)} 파트 멤버가 없습니다.
                          </div>
                        );
                      })()}
                    </HoverCardContent>
                  </HoverCard>
                </div>
                <Label
                  className={`flex-shrink-0 text-lg font-semibold tracking-wide ${currentTo >= maxTo ? 'text-red-500' : 'text-green-600'}`}
                >
                  {currentTo >= maxTo ? '모집 완료' : '모집 중'}
                </Label>
              </div>
            );
          },
        )}
      </div>

      <Separator />

      {/* Forms Section */}
      <div className="space-y-4">
        <div className="flex items-center gap-2">
          <h2 className="text-2xl font-semibold">지원용 폼 목록</h2>
        </div>

        {projectForms.length > 0 ? (
          <div className="grid gap-4">
            {projectForms.map((form) => (
              <FormInfoCard key={form.id} form={form} isProductOwner={isProductOwner} />
            ))}
          </div>
        ) : (
          <Card className="border-dashed">
            <CardContent className="flex flex-col items-center justify-center gap-y-1 py-16">
              <InboxIcon className="text-muted-foreground h-12 w-12" />
              <p className="text-muted-foreground text-lg font-medium">
                이 프로젝트에 제출 가능한 폼이 존재하지 않습니다.
              </p>
              <p className="text-muted-foreground text-sm">
                {hasPerms
                  ? '폼을 생성해서 지원자를 모집해 보세요.'
                  : 'Plan 챌린저에게 폼 생성을 요구해주세요.'}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
      {hasPerms && (
        <Button
          className={'mt-4 h-14 w-full py-4 text-2xl'}
          variant={'secondary'}
          onClick={handleCreateFormClick}
        >
          {isSchoolLead ? '[회장 직권] 지원용 폼 생성하기' : '지원용 폼 생성하기'}
        </Button>
      )}
    </div>
  );
};

export default ProjectFormsPage;
