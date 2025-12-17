'use client';

import { useEffect, useState } from 'react';

import { ClockIcon } from 'lucide-react';

import { Label } from '@styles/components/ui/label';

import { ProjectMatchingRoundResponse } from '@api/types/matching-round';

import { formatDate } from '@common/utils/format-dates';

import { getTimeRemaining } from '@features/matching/utils/date-related';

import { Time } from '@features/matching/components/matching-info/Time';

const MatchingRoundInfoCard = ({ data }: { data: ProjectMatchingRoundResponse }) => {
  const [currentTime, setCurrentTime] = useState(new Date());
  const startDate = formatDate(data?.startAt);
  const endDate = formatDate(data?.endAt);

  const now = new Date();
  const isFuture = now < new Date(data.startAt);
  // 1초마다 갱신되는 남은 시간 계산
  useEffect(() => {
    const interval = setInterval(() => {
      // 강제로 리렌더링을 유발하는 상태 업데이트
      setCurrentTime(new Date());
    }, 1000);

    return () => clearInterval(interval);
  }, []);

  // const isActive = now >= new Date(data.startDatetime) && now <= new Date(data.endDatetime);
  // const isUpcoming = now < new Date(data.startDatetime);
  // const isEnded = now > new Date(data.endDatetime);

  return (
    <div className="my-5 flex w-full flex-row items-center justify-between gap-x-4 overflow-hidden px-5">
      {/*이름 */}
      <Label className={'shrink-0 grow-0 text-3xl tracking-tight text-gray-600'}>
        <span>{isFuture ? '다가오는 매칭' : '현재 매칭'}</span> |{' '}
        <p className="text-foreground font-semibold">{data.name}</p>
      </Label>

      {/*종료 시간 */}
      <div className={'flex shrink-0 grow-0 flex-row items-center justify-center gap-2'}>
        <Time datetime={startDate} /> ~ <Time datetime={endDate} />
      </div>

      <span className={'w-280pxr flex shrink-0 grow-0 flex-row items-center gap-x-3 text-gray-600'}>
        <ClockIcon />
        <span>{isFuture ? '시작까지' : '마감까지'}</span>
        {getTimeRemaining(isFuture ? data.startAt : data.endAt, currentTime)}
      </span>

      {/*<Progress*/}
      {/*  value={getProgress(data.startDatetime, data.endDatetime) * 100}*/}
      {/*  className="mt-4"*/}
      {/*/>*/}
    </div>
  );
};

export default MatchingRoundInfoCard;
