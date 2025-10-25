'use client';

import { useState } from 'react';

import { useParams, useRouter } from 'next/navigation';

import { toast } from 'sonner';

import { Button } from '@styles/components/ui/button';
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@styles/components/ui/card';
import { Checkbox } from '@styles/components/ui/checkbox';
import { Input } from '@styles/components/ui/input';
import { Label } from '@styles/components/ui/label';
import { Textarea } from '@styles/components/ui/textarea';

import { useCreateForm } from '@api/tanstack/forms.queries';
import { useGetProject } from '@api/tanstack/projects.queries';
import { ChallengerPart } from '@api/types/common';

import { ROUTES } from '@common/constants/routes.constants';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

import RequiredStar from '@features/projects/components/forms/RequiredStar';

const CreateProjectFormPage = () => {
  const params = useParams();
  const router = useRouter();
  const projectId = params.projectId as string;

  const { data: project, isLoading: isProjectLoading } = useGetProject(projectId);

  if (isProjectLoading || !project) {
    return <DefaultSkeleton />;
  }

  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [selectedPart, setSelectedPart] = useState<ChallengerPart[]>([]);

  const { mutate: createForm, isPending } = useCreateForm();

  const handleCreateForm = () => {
    if (!title || selectedPart.length === 0) {
      toast.error('필수 입력 필드를 모두 채워주세요.');
      return;
    }

    createForm(
      { projectId, title, description },
      {
        onSuccess: (form) => {
          if (form) {
            // Redirect to the projects editing page to add questions
            toast.success('지원용 폼이 생성되었습니다.', {
              description: '이동되는 페이지에서 질문을 추가해주세요.',
              position: 'top-center',
            });
            router.push(ROUTES.PROJECTS.EDIT_FORM(projectId, form.id));
          }
        },
        onError: () => {
          toast.error('지원서 생성에 실패했습니다.');
        },
      },
    );
  };

  return (
    <div className="flex w-full flex-col items-center justify-center p-4">
      <Card className="mt-5 w-full max-w-2xl p-5">
        <CardHeader>
          <CardTitle>프로젝트 지원용 폼 생성</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className={'flex flex-col gap-y-2'}>
            <Label className={'text-lg'}>
              폼 제목 (필수)
              <RequiredStar />
            </Label>
            <Input id="form-title" value={title} onChange={(e) => setTitle(e.target.value)} />
          </div>
          <div className={'flex flex-col gap-y-2'}>
            <Label className={'text-lg'}>폼 설명</Label>
            <Textarea
              id="form-description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>
          <div className={'flex flex-col gap-y-2'}>
            <Label className={'mb-3 text-lg'}>
              지원 가능 파트 선택 <RequiredStar />
            </Label>
            <div className="flex flex-col gap-y-2">
              {project.projectTos
                .map((tos) => tos.part)
                .map((part) => (
                  <div key={part} className="flex items-center space-x-2">
                    <Checkbox
                      id={part}
                      checked={selectedPart.includes(part)}
                      onCheckedChange={(checked) => {
                        let newValues = selectedPart || [];
                        if (checked) {
                          newValues = [...newValues, part];
                        } else {
                          newValues = newValues.filter((v) => v !== part);
                        }
                        setSelectedPart(newValues);
                      }}
                    />
                    <Label className={'text-base'} htmlFor={part}>
                      {part}
                    </Label>
                  </div>
                ))}
            </div>
          </div>
        </CardContent>
        <CardFooter className="flex justify-end">
          <Button onClick={handleCreateForm} disabled={isPending}>
            {isPending ? '생성 중...' : '폼 생성 및 질문 추가하러 가기'}
          </Button>
        </CardFooter>
      </Card>
    </div>
  );
};

export default CreateProjectFormPage;
