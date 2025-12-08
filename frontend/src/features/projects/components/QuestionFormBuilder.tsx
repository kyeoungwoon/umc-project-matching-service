'use client';

import { useForm } from '@tanstack/react-form';
import { toast } from 'sonner';
import { z } from 'zod';

import { Button } from '@styles/components/ui/button';

import { QuestionType, QuestionTypeEnum } from '@api/types/common';
import { QuestionItem } from '@api/types/question';

import { extractErrorMessages } from '@common/utils/form-errors';

import QuestionCreator from '@features/projects/components/QuestionCreator';

interface QuestionFormBuilderProps {
  onSubmit: (data: { questions: QuestionItem[] }) => void;
  initialData?: QuestionItem[];
}

const formSchema = z.object({
  questions: z.array(
    z.object({
      questionNo: z.number(),
      title: z.string().min(1, '질문 제목을 입력해주세요.'),
      description: z.string().optional(),
      type: z.enum(['SINGLE_CHOICE', 'MULTIPLE_CHOICE', 'SUBJECTIVE', 'FILE']),
      options: z.array(z.string().min(1, '선택지를 입력해주세요.')),
      required: z.boolean(),
      fileAccept: z.string().optional(),
      fileMaxSize: z.number().optional(),
    }),
  ),
});

// type FormValues = z.infer<typeof formSchema>;

export const QuestionFormBuilder = ({ onSubmit, initialData }: QuestionFormBuilderProps) => {
  const form = useForm({
    defaultValues: {
      questions: initialData ?? [],
    },
    onSubmit: ({ value }) => {
      onSubmit(value);
    },
    validators: {
      onSubmit: formSchema,
      onChange: formSchema,
      onBlur: formSchema,
    },
  });

  return (
    <form
      onSubmit={(e) => {
        e.preventDefault();
        e.stopPropagation();

        if (!form.state.isValid) {
          toast.error('유효하지 않은 폼입니다. 아래 사항들을 확인해주세요.', {
            position: 'top-center',
            description: (
              <>
                <p>- 각 질문의 제목은 필수로 입력해야 합니다.</p>
                <p>- 객관식 질문은 선택지가 모두 유효하여야 합니다.</p>
              </>
            ),
          });
          return;
        }

        form.handleSubmit();
      }}
      className="space-y-4"
    >
      <form.Field
        name="questions"
        mode="array"
        children={(field) => (
          <div className="space-y-4">
            {field.state.value.map((_, index) => (
              <QuestionCreator
                key={index}
                tanstackForm={form}
                index={index}
                remove={() => field.removeValue(index)}
              />
            ))}

            {/* 폼에 속한 모든 문항들이 표시되고 나서 가장 최하단임! */}
            {/* 문항 추가 및 지원서 저장 버튼 */}
            <div className="flex justify-between">
              <Button
                type="button"
                variant="secondary"
                onClick={() =>
                  field.pushValue({
                    questionNo: field.state.value.length + 1,
                    title: '',
                    description: '',
                    type: QuestionTypeEnum.SUBJECTIVE as QuestionType,
                    required: false,
                    options: [],
                    fileAccept: undefined,
                    fileMaxSize: undefined,
                  })
                }
              >
                새로운 질문 추가하기
              </Button>
              <Button type={'submit'}>지원서 저장</Button>
            </div>
          </div>
        )}
      />
    </form>
  );
};
