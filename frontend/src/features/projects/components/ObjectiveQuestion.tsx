'use client';

import { Button } from '@styles/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@styles/components/ui/card';
import { Checkbox } from '@styles/components/ui/checkbox';
import { Label } from '@styles/components/ui/label';
import { RadioGroup, RadioGroupItem } from '@styles/components/ui/radio-group';

import { QuestionTypeEnum } from '@api/types/common';
import { ApplicationFormQuestion } from '@api/types/question';

import RequiredStar from '@features/projects/components/forms/RequiredStar';

interface ObjectiveQuestionProps {
  field: any;
  question: ApplicationFormQuestion;
}

export const ObjectiveQuestion = ({ field, question }: ObjectiveQuestionProps) => {
  const { type, options } = question;

  return (
    <Card>
      <CardHeader>
        <CardTitle>
          {question.title}
          {question.required && <RequiredStar />}
        </CardTitle>
        <CardDescription className={'whitespace-pre-line'}>{question.description}</CardDescription>
      </CardHeader>
      <CardContent>
        {type === QuestionTypeEnum.SINGLE_CHOICE ? (
          <RadioGroup
            value={field.state.value?.[0] ?? ''}
            onValueChange={(val) => {
              field.handleChange([val]);
            }}
            onBlur={field.handleBlur}
          >
            {options.map((option) => (
              <div key={option} className="flex items-center space-x-2">
                <RadioGroupItem value={option} id={option} />
                <Label htmlFor={option}>{option}</Label>
              </div>
            ))}
            <Button type={'button'} className={'h-8 w-20'} onClick={() => field.handleChange([''])}>
              선택 해제
            </Button>
          </RadioGroup>
        ) : (
          <div className="space-y-2">
            {options.map((option) => (
              <div key={option} className="flex items-center space-x-2">
                <Checkbox
                  id={option}
                  checked={field.state.value?.includes(option)}
                  onCheckedChange={(checked) => {
                    let newValues = field.state.value || [];

                    if (checked) {
                      newValues = [...newValues, option];
                    } else {
                      newValues = newValues.filter((v: string) => v !== option);
                    }

                    newValues = newValues.filter((v: string) => v !== '');
                    if (!newValues.length) newValues = [''];
                    field.handleChange(newValues);
                  }}
                />
                <Label htmlFor={option}>{option}</Label>
              </div>
            ))}
          </div>
        )}
      </CardContent>
    </Card>
  );
};
