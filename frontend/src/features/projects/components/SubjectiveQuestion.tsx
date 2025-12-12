'use client';

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@styles/components/ui/card';
import { Textarea } from '@styles/components/ui/textarea';

import { ApplicationFormQuestion } from '@api/types/question';

import RequiredStar from '@features/projects/components/forms/RequiredStar';

interface SubjectiveQuestionProps {
  field: any;
  question: ApplicationFormQuestion;
}

export const SubjectiveQuestion = ({ field, question }: SubjectiveQuestionProps) => {
  return (
    <Card>
      <CardHeader>
        {/*TODO: title 부분 분리해서 스타일 통일하기*/}
        <CardTitle>
          {question.title}
          {question.required && <RequiredStar />}
        </CardTitle>
        <CardDescription className={'whitespace-pre-line'}>{question.description}</CardDescription>
      </CardHeader>
      <CardContent>
        <Textarea
          value={field.state.value?.[0] ?? ''}
          onBlur={field.handleBlur}
          onChange={(e) => field.handleChange([e.target.value || ''])}
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              e.preventDefault();
            }
          }}
          placeholder="답변을 입력해주세요."
        />
      </CardContent>
    </Card>
  );
};
