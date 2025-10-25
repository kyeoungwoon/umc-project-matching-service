import { useState } from 'react';

import { useRouter } from 'next/navigation';

import clsx from 'clsx';
import { toast } from 'sonner';

import { Button } from '@styles/components/ui/button';
import { Input } from '@styles/components/ui/input';
import { Textarea } from '@styles/components/ui/textarea';

import { useDeleteForm, useUpdateForm } from '@api/tanstack/forms.queries';
import { ChapterAdminRoleEnum } from '@api/types/common';
import { ProjectApplicationForm, ProjectApplicationFormUpdateRequest } from '@api/types/form';

import { ROUTES } from '@common/constants/routes.constants';

import {
  useGetChallengerChapterRoles,
  useHasChallengerChapterRole,
  useIsPlanChallenger,
} from '@common/hooks/check-challenger-permissions';
import { useDoubleCheck } from '@common/hooks/useDoubleCheck';

import DefaultSkeleton from '@common/components/DefaultSkeleton';
import DoubleCheckDialog from '@common/components/DoubleCheckDialog';

interface FormCardProps {
  form: ProjectApplicationForm;
  isProductOwner: boolean;
}

enum FormCardMode {
  EDIT = 'edit',
  NORMAL = 'normal',
}

export const FormInfoCard = ({ form, isProductOwner }: FormCardProps) => {
  const { id: formId, projectId } = form;

  const { mutate: editForm } = useUpdateForm();
  const { mutate: deleteForm } = useDeleteForm();

  const { onOpen, dialogProps } = useDoubleCheck({
    title: '폼 삭제하기',
    variant: 'destructive',
    cancelText: '취소',
    confirmText: '삭제하기',
    description: '폼을 삭제하면 해당 폼에 대한 모든 정보가 사라집니다. 정말 삭제하시겠습니까?',
  });

  // TODO: 각각 가져오도록 변경

  const router = useRouter();

  // 권한 관리
  const isSchoolLead = useHasChallengerChapterRole(ChapterAdminRoleEnum.SCHOOL_LEAD);
  const hasPermissions = isProductOwner || isSchoolLead;

  const [mode, setMode] = useState<FormCardMode>(FormCardMode.NORMAL);
  const [editedForm, setEditedForm] = useState<ProjectApplicationFormUpdateRequest>({
    title: form.title,
    description: form.description || '',
  });

  const handleManageForm = () => {
    router.push(ROUTES.PROJECTS.EDIT_FORM(projectId, formId));
  };
  const handleApplyToForm = () => {
    router.push(ROUTES.PROJECTS.APPLY_TO_FORM(projectId, formId));
  };

  const handleToggleMode = () => {
    setMode((prevMode) => {
      if (prevMode === FormCardMode.EDIT) {
        // Save changes
        editForm(
          {
            id: formId,
            request: editedForm,
          },
          {
            onSuccess: () => {
              toast.success('폼 제목과 설명을 수정했습니다.');
            },
            onError: (err) => {
              toast.error('Form 정보 수정에 실패하였습니다.', {
                description: err.message,
              });
            },
          },
        );
      }

      return prevMode === FormCardMode.NORMAL ? FormCardMode.EDIT : FormCardMode.NORMAL;
    });
  };
  const handleViewApplicants = () => {
    router.push(ROUTES.PROJECTS.VIEW_APPLICANTS(projectId, formId));
  };

  const handleDeleteForm = () => {
    onOpen(() =>
      deleteForm(formId, {
        onSuccess: () => {
          toast.success('폼을 삭제하였습니다.');
        },
        onError: (err) => {
          toast.error('폼 삭제에 실패하였습니다.', {
            description: err.message,
          });
        },
      }),
    );
  };

  return (
    <>
      <DoubleCheckDialog {...dialogProps} />
      <div className={'flex flex-col gap-y-4 rounded-lg border border-gray-200 px-8 py-6'}>
        <div>
          {mode === FormCardMode.NORMAL ? (
            <>
              <div className={'flex flex-col gap-y-5'}>
                <p className="text-4xl font-bold">{form.title}</p>
                <p
                  className={clsx(
                    'mb-2 text-xl whitespace-pre-line text-gray-900',
                    !form.description && 'text-gray-500 italic',
                  )}
                >
                  {form.description || '설명 없음'}
                </p>
              </div>
            </>
          ) : (
            <div className={'flex flex-col gap-y-3'}>
              <span>폼 제목</span>
              <Input
                value={editedForm.title}
                onChange={(e) => setEditedForm({ ...editedForm, title: e.target.value })}
              />
              <span>폼 설명</span>
              <Textarea
                value={editedForm.description}
                onChange={(e) => setEditedForm({ ...editedForm, description: e.target.value })}
              />
            </div>
          )}
        </div>
        <div className="mt-4 flex w-full flex-col justify-between">
          {/*Plan이거나 관리자일 땐 질문 수정 및 지원자 보기 버튼 활성화 (관리자는 아직 TODO)*/}
          {hasPermissions && (
            <>
              <div className={'flex w-full flex-row gap-x-4'}>
                {/*{isProductOwner && (*/}
                {/*  <>*/}
                <Button
                  className={clsx(
                    'h-14 flex-1 py-4 text-2xl',
                    mode === FormCardMode.EDIT && 'bg-green-600 text-white',
                  )}
                  onClick={handleToggleMode}
                  variant="outline"
                >
                  {mode === FormCardMode.NORMAL ? '폼 정보 수정' : '저장하기'}
                </Button>
                <Button
                  className={'h-14 flex-1 py-4 text-2xl'}
                  onClick={handleManageForm}
                  variant="outline"
                >
                  {isSchoolLead ? '[회장 직권] 질문 수정하기' : '질문 수정하기'}
                </Button>
                {/*</>*/}
                {/*)}*/}
              </div>
              <div className={'flex w-full flex-row gap-x-4'}>
                <Button
                  className={'mt-4 h-14 flex-1 py-4 text-2xl'}
                  onClick={handleDeleteForm}
                  variant="destructive"
                >
                  {isSchoolLead ? '[회장 직권] 폼 삭제' : '폼 삭제하기'}
                </Button>
                <Button
                  className={'mt-4 h-14 flex-1 py-4 text-2xl'}
                  onClick={handleViewApplicants}
                  variant="default"
                >
                  {isSchoolLead ? '[회장 직권] 지원자 조회' : '지원자 보기'}
                </Button>
              </div>
            </>
          )}
          {/*일반 사용자는 무조건 지원하기만 가능*/}
          {!isProductOwner && (
            <Button
              onClick={handleApplyToForm}
              className={'mt-4 h-14 w-full py-4 text-2xl'}
              variant="default"
            >
              지원하기
            </Button>
          )}
        </div>
      </div>
    </>
  );
};
