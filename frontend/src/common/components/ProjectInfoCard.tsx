'use client';

import { useState } from 'react';

import Image from 'next/image';
import { useRouter } from 'next/navigation';

import { useQueryClient } from '@tanstack/react-query';

import { toast } from 'sonner';

import { Button } from '@styles/components/ui/button';
import { Input } from '@styles/components/ui/input';
import { Label } from '@styles/components/ui/label';
import { Textarea } from '@styles/components/ui/textarea';

import { useGetChallenger } from '@api/tanstack/challengers.queries';
import { useUpdateProject } from '@api/tanstack/projects.queries';
import { Project, type ProjectUpdateRequest } from '@api/types/project';

import { ROUTES } from '@common/constants/routes.constants';

import { calculateCurrentAndMaxProjectTo } from '@common/utils/project-response-card';

import {
  useGetChallengerChapterRoles,
  useIsPlanChallenger,
} from '@common/hooks/check-challenger-permissions';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

export interface ProjectPartAndTo {
  part: string;
  currentTo: number;
  maxTo: number;
}

export enum ProjectCardMode {
  EDIT = 'edit',
  VIEW = 'view',
}

const ProjectInfoCard = ({
  id,
  name,
  description,
  productOwnerName,
  productOwnerId,
  chapterName,
  chapterId,
  logoImageUrl,
  bannerImageUrl,
  notionLink,
  projectTos,
  projectMembers,
}: Project) => {
  const { data: poInfo, isLoading: isPoInfoLoading } = useGetChallenger({
    challengerId: productOwnerId,
  });
  const user = useGetUser();
  const { roles: chapterAdminRole, isLoading: isRoleLoading } = useGetChallengerChapterRoles();

  const isProductOwner = user?.info.id === productOwnerId;
  const isAdmin = chapterAdminRole.length > 0;

  const [mode, setMode] = useState<ProjectCardMode>(ProjectCardMode.VIEW);
  const [editedProject, setEditedProject] = useState<ProjectUpdateRequest>({
    name,
    description,
    productOwnerId,
    logoImageUrl,
    bannerImageUrl,
    notionLink,
  });

  const { mutate: updateProject } = useUpdateProject();
  const router = useRouter();

  const handleApplyClick = () => {
    // Navigate to the application page
    router.push(ROUTES.PROJECTS.FORM_LIST(id));
  };

  const handleViewMoreClick = () => {
    // props.link 로 새로운 창을 띄워준다
    if (notionLink) {
      window.open(notionLink, '_blank');
    }
  };

  const handleEditModeClick = () => {
    setMode(ProjectCardMode.EDIT);
  };

  const handleSaveClick = () => {
    setMode(ProjectCardMode.VIEW);
    updateProject(
      { id, request: editedProject },
      {
        onSuccess: () => {
          toast.success('프로젝트가 성공적으로 수정되었습니다.');
        },
        onError: (error) => {
          toast.error(`프로젝트 수정에 실패했습니다.`, {
            description: error.message,
          });
          console.error(error);
        },
      },
    );
  };

  if (!poInfo || isPoInfoLoading) {
    return <DefaultSkeleton />;
  }

  // =========== 프로젝트 수정 모드 ===========

  if (mode === ProjectCardMode.EDIT) {
    return (
      <div className="rounded-22pxr flex w-full max-w-100 min-w-80 flex-col justify-between border border-gray-200 bg-white p-5">
        <div className={'flex flex-col gap-y-3'}>
          <span>프로젝트 제목</span>
          <Input
            value={editedProject.name}
            onChange={(e) => setEditedProject((prev) => ({ ...prev, title: e.target.value }))}
            className="text-xl font-bold"
          />
          <span>프로젝트 설명</span>
          <Textarea
            value={editedProject.description}
            onChange={(e) => setEditedProject((prev) => ({ ...prev, description: e.target.value }))}
            className="text-muted-foreground mb-2 text-lg"
          />
          <span>프로젝트 기획안</span>
          <Input
            value={editedProject.notionLink}
            onChange={(e) => setEditedProject((prev) => ({ ...prev, link: e.target.value }))}
            className="text-muted-foreground mb-2 text-lg"
          />
        </div>
        <Button className={'h-10 w-full text-lg'} variant={'default'} onClick={handleSaveClick}>
          수정 완료
        </Button>
      </div>
    );
  }

  // =========== 프로젝트 뷰 모드 ===========

  return (
    <div className="rounded-22pxr flex w-full max-w-100 min-w-80 flex-col justify-between border border-gray-200 bg-white">
      {/*상단 이미지 영역*/}
      <Image
        src={bannerImageUrl || 'https://placehold.co/600x400'}
        alt={'프로젝트 배너 이미지'}
        width={800}
        height={200}
        className={'rounded-t-22pxr h-48 w-full bg-gray-200 object-cover'}
      />

      <div className={'flex h-full flex-col items-start gap-y-3 p-6'}>
        <div className={'flex w-full flex-row items-end justify-between'}>
          <p className="text-3xl font-semibold">{name}</p>
          <div className={'text-15pxr flex flex-row gap-x-2 text-gray-700'}>
            {poInfo.schoolName} {poInfo.nickname}/{poInfo.name}
          </div>
        </div>
        <p className="text-muted-foreground text-base">{description}</p>

        {/*TODO: 안내멘트, 삭제 필요*/}
        {/*<div className="text-muted-foreground mb-2 text-sm">*/}
        {/*  <span className={'text-black'}>파트별 TO | </span>*/}
        {/*  형식 : (현재 지원자 수) TO*/}
        {/*</div>*/}

        <div className={'flex-1'} />

        <div className="mt-2 mb-5 flex w-full flex-col items-center gap-y-1">
          {calculateCurrentAndMaxProjectTo(projectTos, projectMembers).map((partTo, index) => {
            const { part, currentTo, maxTo } = partTo;
            return (
              <div key={index} className="flex w-full flex-row items-center justify-between">
                <div className={'flex flex-row items-center text-sm text-gray-800'}>
                  <span className="min-w-25">{part}</span>
                  <span className={'tracking-wide'}>
                    {currentTo}/{maxTo}
                  </span>
                </div>
                <Label
                  className={`font-semibold tracking-wide ${
                    currentTo >= maxTo ? 'text-red-500' : 'text-green-600'
                  }`}
                >
                  {currentTo >= maxTo ? '모집 완료' : '모집 중'}
                </Label>
              </div>
            );
          })}
        </div>

        {(isProductOwner || isAdmin) && (
          <Button
            variant={'outline'}
            onClick={handleEditModeClick}
            className={'h-10 w-full px-4 text-base'}
          >
            프로젝트 정보 수정
          </Button>
        )}
        <div className="flex h-12 w-full flex-row gap-x-2">
          <Button
            onClick={handleViewMoreClick}
            variant={'default'}
            className={'h-full flex-1 text-base'}
          >
            기획안 자세히 보기
          </Button>
          <Button
            onClick={handleApplyClick}
            variant={'outline'}
            className={'h-full flex-1 text-base'}
          >
            {isProductOwner ? '지원자 보기' : '지원하기'}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default ProjectInfoCard;
