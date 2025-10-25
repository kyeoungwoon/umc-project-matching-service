'use client';

import { useState } from 'react';

import { router } from 'next/client';
import { useRouter } from 'next/navigation';

import { useForm } from '@tanstack/react-form';
import { clsx } from 'clsx';
import { toast } from 'sonner';
import { z } from 'zod';

import { Button } from '@styles/components/ui/button';
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@styles/components/ui/card';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@styles/components/ui/dialog';
import { Field, FieldError, FieldGroup, FieldLabel } from '@styles/components/ui/field';
import { Input } from '@styles/components/ui/input';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@styles/components/ui/select';
import { Textarea } from '@styles/components/ui/textarea';

import { useCreateProjectTosBulk } from '@api/tanstack/project-to.queries';
import { useCreateProject } from '@api/tanstack/projects.queries';
import { ChallengerPart, ChallengerPartEnum } from '@api/types/common';

import { partOptionLabels } from '@common/constants/part-options.constants';
import { ROUTES } from '@common/constants/routes.constants';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

import InputFormField from '@features/projects/components/forms/InputFormField';

const projectSchema = z.object({
  name: z.string().min(1, '프로젝트 이름을 입력해주세요.'),
  description: z.string().min(1, '프로젝트 설명을 입력해주세요.'),
  notionLink: z.url('올바른 URL을 입력해주세요.'),
  logoImageUrl: z.url('올바른 URL을 입력해주세요.'),
  bannerImageUrl: z.url('올바른 URL을 입력해주세요.'),
  toItems: z
    .array(
      z.object({
        part: z.enum(ChallengerPartEnum),
        toCount: z.number().min(1, '인원은 1명 이상이어야 합니다.'),
      }),
    )
    .min(1, '모집 파트를 최소 1개 이상 추가해주세요.'),
});

