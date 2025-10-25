'use client';

// Application Card Component
import { useState } from 'react';

import { useQueryClient } from '@tanstack/react-query';

import { CheckIcon, ChevronDownIcon, ChevronUpIcon, XIcon } from 'lucide-react';
import { toast } from 'sonner';

import { Button } from '@styles/components/ui/button';
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from '@styles/components/ui/collapsible';
import { ScrollArea } from '@styles/components/ui/scroll-area';

import { useUpdateApplication } from '@api/tanstack/applications.queries';
import type { ProjectApplicationDetail } from '@api/types/application';
import { ApplicationStatus, ApplicationStatusEnum } from '@api/types/common';

import ApplicantChallengerInfo from '@common/components/ApplicantChallengerInfo';

import ApplicationStatusBadge from '@features/matching/components/ApplicationStatusBadge';
import MatchingRoundBadge from '@features/matching/components/matching-info/MatchingRoundBadge';
import ConfirmDialog from '@features/projects/components/ConfirmDialog';

import ApplicationAnswerList from '@features/applications/ApplicationAnswerList';
import ApplicationInfo from '@features/applications/ApplicationInfo';

const ApplicantApplicationCard = ({ application }: { application: ProjectApplicationDetail }) => {
  const [selectedStatus, setSelectedStatus] = useState<ApplicationStatus | null>(
    application.status,
  );
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [isCollapsibleOpen, setIsCollapsibleOpen] = useState(false);

  const { mutate: updateApplicationStatus } = useUpdateApplication();

  const handleButtonClick = (newStatus: ApplicationStatus) => {
    setSelectedStatus(newStatus);
    setIsDialogOpen(true);
  };

  const handleStatusChange = (newStatus: ApplicationStatus | null) => {
    if (!newStatus) throw new Error('변경할 상태값이 존재하지 않습니다.');
    const toKoreanStatus =
      selectedStatus === 'CONFIRMED'
        ? '합격'
        : selectedStatus === 'REJECTED'
          ? '불합격'
          : '(알 수 없음)';

    updateApplicationStatus(
      { id: application.id, request: { status: newStatus } },
      {
        onSuccess: async () => {
          toast.success(`지원서 ${toKoreanStatus} 처리에 성공하였습니다.`, {});
          setIsDialogOpen(false);
          setSelectedStatus(null);
        },
        onError: (err) => {
          setIsDialogOpen(false);
          toast.error(`지원서 ${toKoreanStatus} 처리에 실패하였습니다.`, {
            description: err.message,
          });
        },
      },
    );
  };

  return (
    <>
      {/*합/불 처리시 double-check 용 dialog*/}
      <ConfirmDialog
        isOpen={isDialogOpen}
        handleConfirm={() => handleStatusChange(selectedStatus)}
        onChange={(state) => setSelectedStatus(state)}
        pendingAction={selectedStatus}
      />
      {/*카드 본문 내용*/}
      <div className="rounded-20pxr flex w-full flex-col gap-y-2 border border-gray-400 px-6 py-7">
        {/*최상단 : 상태 뱃지 + 매칭 info justify-between*/}
        <div className={'flex w-full flex-row items-center justify-between'}>
          <ApplicationStatusBadge status={application.status} />
          <MatchingRoundBadge roundName={application.matchingRoundName} />
        </div>

        {/*중간 정보 : 학교 이름/닉네임, 다음에 지원서 ID랑 제출일*/}

        <ApplicantChallengerInfo
          school={application.applicantSchoolName}
          part={application.applicantPart}
          name={application.applicantName}
          nickname={application.applicantNickname}
        />

        <ApplicationInfo id={application.id} createdAt={application.createdAt} />

        <Collapsible open={isCollapsibleOpen} onOpenChange={setIsCollapsibleOpen}>
          <CollapsibleTrigger asChild>
            <Button variant={'outline'} className={'my-3 h-13 w-full text-xl font-semibold'}>
              {isCollapsibleOpen ? <ChevronUpIcon /> : <ChevronDownIcon />}
              {isCollapsibleOpen ? '응답 숨기기' : '응답 보기'}
            </Button>
          </CollapsibleTrigger>
          <CollapsibleContent>
            {/*카드 헤더: 폼 제목과 설명, 합불 여부를 표시함*/}
            <ScrollArea className={'h-100'}>
              <ApplicationAnswerList responses={application.responses} />
            </ScrollArea>
          </CollapsibleContent>
        </Collapsible>

        {/*제출 상태일 경우 합불 처리 가능하도록 버튼 배치*/}
        {application.status === ApplicationStatusEnum.PENDING && (
          <div className={'mt-5 flex w-full flex-row items-center gap-x-2'}>
            <Button
              className={'h-15 flex-1 px-10 py-3 text-2xl'}
              variant={'destructive'}
              onClick={() => handleButtonClick('REJECTED')}
            >
              <XIcon className="mr-1 h-10 w-10" />
              불합격
            </Button>
            <Button
              className={'h-15 flex-1 bg-green-500 px-10 py-3 text-2xl'}
              variant={'default'}
              onClick={() => handleButtonClick('CONFIRMED')}
            >
              <CheckIcon className="mr-1 h-10 w-10" />
              합격
            </Button>
          </div>
        )}
      </div>
    </>
  );
};

export default ApplicantApplicationCard;
