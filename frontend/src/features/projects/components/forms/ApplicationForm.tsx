'use client';

// Inner component to handle the projects logic after data is fetched
import { useEffect, useMemo } from 'react';

import { useRouter } from 'next/navigation';

import { useForm } from '@tanstack/react-form';
import { toast } from 'sonner';

import { Button } from '@styles/components/ui/button';

import { useCreateBulkApplicationResponse } from '@api/tanstack/application-responses.queries';
import { useCreateApplication } from '@api/tanstack/applications.queries';
import { useGetQuestions } from '@api/tanstack/questions.queries';
import { SingleQuestionResponseDto } from '@api/types/application';
import type { ProjectApplicationForm } from '@api/types/form';

import { ROUTES } from '@common/constants/routes.constants';

import { extractErrorMessages } from '@common/utils/form-errors';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

import { QuestionField } from '@features/projects/components/QuestionField';

export const ApplicationForm = ({
  formId,
}: {
  form: ProjectApplicationForm;
  projectId: string;
  formId: string;
}) => {
  const router = useRouter();

  const user = useGetUser();

  // Always call all hooks unconditionally
  const { data: questions, isLoading: isQuestionLoading } = useGetQuestions(formId);
  const { mutate: createApplication, isPending: isApplicationPending } = useCreateApplication();

  // Memoize default values to prevent unnecessary re-renders
  const defaultAnswers = useMemo(() => {
    if (!questions || questions.length === 0) return [];
    return questions.map(
      (q): SingleQuestionResponseDto => ({
        questionId: q.id,
        values: [''],
      }),
    );
  }, [questions]);

  // Custom validator to check if required questions are answered
  const validateRequiredQuestions = (values: { answers: SingleQuestionResponseDto[] }) => {
    const errors: Record<string, string> = {};

    if (!questions) return undefined;

    values.answers.forEach((answer, index) => {
      const question = questions[index];
      if (question?.required) {
        // Check if values array is empty or contains only empty strings
        const hasValidAnswer = answer.values.some((val) => val && val.trim().length > 0);
        if (!hasValidAnswer) {
          errors[`answers.${index}`] =
            `질문 ${question.questionNo}: "${question.title}" - 필수 질문입니다.`;
        }
      }
    });

    if (Object.keys(errors).length > 0) {
      return errors;
    }

    return undefined;
  };

  // Always call useForm hook - this must be called unconditionally
  const form = useForm({
    defaultValues: {
      answers: defaultAnswers,
    },
    onSubmit: ({ value }) => {
      createApplication(
        {
          formId: formId,
          responses: value.answers,
        },
        {
          onSuccess: () => {
            toast.success('지원이 완료되었습니다.', {
              richColors: true,
              position: 'top-center',
            });
            // TODO: redirect link 확인 필요
            router.push(ROUTES.MY.APPLICATIONS); // Redirect to project list or a confirmation page
          },
          onError: (err) => {
            toast.error('지원서 제출에 실패하였습니다.', {
              richColors: true,
              position: 'top-center',
              description: err.message,
            });
          },
        },
      );
    },
    validators: {
      onSubmit: ({ value }) => validateRequiredQuestions(value),
    },
  });

  // Reset form when questions are loaded
  useEffect(() => {
    if (questions && questions.length > 0 && defaultAnswers.length > 0) {
      form.reset();
    }
  }, [questions, defaultAnswers, form]);

  // Check loading and data state AFTER all hooks are called
  if (!user || isQuestionLoading) {
    return <DefaultSkeleton />;
  }

  // If questions is still undefined or empty after loading, show skeleton
  if (!questions || questions.length === 0) {
    return <DefaultSkeleton />;
  }

  return (
    <form
      onSubmit={async (e) => {
        e.preventDefault();
        e.stopPropagation();

        // Validate before submission
        await form.handleSubmit();

        // Show validation errors if any
        if (!form.state.isValid && form.state.errors) {
          const errorMessages = extractErrorMessages(form.state.errors);
          toast.error(<span className={'text-xl'}>필수 질문을 모두 작성해주세요.</span>, {
            richColors: true,
            position: 'top-center',
            description: (
              <div className="mt-2 space-y-1.5 text-sm">
                {errorMessages.map((msg, i) => (
                  <div key={i} className="flex items-start gap-2">
                    <span className="text-destructive mt-0.5">•</span>
                    <span className="flex-1">{msg}</span>
                  </div>
                ))}
              </div>
            ),
          });
        }
      }}
      className="mt-5 w-full max-w-4xl space-y-4"
    >
      {questions.map((question, index) => (
        <QuestionField key={question.id} tanstackForm={form} question={question} index={index} />
      ))}
      <div className="flex justify-end">
        <Button type="submit" disabled={isApplicationPending}>
          {isApplicationPending ? '지원서 제출 중...' : '지원서 제출하기'}
        </Button>
      </div>
    </form>
  );
};
