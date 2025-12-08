'use client';

import { QuestionTypeEnum } from '@api/types/common';
import { ApplicationFormQuestion } from '@api/types/question';

import { FileQuestion } from '@features/projects/components/FileQuestion';

import { ObjectiveQuestion } from './ObjectiveQuestion';
import { SubjectiveQuestion } from './SubjectiveQuestion';

interface QuestionFieldProps {
  tanstackForm: any;
  question: ApplicationFormQuestion;
  index: number;
}

export const QuestionField = ({ tanstackForm, question, index }: QuestionFieldProps) => {
  // This component is designed for the 'answering' mode.
  // For 'creation' mode, we would need a different set of components
  // to build the FormQuestionDto object itself, likely using a dynamic projects for questions and options.

  return (
    <tanstackForm.Field
      name={`answers[${index}].values`}
      children={(field: any) => {
        if (question.type === QuestionTypeEnum.SUBJECTIVE) {
          return <SubjectiveQuestion field={field} question={question} />;
        } else if (question.type === QuestionTypeEnum.FILE) {
          return <FileQuestion field={field} question={question} />;
        }

        return <ObjectiveQuestion field={field} question={question} />;
      }}
    />
  );
};