const CreateProjectPage = () => {
  const user = useGetUser();
  const router = useRouter();

  const { mutate: createProject, isPending } = useCreateProject();
  const { mutate: createProjectToBulk } = useCreateProjectTosBulk();

  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogTitle, setDialogTitle] = useState('');
  const [dialogDescription, setDialogDescription] = useState('');
  const [isConfirmDialogOpen, setConfirmDialogOpen] = useState(false);

  const form = useForm({
    defaultValues: {
      name: '',
      description: '',
      notionLink: '',
      logoImageUrl: '',
      bannerImageUrl: '',
      toItems: [] as Array<{ part: ChallengerPart; toCount: number }>,
    },
    onSubmit: async ({ value }) => {
      if (!user) throw new Error('로그인되지 않은 사용자 입니다.');
      createProject(
        {
          name: value.name,
          description: value.description,
          notionLink: value.notionLink,
          logoImageUrl: value.logoImageUrl,
          bannerImageUrl: value.bannerImageUrl,
          productOwnerId: user.info.id,
          chapterId: user.info.chapterId,
        },
        {
          onSuccess: (data) => {
            createProjectToBulk({
              projectId: data.id,
              toItems: value.toItems,
            });
            setConfirmDialogOpen(false);
            setDialogTitle('성공');
            setDialogDescription('프로젝트가 생성되었습니다.');
            setDialogOpen(true);
            form.reset();
            router.push(ROUTES.PROJECTS.FORM_LIST(data.id));
          },
          onError: () => {
            setConfirmDialogOpen(false);
            setDialogTitle('오류');
            setDialogDescription('프로젝트 생성에 실패했습니다.');
            setDialogOpen(true);
          },
        },
      );
    },
    validators: {
      onSubmit: projectSchema,
      onChange: projectSchema,
      onBlur: projectSchema,
    },
  });

  const handleCreateClick = () => {
    form.validate('submit');
    if (form.state.isValid) {
      setConfirmDialogOpen(true);
    } else {
      console.error(form.getAllErrors());
      toast.error('올바른 폼을 작성해주세요.');
    }
  };

  return (
    <div className="flex w-full flex-col items-center justify-center">
      <form
        onSubmit={(e) => {
          e.preventDefault();
          e.stopPropagation();
        }}
      >
        <Card className={'min-w-700pxr max-w-1000pxr mt-5 flex flex-col justify-center p-5'}>
          <CardHeader>
            <CardTitle className="font-medium">프로젝트 정보</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <InputFormField tanstackForm={form} name="name" label="프로젝트 이름" />
            <form.Field
              name={'description'}
              children={(field: any) => {
                const isInvalid = field.state.meta.isTouched && !field.state.meta.isValid;
                return (
                  <Field>
                    <FieldLabel htmlFor={field.name}>{'프로젝트 설명'}</FieldLabel>
                    <Textarea
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                      className={clsx(isInvalid && 'border-red-500')}
                    />
                    {isInvalid && <FieldError errors={field.state.meta.errors} />}
                  </Field>
                );
              }}
            />
            <InputFormField
              tanstackForm={form}
              name={'notionLink'}
              label="기획안 링크 (Notion 등)"
            />
            <InputFormField tanstackForm={form} name={'logoImageUrl'} label="로고 이미지 링크" />
            <InputFormField tanstackForm={form} name={'bannerImageUrl'} label="배너 이미지 링크" />
            <FieldGroup>
              <FieldLabel>모집 파트 및 인원</FieldLabel>
              <form.Field
                name="toItems"
                mode="array"
                children={(field) => (
                  <div className="space-y-2">
                    {field.state.value.map((_, i) => (
                      <div key={i} className="flex items-center gap-2">
                        <form.Field
                          name={`toItems[${i}].part`}
                          children={(subField) => (
                            <Select
                              value={subField.state.value}
                              onValueChange={(value) =>
                                subField.handleChange(value as ChallengerPart)
                              }
                            >
                              <SelectTrigger className="w-full">
                                <SelectValue placeholder="선택하세요" />
                              </SelectTrigger>
                              <SelectContent>
                                {partOptionLabels.map((p) => (
                                  <SelectItem key={p.value} value={p.value}>
                                    {p.label}
                                  </SelectItem>
                                ))}
                              </SelectContent>
                            </Select>
                          )}
                        />
                        <form.Field
                          name={`toItems[${i}].toCount`}
                          children={(subField) => (
                            <Input
                              type="number"
                              min={1}
                              value={subField.state.value}
                              onBlur={subField.handleBlur}
                              onChange={(e) => subField.handleChange(parseInt(e.target.value, 10))}
                            />
                          )}
                        />
                        <Button
                          type="button"
                          variant="destructive"
                          size="sm"
                          onClick={() => field.removeValue(i)}
                        >
                          삭제
                        </Button>
                      </div>
                    ))}
                    <Button
                      type="button"
                      variant="secondary"
                      onClick={() => field.pushValue({ part: ChallengerPartEnum.WEB, toCount: 1 })}
                    >
                      파트 추가
                    </Button>
                    <FieldError errors={field.state.meta.errors} />
                  </div>
                )}
              />
            </FieldGroup>
          </CardContent>
          <CardFooter className="flex justify-end">
            <Button type="button" onClick={handleCreateClick} disabled={isPending}>
              {isPending ? '생성 중...' : '프로젝트 생성'}
            </Button>
          </CardFooter>
        </Card>
      </form>
      {/*프로젝트 생성할 것을 confirm하는 dialog*/}
      <Dialog open={isConfirmDialogOpen} onOpenChange={setConfirmDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>프로젝트 생성</DialogTitle>
            <DialogDescription>프로젝트를 생성하시겠습니까?</DialogDescription>
          </DialogHeader>
          <DialogFooter>
            <DialogClose asChild>
              <Button variant="secondary">취소</Button>
            </DialogClose>
            <Button onClick={() => form.handleSubmit()} disabled={isPending}>
              생성
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
      {/*프로젝트 생성되었을 때 알림*/}
      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>{dialogTitle}</DialogTitle>
            <DialogDescription>{dialogDescription}</DialogDescription>
          </DialogHeader>
          <DialogFooter>
            <DialogClose asChild>
              <Button>확인</Button>
            </DialogClose>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default CreateProjectPage;
