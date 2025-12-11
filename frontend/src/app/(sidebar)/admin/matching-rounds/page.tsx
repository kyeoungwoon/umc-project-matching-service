'use client';

import { useState } from 'react';

import { toast } from 'sonner';

import { Button } from '@styles/components/ui/button';
import { Card, CardContent } from '@styles/components/ui/card';

import { useGetMatchingRounds } from '@api/tanstack/matching-rounds.queries';

import DatetimePicker from '@common/components/DatetimePicker';
import DefaultSkeleton from '@common/components/DefaultSkeleton';
import UpmsHeader from '@common/components/upms/UpmsHeader';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

import JeewonMatchingInfoCard from '@features/matching/components/jeewon-matching-info/JeewonMatchingInfoCard';
import CreateMatchingRoundCard from '@features/projects/components/CreateMatchingRoundCard';

const MatchingRounds = () => {
  const user = useGetUser();
  const chapterId = user?.info?.chapterId;
  const chapterName = user?.info?.chapterName;

  const yesterday = new Date();
  yesterday.setDate(yesterday.getDate() - 1);

  const nextMonth = new Date();
  nextMonth.setMonth(nextMonth.getMonth() + 1);

  const [startDate, setStartDate] = useState<Date>(yesterday);
  const [endDate, setEndDate] = useState<Date>(nextMonth);

  const { data, isFetching, refetch, isLoading } = useGetMatchingRounds({
    chapterId,
    startTime: startDate,
    endTime: endDate,
  });

  if (!user) {
    return <DefaultSkeleton />;
  }

  const section = {
    title: `${chapterName} 지부 매칭 차수 관리`,
    description: '매칭 차수는 기간을 중복해서 생성이 불가능하며, 합/불 결정 마감 시간을 함께 관리합니다.',
  };

  const handleSearch = () => {
    if (!startDate || !endDate) {
      return toast.error('조회할 시작/종료 시간을 모두 선택해주세요.', { position: 'top-center' });
    }

    if (startDate > endDate) {
      return toast.error('시작 시간이 종료 시간보다 늦을 수 없습니다.', { position: 'top-center' });
    }

    refetch().then(() => toast.success('조회가 완료되었습니다.', { position: 'top-center' }));
  };

  if (isLoading) return <DefaultSkeleton />;

  return (
    <>
      <UpmsHeader section={section} />
      <div className={'flex flex-col items-start p-5'}>
        <CreateMatchingRoundCard chapterId={chapterId} />
        <Card className="mt-6 w-full">
          <CardContent className={'flex flex-col items-start justify-center gap-y-1'}>
            <div className="mb-4 flex flex-wrap items-end gap-4">
              <span className="font-medium">조회 기간</span>
              <DatetimePicker
                date={startDate}
                onDateChange={(date) => date && setStartDate(date)}
              />
              <span className="text-muted-foreground">~</span>
              <DatetimePicker date={endDate} onDateChange={(date) => date && setEndDate(date)} />
              <Button
                disabled={isFetching}
                onClick={handleSearch}
              >
                {isFetching ? '조회 중...' : '조회'}
              </Button>
            </div>
            <p className={'ml-1'}>매칭 차수 목록</p>
            <div className={'mt-2 flex w-full flex-col gap-y-4'}>
              {data && data.length > 0 ? (
                data.map((round) => (
                  <JeewonMatchingInfoCard key={round.id} data={round} />
                  // <div key={round.id} className="mb-4 rounded border p-4">
                  //   <div>매칭 라운드 ID: {round.id}</div>
                  //   <div>매칭 차수 이름: {round.name}</div>
                  //
                  //   <div>시작 날짜: {new Date(round.startDatetime).toLocaleString('ko-KR')}</div>
                  //   <div>종료 날짜: {new Date(round.endDatetime).toLocaleString('ko-KR')}</div>
                  // </div>
                ))
              ) : (
                <div>해당 기간에 매칭 라운드가 없습니다.</div>
              )}
            </div>
          </CardContent>
        </Card>
      </div>
    </>
  );
};

export default MatchingRounds;
