'use client';

import { format } from 'date-fns';

import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@styles/components/ui/dialog';
import { Separator } from '@styles/components/ui/separator';

import { useGetApplication } from '@api/tanstack/applications.queries';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

import ApplicationStatusBadge from '@features/matching/components/ApplicationStatusBadge';

interface ApplicationDetailDialogProps {
  applicationId: string | null;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export function ApplicationDetailDialog({
  applicationId,
  open,
  onOpenChange,
}: ApplicationDetailDialogProps) {
  if (!applicationId) {
    return null;
  }

  const { data: application, isLoading: isApplicationLoading } = useGetApplication(applicationId);

  if (isApplicationLoading || !application) {
    return <DefaultSkeleton />;
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-h-[80vh] max-w-3xl overflow-y-auto px-10 py-9">
        <DialogHeader className={'flex flex-row items-end justify-between gap-4'}>
          <div className={'flex flex-col gap-y-2'}>
            <DialogTitle className="text-2xl">지원서</DialogTitle>
            <DialogDescription>지원자의 상세 정보를 확인할 수 있습니다.</DialogDescription>
          </div>
          <ApplicationStatusBadge status={application.status} />
        </DialogHeader>

        <Separator />

        <div className="space-y-6">
          {/* 지원자 정보 */}
          <div>
            <h3 className="mb-3 text-lg font-bold">지원자 정보</h3>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <p className="text-muted-foreground text-sm">이름</p>
                <p className="">{application.applicantName}</p>
              </div>
              <div>
                <p className="text-muted-foreground text-sm">닉네임</p>
                <p className="">{application.applicantNickname}</p>
              </div>
              <div>
                <p className="text-muted-foreground text-sm">소속 대학</p>
                <div className="flex flex-wrap gap-1">{application.applicantSchoolName}</div>
              </div>
            </div>
          </div>

          <Separator />

          {/* 프로젝트 정보 */}
          <div>
            <h3 className="mb-3 text-lg font-bold">프로젝트 정보</h3>
            <div className="flex flex-col gap-y-5">
              <div>
                <p className="text-muted-foreground text-sm">프로젝트 제목</p>
                <p className="font-medium">{application.projectName}</p>
              </div>
              <div>
                <p className="text-muted-foreground text-sm">지원 폼 제목</p>
                <p className="font-medium">{application.formTitle}</p>
              </div>
            </div>
          </div>

          <Separator />

          {/* 지원 시간 */}
          <div>
            <h3 className="mb-3 text-lg font-bold">지원 일시</h3>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <p className="text-muted-foreground text-sm">지원 일시</p>
                <p className="font-medium">
                  {format(new Date(application.createdAt), 'yyyy년 MM월 dd일 HH:mm:ss')}
                </p>
              </div>
            </div>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
}
