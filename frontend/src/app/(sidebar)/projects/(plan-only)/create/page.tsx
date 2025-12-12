'use client';

import { useState } from 'react';

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
import { Separator } from '@styles/components/ui/separator';
import { Textarea } from '@styles/components/ui/textarea';

import { useCreateProjectTosBulk } from '@api/tanstack/project-to.queries';
import { useCreateProject } from '@api/tanstack/projects.queries';
import { ChallengerPart, ChallengerPartEnum } from '@api/types/common';

import { partOptionLabels } from '@common/constants/part-options.constants';
import { ROUTES } from '@common/constants/routes.constants';

import { FileUploader } from '@common/components/FileUploader';
import ProjectInfoCard from '@common/components/ProjectInfoCard';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

import InputFormField from '@features/projects/components/forms/InputFormField';

const projectSchema = z.object({
  name: z.string().min(1, '프로젝트 이름을 입력해주세요.'),
  description: z.string().min(1, '프로젝트 설명을 입력해주세요.'),
  notionLink: z.url('올바른 URL을 입력해주세요.').or(z.literal('')),
  logoImageUrl: z.url('올바른 URL을 입력해주세요.').or(z.literal('')),
  bannerImageUrl: z.url('올바른 URL을 입력해주세요.').or(z.literal('')),
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
  const [isPreviewDialogOpen, setPreviewDialogOpen] = useState(false);

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
      // console.error(form.getAllErrors());
      toast.error('올바른 폼을 작성해주세요.');
    }
  };

  const handlePreviewClick = () => {
    setPreviewDialogOpen(true);
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
            <form.Field
              name={'notionLink'}
              children={(field: any) => {
                const isInvalid = field.state.meta.isTouched && !field.state.meta.isValid;
                return (
                  <Field>
                    <FieldLabel htmlFor={field.name}>
                      <div className={'flex flex-col gap-y-1'}>
                        <p>기획안 링크</p>
                        <p className={'text-muted-foreground whitespace-pre-line'}>
                          프로젝트에 대한 보다 자세한 기획안입니다.{'\n'}
                          기존에 사용중이시던 Notion 링크를 넣으시는 것을 추천드립니다.
                        </p>
                      </div>
                    </FieldLabel>
                    <Input
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
            <form.Field
              name={'logoImageUrl'}
              children={(field: any) => {
                const isInvalid = field.state.meta.isTouched && !field.state.meta.isValid;
                return (
                  <Field>
                    <FieldLabel htmlFor={field.name}>
                      <div className={'flex flex-col gap-y-1'}>
                        <p>로고 이미지</p>
                        <p className={'text-muted-foreground whitespace-pre-line'}>
                          프로젝트 카드 및 목록에 표시될 로고 이미지입니다.{'\n'}
                          가로 세로 비율이 1:1인 이미지를 권장합니다.
                        </p>
                      </div>
                    </FieldLabel>
                    <FileUploader
                      accept="image/*"
                      maxSize={5 * 1024 * 1024}
                      buttonText="로고 업로드"
                      showPreview={true}
                      value={field.state.value}
                      onUploadComplete={(url) => field.handleChange(url)}
                    />
                    {isInvalid && <FieldError errors={field.state.meta.errors} />}
                  </Field>
                );
              }}
            />
            <form.Field
              name={'bannerImageUrl'}
              children={(field: any) => {
                const isInvalid = field.state.meta.isTouched && !field.state.meta.isValid;
                return (
                  <Field>
                    <FieldLabel htmlFor={field.name}>
                      <div className={'flex flex-col gap-y-1'}>
                        <p>배너 이미지</p>
                        <p className={'text-muted-foreground whitespace-pre-line'}>
                          프로젝트 카드 및 목록에 표시될 배너 이미지입니다.{'\n'}
                          가로 세로 비율이 4:1인 이미지를 권장합니다. (800*200, 1800*300 두 사이즈로
                          표시됩니다)
                        </p>
                      </div>
                    </FieldLabel>
                    <FileUploader
                      accept="image/*"
                      maxSize={10 * 1024 * 1024}
                      buttonText="배너 업로드"
                      showPreview={true}
                      value={field.state.value}
                      onUploadComplete={(url) => field.handleChange(url)}
                    />
                    {isInvalid && <FieldError errors={field.state.meta.errors} />}
                  </Field>
                );
              }}
            />
            <FieldGroup>
              <FieldLabel>
                <div className={'flex flex-col gap-y-1'}>
                  <p>모집 파트 및 인원</p>
                  <p className={'text-muted-foreground whitespace-pre-line'}>
                    Plan은 PO를 제외하고 추가로 있다면 (Plan이 1명 이상이라면) 넣어주세요.{'\n'}
                    나머지 파트는 각 지부 내에서 본인에게 배정된 TO를 알맞게 입력 부탁드립니다.
                  </p>
                </div>
              </FieldLabel>
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
          <CardFooter className="flex justify-end gap-2">
            <Button type="button" variant="outline" onClick={handlePreviewClick}>
              카드 미리보기
            </Button>
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
      {/*미리보기 Dialog*/}
      <Dialog open={isPreviewDialogOpen} onOpenChange={setPreviewDialogOpen}>
        <DialogContent className="max-h-[90vh] max-w-4xl overflow-y-auto">
          <DialogHeader>
            <DialogTitle>프로젝트 미리보기</DialogTitle>
            <DialogDescription>
              입력한 정보를 바탕으로 프로젝트 카드가 어떻게 보일지 미리 확인할 수 있습니다.
            </DialogDescription>
          </DialogHeader>
          <Separator />
          <div className="mt-4 flex w-full items-center justify-center">
            <ProjectInfoCard
              id="preview"
              name={form.state.values.name || '프로젝트 이름'}
              description={form.state.values.description || '프로젝트 설명'}
              productOwnerNickname={user?.info.nickname || ''}
              productOwnerSchool={user?.info.schoolName || ''}
              productOwnerName={user?.info.name || ''}
              productOwnerId={user?.info.id || ''}
              chapterName={user?.info.chapterName || ''}
              chapterId={user?.info.chapterId || ''}
              logoImageUrl={form.state.values.logoImageUrl}
              bannerImageUrl={form.state.values.bannerImageUrl}
              notionLink={form.state.values.notionLink}
              projectTos={form.state.values.toItems.map((item, index) => ({
                id: `preview-${index}`,
                projectId: 'preview',
                part: item.part,
                toCount: item.toCount,
                createdAt: new Date().toISOString(),
                updatedAt: new Date().toISOString(),
              }))}
              projectMembers={[]}
              createdAt={new Date().toISOString()}
              updatedAt={new Date().toISOString()}
            />
          </div>
          <DialogFooter>
            <DialogClose asChild>
              <Button variant="default">닫기</Button>
            </DialogClose>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default CreateProjectPage;
