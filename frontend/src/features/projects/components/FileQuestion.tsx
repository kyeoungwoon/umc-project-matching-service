'use client';

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@styles/components/ui/card';
import { FieldError } from '@styles/components/ui/field';
import { Label } from '@styles/components/ui/label';

import { ApplicationFormQuestion } from '@api/types/question';

import { FileUploader } from '@common/components/FileUploader';

import RequiredStar from '@features/projects/components/forms/RequiredStar';

interface FileQuestionProps {
  field: any;
  question: ApplicationFormQuestion;
}

export const FileQuestion = ({ field, question }: FileQuestionProps) => {
  const isInvalid = field.state.meta.isTouched && !field.state.meta.isValid;
  const currentValue = field.state.value?.[0] || '';

  return (
    <Card>
      <CardHeader>
        {/*TODO: title 부분 분리해서 스타일 통일하기*/}
        <CardTitle>
          {question.title}
          {question.required && <RequiredStar />}
        </CardTitle>
        <CardDescription>{question.description}</CardDescription>
      </CardHeader>
      <CardContent className={'flex flex-col gap-y-2'}>
        <div className="border-border bg-muted/30 space-y-4 rounded-lg border p-4">
          <div className="flex items-center gap-2">
            <Label className="text-base font-semibold">파일 업로드형 질문 공지</Label>
          </div>

          <div className="space-y-2">
            <Label>업로드 가능한 파일 형식 및 용량 안내</Label>
            <p className="text-muted-foreground text-sm">
              파일 업로드는 PDF 파일만 허용되며, 최대 파일 크기는 100MB 입니다.
            </p>
          </div>
          <div className="space-y-2">
            <Label>파일 제출 관련 안내</Label>
            <p className="text-muted-foreground text-sm whitespace-pre-line">
              제출하신 파일은 Seoul Region 소속의 AWS S3에 업로드되며, 지원하신 프로젝트의 PO 및
              운영진은 지원서 검토 목적으로 해당 파일을 열람 및 다운로드할 수 있습니다.{'\n\n'}본
              서비스를 통해 파일을 제출하시는 경우, 귀하는 다음 사항에 동의하는 것으로 간주됩니다:
              {'\n'}• UPMS 서비스가 제출된 파일을 저장, 처리 및 관리하는 것{'\n'}• 지원서 검토 및
              프로젝트 운영 목적으로 파일이 사용되는 것{'\n'}• 개인정보보호법 및 관련 법령에 따라
              파일이 처리되는 것{'\n\n'}
              제출하신 파일은 지원서 검토 기간 동안 안전하게 보관되며, 검토 완료 후 일정 기간 경과
              시 자동으로 삭제됩니다. 즉시 삭제를 원하시거나 문의사항이 있으신 경우, 좌측 사이드바의
              오픈채팅방을 통해 문의해 주시기 바랍니다.
            </p>
          </div>
        </div>
        <FileUploader
          accept={'application/pdf'} // pdf파일만 허용
          maxSize={100 * 1024 * 1024} // 기본 100MB
          buttonText="파일 선택"
          showPreview={true}
          value={currentValue}
          onUploadComplete={(url) => {
            // 파일 URL을 배열로 저장
            field.handleChange([url]);
          }}
        />
        {isInvalid && <FieldError errors={field.state.meta.errors} />}
      </CardContent>
    </Card>
  );
};
