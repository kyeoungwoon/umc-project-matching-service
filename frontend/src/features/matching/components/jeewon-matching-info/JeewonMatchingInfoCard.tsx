'use client';

import { ClockIcon } from 'lucide-react';
import { toast } from 'sonner';

import { Badge } from '@styles/components/ui/badge';
import { Button } from '@styles/components/ui/button';
import { Card } from '@styles/components/ui/card';

import {
  useDeleteMatchingRound,
  useUpdateMatchingRound,
} from '@api/tanstack/matching-rounds.queries';
import { ProjectMatchingRoundResponse } from '@api/types/matching-round';

import { formatDate } from '@common/utils/format-dates';

import { useDoubleCheck } from '@common/hooks/useDoubleCheck';

import DoubleCheckDialog from '@common/components/DoubleCheckDialog';

const JeewonMatchingInfoCard = ({ data }: { data: ProjectMatchingRoundResponse }) => {
  const { mutate: deleteMatching } = useDeleteMatchingRound();
  const { mutate: updateMatching } = useUpdateMatchingRound();

  const { onOpen, dialogProps } = useDoubleCheck({
    title: '매칭 차수 삭제하기',
    description: '삭제된 매칭 차수는 복구할 수 없습니다.',
    variant: 'destructive',
    confirmText: '삭제',
    cancelText: '취소',
  });
  const now = new Date();
  const startDate = formatDate(data?.startAt);
  const endDate = formatDate(data?.endAt);
  const decisionDeadline = formatDate(data?.decisionDeadlineAt);

  const isActive = now >= new Date(data.startAt) && now <= new Date(data.endAt);
  const isUpcoming = now < new Date(data.startAt);
  const isEnded = now > new Date(data.endAt);

  const handleDelete = () => {
    onOpen(() =>
      deleteMatching(data.id, {
        onSuccess: () => {
          toast.success(`${data.name} 매칭 차수가 삭제되었습니다.`, {
            position: 'top-center',
          });
        },
        onError: (err) => {
          toast.error(`매칭 차수 삭제에 실패했습니다`, {
            description: err.message,
            position: 'top-center',
          });
        },
      }),
    );
  };

  const getStatusBadge = () => {
    if (isActive)
      return (
        <Badge className="border-none bg-[#EFF6FF] px-2 py-1 text-sm text-[#2563EB]">진행 중</Badge>
      );
    if (isUpcoming)
      return (
        <Badge
          className={'border-none bg-[F5F3FF] px-2 py-1 text-sm text-[#7C3AED]'}
          variant="secondary"
        >
          예정
        </Badge>
      );
    if (isEnded)
      return (
        <Badge
          className={'border-none bg-[#F1F5F9] px-2 py-1 text-sm text-[#64748B]'}
          variant="outline"
        >
          종료
        </Badge>
      );
    return null;
  };

  return (
    <>
      <DoubleCheckDialog {...dialogProps} />
      <Card className="flex w-full flex-col items-center justify-between gap-x-4 overflow-hidden px-5">
        {/* 라운드 이름 */}
        <div className={'flex w-full flex-row items-center justify-between gap-x-2'}>
          {/*<div className={'flex flex-row items-center gap-x-2'}>*/}
          {/*  <div className="bg-primary/10 flex h-8 w-8 items-center justify-center rounded-full">*/}
          {/*    <ClockIcon className="text-primary h-4 w-4" />*/}
          {/*  </div>*/}
          <p className="text-foreground text-2xl font-semibold">{data.name}</p>
          {/*</div>*/}
          {getStatusBadge()}
        </div>

        <div className={'flex w-full flex-row justify-between'}>
          {/* 기간 정보 */}
          <div className="flex flex-col gap-1">
            <div className="flex flex-row gap-x-2">
              <div className="flex items-baseline gap-2">
                <p className="text-lg font-medium">{startDate.date}</p>
                <p className="text-muted-foreground text-base">{startDate.time}</p>
              </div>
              <span>~</span>
              <div className="flex items-baseline gap-2">
                <p className="text-lg font-medium">{endDate.date}</p>
                <p className="text-muted-foreground text-base">{endDate.time}</p>
              </div>
            </div>
            <div className="flex items-center gap-2 text-sm text-muted-foreground">
              <ClockIcon className="h-4 w-4" />
              <span>합/불 결정 마감: </span>
              <span className="font-medium text-foreground">{decisionDeadline.date}</span>
              <span className="text-muted-foreground">{decisionDeadline.time}</span>
            </div>
            {data.description ? (
              <p className="text-sm text-muted-foreground">{data.description}</p>
            ) : null}
          </div>
          <div className={'flex flex-row gap-x-1'}>
            {/*<Button variant="outline" className="flex items-center gap-2">*/}
            {/*  수정*/}
            {/*</Button>*/}
            <Button
              variant="destructive"
              className="flex items-center gap-2 text-base"
              onClick={handleDelete}
            >
              삭제
            </Button>
          </div>
        </div>
      </Card>
    </>
  );
};

export default JeewonMatchingInfoCard;
