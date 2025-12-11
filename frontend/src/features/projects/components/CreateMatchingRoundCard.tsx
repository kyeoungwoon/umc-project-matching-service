'use client';

import { useState } from 'react';

import { toast } from 'sonner';

import { Button } from '@styles/components/ui/button';
import { Card, CardContent } from '@styles/components/ui/card';
import { Input } from '@styles/components/ui/input';
import { Textarea } from '@styles/components/ui/textarea';

import { useCreateMatchingRound } from '@api/tanstack/matching-rounds.queries';

import DatetimePicker from '@common/components/DatetimePicker';

const CreateMatchingRoundCard = ({ chapterId }: { chapterId?: string }) => {
  const { mutate: createMatchingRound } = useCreateMatchingRound();

  const defaultStartDatetime = new Date(Date.now() - 60 * 60 * 1000);
  const defaultEndDatetime = new Date(Date.now() + 60 * 60 * 1000);
  const defaultDecisionDeadline = new Date(defaultEndDatetime.getTime() + 24 * 60 * 60 * 1000);

  const [start, setStart] = useState<Date | undefined>(defaultStartDatetime);
  const [end, setEnd] = useState<Date | undefined>(defaultEndDatetime);
  const [decisionDeadline, setDecisionDeadline] = useState<Date | undefined>(
    defaultDecisionDeadline,
  );

  const [roundName, setRoundName] = useState<string>('');
  const [description, setDescription] = useState<string>('');

  const handleCreateMatchingRound = () => {
    if (!chapterId) {
      return toast.error('챕터 정보를 확인할 수 없어 매칭 라운드를 생성할 수 없습니다.', {
        position: 'top-center',
      });
    }

    if (!start || !end || !decisionDeadline || !roundName) {
      return toast.error('이름, 시작/종료/합불 마감 시간을 모두 입력해야 합니다.', {
        richColors: true,
      });
    }

    if (start >= end) {
      return toast.error('종료 시간은 시작 시간보다 늦어야 합니다.', { position: 'top-center' });
    }

    if (end >= decisionDeadline) {
      return toast.error('합불 결정 마감은 종료 시간 이후여야 합니다.', { position: 'top-center' });
    }

    // 매칭 라운드 생성 로직을 여기에 작성하세요.
    createMatchingRound(
      {
        name: roundName,
        chapterId,
        startAt: start.toISOString(),
        endAt: end.toISOString(),
        decisionDeadlineAt: decisionDeadline.toISOString(),
        description,
      },
      {
        onSuccess: (data) => {
          toast.success('매칭 라운드가 성공적으로 생성되었습니다.', {
            richColors: true,
            position: 'top-center',
          });
          setRoundName('');
          setDescription('');
          const nextStart = new Date(Date.now() - 60 * 60 * 1000);
          const nextEnd = new Date(Date.now() + 60 * 60 * 1000);
          const nextDecisionDeadline = new Date(nextEnd.getTime() + 24 * 60 * 60 * 1000);
          setStart(nextStart);
          setEnd(nextEnd);
          setDecisionDeadline(nextDecisionDeadline);
        },
        onError: (err) => {
          // console.error('매칭 라운드 생성에 실패했습니다:', err);
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
          <p className="text-lg font-semibold">매칭 라운드 생성</p>
          <Input
            value={roundName}
            onChange={(e) => setRoundName(e.target.value)}
            type="text"
            placeholder="매칭 라운드 이름"
          />
          <Textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="설명 (선택)"
          />
          {/* 시간은 KST 기준으로 저장함 */}
          <div className="space-y-2">
            <p className="text-muted-foreground text-sm">시작 시간</p>
            <DatetimePicker date={start} onDateChange={(date) => setStart(date)} />
          </div>
          <div className="space-y-2">
            <p className="text-muted-foreground text-sm">종료 시간</p>
            <DatetimePicker date={end} onDateChange={(date) => setEnd(date)} />
          </div>
          <div className="space-y-2">
            <p className="text-muted-foreground text-sm">합/불 결정 마감</p>
            <DatetimePicker
              date={decisionDeadline}
              onDateChange={(date) => setDecisionDeadline(date)}
            />
          </div>
          <Button type="button" onClick={handleCreateMatchingRound} disabled={!chapterId}>
            매칭 라운드 생성
          </Button>
        </div>
      </CardContent>
    </Card>
  );
};

export default CreateMatchingRoundCard;
