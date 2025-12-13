'use client';

import * as React from 'react';
import { useState } from 'react';

import { ChevronDownIcon } from 'lucide-react';

import { Button } from '@styles/components/ui/button';
import { Calendar } from '@styles/components/ui/calendar';
import { Input } from '@styles/components/ui/input';
import { Label } from '@styles/components/ui/label';
import { Popover, PopoverContent, PopoverTrigger } from '@styles/components/ui/popover';

export interface DatetimePickerProps {
  date: Date | undefined;
  onDateChange: (date: Date | undefined) => void;
}

export const DatetimePicker = ({ date, onDateChange }: DatetimePickerProps) => {
  const [open, setOpen] = useState(false);

  // date에서 시간 추출 (HH:mm:ss 형식)
  const timeValue = date
    ? `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}`
    : '10:30:00';

  // 시간 변경 핸들러
  const handleTimeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const timeString = e.target.value; // "HH:mm:ss" 형식
    // console.log(timeString);

    // 빈 값이거나 불완전한 입력인 경우 처리하지 않음
    if (!timeString || timeString.length < 5) {
      return;
    }

    const [hours, minutes, seconds] = timeString.split(':').map(Number);

    // 유효하지 않은 숫자가 있는 경우 처리하지 않음
    if (isNaN(hours) || isNaN(minutes)) {
      return;
    }

    // 기존 date가 있으면 그 날짜에 시간만 변경, 없으면 오늘 날짜 사용
    const newDate = date ? new Date(date) : new Date();
    newDate.setHours(hours, minutes, seconds || 0);

    onDateChange(newDate);
  };

  // 날짜 변경 핸들러
  const handleDateChange = (selectedDate: Date | undefined) => {
    if (!selectedDate) {
      onDateChange(undefined);
      setOpen(false);
      return;
    }

    // 기존 시간 유지하면서 날짜만 변경
    if (date) {
      selectedDate.setHours(date.getHours(), date.getMinutes(), date.getSeconds());
    }

    onDateChange(selectedDate);
    setOpen(false);
  };

  return (
    <div className="flex gap-4">
      <div className="flex flex-col gap-3">
        <Label htmlFor="date-picker" className="px-1">
          날짜
        </Label>
        <Popover open={open} onOpenChange={setOpen}>
          <PopoverTrigger asChild>
            <Button variant="outline" id="date-picker" className="w-32 justify-between font-normal">
              {date
                ? date.toLocaleDateString('ko-KR', {
                    year: 'numeric',
                    month: '2-digit',
                    day: '2-digit',
                  })
                : '날짜 선택'}
              <ChevronDownIcon />
            </Button>
          </PopoverTrigger>
          <PopoverContent className="w-auto overflow-hidden p-0" align="start">
            <Calendar
              mode="single"
              selected={date}
              captionLayout="dropdown"
              onSelect={handleDateChange}
            />
          </PopoverContent>
        </Popover>
      </div>
      <div className="flex flex-col gap-3">
        <Label htmlFor="time-picker" className="px-1">
          시간
        </Label>
        <Input
          type="time"
          id="time-picker"
          step="1"
          value={timeValue}
          onChange={handleTimeChange}
          className="bg-background appearance-none [&::-webkit-calendar-picker-indicator]:hidden [&::-webkit-calendar-picker-indicator]:appearance-none"
        />
      </div>
    </div>
  );
};

export default DatetimePicker;
