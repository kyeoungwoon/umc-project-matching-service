'use client';

import { Trash2Icon, XIcon } from 'lucide-react';

import { Button } from '@styles/components/ui/button';
import { Card, CardContent, CardFooter, CardHeader } from '@styles/components/ui/card';
import { Label } from '@styles/components/ui/label';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@styles/components/ui/select';
import { Switch } from '@styles/components/ui/switch';

import { QuestionType, QuestionTypeEnum } from '@api/types/common';

import InputFormField from '@features/projects/components/forms/InputFormField';

interface QuestionCreatorProps {
  tanstackForm: any;
  index: number;
  remove: (index: number) => void;
}

export default function QuestionCreator({ tanstackForm, index, remove }: QuestionCreatorProps) {
  const questionLabel = ['객관식(단일 선택)', '객관식(복수 선택)', '주관식'];

  return (
    <Card className="space-y-4 p-4">
      {/* Header: 질문 index랑 삭제 버튼 */}
      {/* justify-between으로 양옆으로 배치하도록 함 */}
      <CardHeader className="flex flex-row items-center justify-between">
        <h3 className="font-bold">질문 #{index + 1}</h3>

        {/* 질문 유형 선택 부분, Field로 감싸져있고, 내부는 shadcn Select */}
        <tanstackForm.Field
          name={`questions[${index}].type`}
          children={(field) => (
            <Select
              onValueChange={(newType) => {
                field.handleChange(newType);
                // 변경한 질문 유형이 주관식인 경우
                const isNowSubjective = newType === 'SUBJECTIVE';

                // 새로운 타입이 주관식인 경우 보기 목록을 지워버려야 함.
                if (isNowSubjective) {
                  tanstackForm.setFieldValue(`questions[${index}].options`, []);
                  return;
                }

                // 질문의 보기 목록을 가져옴
                const currentOptions =
                  tanstackForm.getFieldValue(`questions[${index}].options`) || [];

                // 객관식이고 (주관식이 아니고), 현재 보기가 없는 경우 기본으로 하나 추가
                if (!isNowSubjective && currentOptions.length === 0) {
                  // setFieldValue로 배열에 빈 문자열 추가
                  tanstackForm.setFieldValue(`questions[${index}].options`, ['']);
                }
              }}
              value={field.state.value}
            >
              {/* 질문 유형 선택 드롭다운 */}
              <SelectTrigger className="w-40">
                <SelectValue placeholder="질문 유형" />
              </SelectTrigger>
              <SelectContent>
                {Object.values(QuestionTypeEnum).map((type, idx) => (
                  <SelectItem key={type} value={type}>
                    {questionLabel[idx]}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          )}
        />
      </CardHeader>

      {/* 보기 문항 내용들 */}
      <CardContent className="space-y-4">
        {/* 질문 제목 부분 */}
        <InputFormField
          tanstackForm={tanstackForm}
          name={`questions[${index}].title`}
          label={'질문 제목'}
        />

        {/* 질문 설명 부분 */}
        <InputFormField
          tanstackForm={tanstackForm}
          name={`questions[${index}].description`}
          label={'질문 설명'}
        />

        {/* 질문 유형 선택 & 필수 응답 여부 */}
        <div className="flex items-center space-x-4"></div>

        {/* 객관식인 경우에만 선택지를 표시함 */}
        <tanstackForm.Field
          name={`questions[${index}].type`}
          children={(typeField) => {
            const isObjective =
              typeField.state.value === 'SINGLE_CHOICE' ||
              typeField.state.value === 'MULTIPLE_CHOICE';

            return (
              isObjective && (
                <div className="space-y-2">
                  {/* 라벨 + 아래 map으로 선택지들 나열하는 방식 */}
                  <Label>선택지</Label>
                  <tanstackForm.Field
                    name={`questions[${index}].options`}
                    mode="array"
                    children={(field) => {
                      return (
                        <div className="space-y-2">
                          {field.state.value.map((_, i) => (
                            <div className={'flex flex-col gap-1'} key={i}>
                              <div className="flex items-center gap-2">
                                <InputFormField
                                  tanstackForm={tanstackForm}
                                  name={`questions[${index}].options[${i}]`}
                                />
                                <Button
                                  type="button"
                                  variant="ghost"
                                  size="sm"
                                  onClick={() => field.removeValue(i)}
                                >
                                  <XIcon />
                                </Button>
                              </div>
                            </div>
                          ))}
                          <Button
                            className={'mt-1'}
                            type="button"
                            variant="secondary"
                            onClick={() => field.pushValue('')}
                          >
                            선택지 추가
                          </Button>
                        </div>
                      );
                    }}
                  />
                </div>
              )
            );
          }}
        />
      </CardContent>
      <CardFooter className={'flex flex-row justify-end gap-x-4'}>
        {/* 필수 응답 여부 체크  */}
        <tanstackForm.Field
          name={`questions[${index}].required`}
          children={(field) => (
            <div className="flex items-center space-x-2">
              <Label htmlFor={`required-${index}`}>필수</Label>
              <Switch
                checked={field.state.value}
                onCheckedChange={field.handleChange}
                id={`required-${index}`}
              />
            </div>
          )}
        />
        <Button type="button" variant="destructive" size="sm" onClick={() => remove(index)}>
          <Trash2Icon />
        </Button>
      </CardFooter>
    </Card>
  );
}
