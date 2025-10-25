'use client';

import { useState } from 'react';

import { toast } from 'sonner';

import { Button } from '@styles/components/ui/button';
import { Card, CardContent } from '@styles/components/ui/card';
import { Input } from '@styles/components/ui/input';

import { useCreateMatchingRound } from '@api/tanstack/matching-rounds.queries';

import DatetimePicker from '@common/components/DatetimePicker';

const CreateMatchingRoundCard = () => {
  const { mutate: createMatchingRound } = useCreateMatchingRound();

  const chapterId = '1';

  const startDatetime = new Date(Date.now() - 60 * 60 * 1000);
  const endDatetime = new Date(Date.now() + 60 * 60 * 1000);

  const [start, setStart] = useState<Date | undefined>(startDatetime);
  const [end, setEnd] = useState<Date | undefined>(endDatetime);

  const [roundName, setRoundName] = useState<string>('');

  const handleCreateMatchingRound = () => {
    if (!start || !end || !roundName) {
      return toast.error('이름 및 시작/종료 날짜를 모두 입력해야 합니다.', {
        richColors: true,
      });
    }

    // 매칭 라운드 생성 로직을 여기에 작성하세요.
    createMatchingRound(
      {
        name: roundName,
        chapterId,
        startAt: start.toISOString(),
        endAt: end.toISOString(),
      },
      {
        onSuccess: (data) => {
          toast.success('매칭 라운드가 성공적으로 생성되었습니다.', {
            richColors: true,
            position: 'top-center',
          });
        },
        onError: (err) => {
          console.error('매칭 라운드 생성에 실패했습니다:', err);
          toast.error('매칭 라운드 생성에 실패했습니다. 다시 시도해주세요.', {
            richColors: true,
            description: err.message,
            position: 'top-center',
          });
        },
      },
    );
  };

  return (
    <Card className={'w-150'}>
      <CardContent>
        <div className="space-y-4">
          <Input
            value={roundName}
            onChange={(e) => setRoundName(e.target.value)}
            type="text"
            placeholder="매칭 라운드 이름"
          />
          {/* 시간은 KST 기준으로 저장함 */}
          <DatetimePicker date={start} onDateChange={(date) => setStart(date)} />
          <DatetimePicker date={end} onDateChange={(date) => setEnd(date)} />
          <Button type="button" onClick={handleCreateMatchingRound}>
            매칭 라운드 생성
          </Button>
        </div>
      </CardContent>
    </Card>
  );
};

export default CreateMatchingRoundCard;
