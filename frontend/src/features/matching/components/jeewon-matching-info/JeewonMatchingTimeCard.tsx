'use client';

import { clsx } from 'clsx';
import { CalendarIcon } from 'lucide-react';

const JeewonMatchingTimeCard = ({
  date,
  isStart,
}: {
  date: { date: string; time: string };
  isStart: boolean;
}) => {
  return (
    <div className="border-border/50 bg-background flex flex-grow flex-row items-center gap-3 rounded-md border p-3">
      <CalendarIcon className={clsx('h-8 w-8', isStart ? 'text-green-600' : 'text-red-600')} />
      <div className="flex-1">
        <p className="text-muted-foreground text-sm">{isStart ? '시작 시간' : '종료 시간'}</p>
        <div className="flex items-baseline gap-2">
          <p className="text-lg font-medium">{date.date}</p>
          <p className="text-muted-foreground text-base">{date.time}</p>
        </div>
      </div>
    </div>
  );
};

export default JeewonMatchingTimeCard;
