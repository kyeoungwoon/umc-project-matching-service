'use client';

import { useParams, useRouter } from 'next/navigation';

import { toast } from 'sonner';

import { useGetForm } from '@api/tanstack/forms.queries';
import {
  useCreateQuestion,
  useCreateQuestionsBulk,
  useGetQuestions,
} from '@api/tanstack/questions.queries';
import { ApplicationFormQuestionBulkCreateRequest, QuestionItem } from '@api/types/question';

import { ROUTES } from '@common/constants/routes.constants';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

import { QuestionFormBuilder } from '@features/projects/components/QuestionFormBuilder';

const EditProjectFormPage = () => {
  const params = useParams();
  const router = useRouter();
  const projectId = params.projectId as string;
  const formId = params.formId as string;

  const { data: form, isLoading: isFormLoading } = useGetForm(formId);
  const { data: formQuestions, isLoading: isFormQuestionsLoading } = useGetQuestions(formId);
  const { mutate: addMultipleQuestions } = useCreateQuestionsBulk();

  // 자고 일어나면 질문 생성하는거부터 하시면 됩니다

  const handleFormSubmit = (questionsData: { questions: QuestionItem[] }) => {
    addMultipleQuestions(
      { formId, questions: questionsData.questions },
      {
        onSuccess: () => {
          toast.success('질문이 저장되었습니다.', { richColors: true });
          router.push(ROUTES.PROJECTS.FORM_LIST(projectId));
        },
        onError: (err) => {
          toast.error('질문 저장에 실패했습니다.', { richColors: true, description: err?.message });
        },
      },
    );
  };

  if (!form || !formQuestions || isFormLoading || isFormQuestionsLoading) {
    return <DefaultSkeleton />;
  }

  const questionNumberedQuestions = formQuestions.map((q, index) => ({
    ...q,
    questionNo: index + 1,
  }));

  return (
    <div className="flex w-full flex-col items-center justify-center p-4">
      <div className="mt-5 w-full max-w-4xl">
        <QuestionFormBuilder onSubmit={handleFormSubmit} initialData={questionNumberedQuestions} />
      </div>
    </div>
  );
};

export default EditProjectFormPage;
