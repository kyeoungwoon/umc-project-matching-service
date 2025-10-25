'use client';

import { useState } from 'react';

import { Button } from '@styles/components/ui/button';
import { Card, CardContent, CardHeader } from '@styles/components/ui/card';
import { Checkbox } from '@styles/components/ui/checkbox';
import { Input } from '@styles/components/ui/input';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@styles/components/ui/select';

const QuestionCard = () => {
  const [selectedValue, setSelectedValue] = useState('singleChoice');

  return (
    <Card>
      <CardHeader className="align-items flex justify-center p-4">
        <Input
          placeholder="질문 제목 입력하기"
          className="rounded-none border-0 border-b text-lg font-medium"
        />
        <Select value={selectedValue} onValueChange={setSelectedValue}>
          <SelectTrigger className="">
            <SelectValue placeholder="" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="singleChoice">{'객관식 단일선택'}</SelectItem>
            <SelectItem value="multiChoice">{'객관식 복수선택'}</SelectItem>
            <SelectItem value="writing">{'주관식'}</SelectItem>
          </SelectContent>
        </Select>
      </CardHeader>
      <CardContent className="p-4">
        {selectedValue === 'singleChoice' && (
          <div className="flex flex-col gap-2">
            <div className="align-items flex justify-center">
              <Checkbox disabled />
              <Input
                placeholder="선택지 1"
                className="rounded-none border-0 border-b shadow-none focus:outline-none"
              />
            </div>
            <div>
              <Checkbox disabled />
              <Input
                placeholder="선택지 1"
                className="rounded-none border-0 border-b shadow-none focus:outline-none"
              />
            </div>
            <div>
              <Checkbox disabled />
              <Button
                variant="ghost"
                className="p-0 font-light underline hover:bg-transparent"
                onClick={() => {
                  /*TODO*/
                }}
              >
                {'선택지 추가하기'}
              </Button>
            </div>
          </div>
        )}
        {selectedValue === 'multiChoice' && (
          <div className="flex flex-col gap-2">
            <Input placeholder="옵션 1" className="rounded-md border-0" />
            <Input placeholder="옵션 2" className="rounded-md border-0" />
          </div>
        )}
        {selectedValue === 'writing' && (
          <div>
            <p className="text-muted-foreground">
              주관식 질문에는 별도의 선택지가 필요하지 않습니다.
            </p>
          </div>
        )}
      </CardContent>
    </Card>
  );
};

export default QuestionCard;
