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
  const questionLabel = ['객관식(단일 선택)', '객관식(복수 선택)', '주관식', '파일 업로드'];

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
                // 변경한 질문 유형이 주관식 또는 파일인 경우
                const isNowSubjective = newType === 'SUBJECTIVE';
                const isNowFile = newType === 'FILE';

                // 파일 타입인 경우 required를 false로 설정
                if (isNowFile) {
                  tanstackForm.setFieldValue(`questions[${index}].required`, false);
                }

                // 새로운 타입이 주관식 또는 파일인 경우 보기 목록을 지워버려야 함.
                if (isNowSubjective || isNowFile) {
                  tanstackForm.setFieldValue(`questions[${index}].options`, []);
                  return;
                }

                // 질문의 보기 목록을 가져옴
                const currentOptions =
                  tanstackForm.getFieldValue(`questions[${index}].options`) || [];

                // 객관식이고 (주관식, 파일이 아니고), 현재 보기가 없는 경우 기본으로 하나 추가
                if (!isNowSubjective && !isNowFile && currentOptions.length === 0) {
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
            const isFile = typeField.state.value === 'FILE';

            return (
              <>
                {isObjective && (
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
                )}

                {isFile && (
                  <div className="border-border bg-muted/30 space-y-4 rounded-lg border p-4">
                    <div className="flex items-center gap-2">
                      <Label className="text-base font-semibold">파일 응답형 질문 공지</Label>
                    </div>

                    <div className="space-y-2">
                      <Label htmlFor={`file-accept-${index}`}>
                        업로드 가능한 파일 형식 및 용량 안내
                      </Label>
                      <p className="text-muted-foreground text-sm">
                        파일 업로드는 PDF 파일만 허용되며, 최대 파일 크기는 100MB 입니다.
                      </p>
                    </div>

                    <div className="space-y-2">
                      <Label htmlFor={`file-accept-${index}`}>
                        지원자가 파일 제출 시 확인 방법 안내
                      </Label>
                      <p className="text-muted-foreground text-sm whitespace-pre-line">
                        • 제출된 파일은 AWS S3에 안전하게 업로드되며, 프로젝트 PO 및 운영진은 지원서
                        검토 목적으로 Cloudfront CDN을 통해 해당 파일에 접근할 수 있습니다. {'\n'}•
                        파일 다운로드 링크는 지원서 상세 페이지에서 확인 가능합니다.
                      </p>
                    </div>

                    <div className="space-y-2">
                      <Label htmlFor={`file-accept-${index}`}>파일 관련 처리 안내</Label>
                      <p className="text-muted-foreground text-sm whitespace-pre-line">
                        지원자가 파일을 제출하는 경우, 다음 사항에 동의하는 것으로 간주됩니다:{'\n'}
                        • UPMS 서비스가 제출된 파일을 저장, 처리 및 관리하는 것{'\n'}• 지원서 검토
                        및 프로젝트 운영 목적으로 파일이 사용되는 것{'\n'}• 개인정보보호법에 따라
                        지원자의 삭제 요청 시 운영진이 파일을 삭제 처리할 수 있는 것{'\n\n'}
                        제출된 파일은 지원서 검토 기간 동안 안전하게 보관되며, 검토 완료 후 일정
                        기간 경과 시 자동으로 삭제됩니다.{'\n\n'}
                        파일 업로드형 질문은 위와 같은 지원자 권리 보호를 위해 필수 응답 질문으로
                        설정할 수 없습니다.
                      </p>
                    </div>
                  </div>
                )}
              </>
            );
          }}
        />
      </CardContent>
      <CardFooter className={'flex flex-row justify-end gap-x-4'}>
        {/* 필수 응답 여부 체크  */}
        <tanstackForm.Field
          name={`questions[${index}].type`}
          children={(typeField) => {
            const isFile = typeField.state.value === 'FILE';

            return (
              <tanstackForm.Field
                name={`questions[${index}].required`}
                children={(field) => (
                  <div className="flex items-center space-x-2">
                    {isFile && (
                      <p className="text-sm text-red-600 italic">
                        파일 응답형 질문은 필수 응답 질문으로 설정할 수 없습니다.
                      </p>
                    )}
                    <Label
                      htmlFor={`required-${index}`}
                      className={isFile ? 'text-muted-foreground' : ''}
                    >
                      필수
                    </Label>
                    <Switch
                      checked={field.state.value && !isFile}
                      onCheckedChange={(checked) => {
                        if (!isFile) {
                          field.handleChange(checked);
                        }
                      }}
                      disabled={isFile}
                      id={`required-${index}`}
                    />
                  </div>
                )}
              />
            );
          }}
        />
        <Button type="button" variant="destructive" size="sm" onClick={() => remove(index)}>
          <Trash2Icon />
        </Button>
      </CardFooter>
    </Card>
  );
}
