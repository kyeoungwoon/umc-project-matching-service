'use client';

import { useState } from 'react';

import Image from 'next/image';
import { useRouter } from 'next/navigation';

import { useForm } from '@tanstack/react-form';
import { Edit2, Loader2 } from 'lucide-react';
import { toast } from 'sonner';

import { Button } from '@styles/components/ui/button';
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@styles/components/ui/card';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@styles/components/ui/dialog';
import { Input } from '@styles/components/ui/input';
import { Label } from '@styles/components/ui/label';
import { Separator } from '@styles/components/ui/separator';
import { Textarea } from '@styles/components/ui/textarea';

import { useCreateProjectTo, useUpdateProjectTo } from '@api/tanstack/project-to.queries';
import { useUpdateProject } from '@api/tanstack/projects.queries';
import { type ChallengerPart, ChallengerPartEnum } from '@api/types/common';
import type { Project, ProjectUpdateRequest } from '@api/types/project';

import { ROUTES } from '@common/constants/routes.constants';

import { FileUploader } from '@common/components/FileUploader';
import ProjectInfoCard from '@common/components/ProjectInfoCard';

interface ProjectEditFormProps {
  project: Project;
}

export default function ProjectEditForm({ project }: ProjectEditFormProps) {
  const router = useRouter();
  const { mutate: updateProject, isPending } = useUpdateProject();
  const { mutate: updateProjectTo, isPending: isToUpdatePending } = useUpdateProjectTo();
  const { mutate: createProjectTo, isPending: isToCreatePending } = useCreateProjectTo();

  const [isPreviewOpen, setIsPreviewOpen] = useState(false);
  const [logoImageHover, setLogoImageHover] = useState(false);
  const [bannerImageHover, setBannerImageHover] = useState(false);

  // ProjectTo 상태 관리 (id를 키로 사용)
  const [toValues, setToValues] = useState<Record<string, number>>(
    project.projectTos.reduce(
      (acc, to) => {
        acc[to.id] = to.toCount;
        return acc;
      },
      {} as Record<string, number>,
    ),
  );

  // 새로 추가할 파트 상태 관리
  const [newParts, setNewParts] = useState<Record<ChallengerPart, number>>(
    {} as Record<ChallengerPart, number>,
  );

  // 현재 프로젝트에 없는 파트 목록
  const existingParts = project.projectTos.map((to) => to.part);
  const availableParts = Object.values(ChallengerPartEnum).filter(
    (part) => !existingParts.includes(part) && part !== 'NO_PART',
  );

  const form = useForm({
    defaultValues: {
      name: project.name,
      description: project.description,
      productOwnerId: project.productOwnerId,
      logoImageUrl: project.logoImageUrl,
      bannerImageUrl: project.bannerImageUrl,
      notionLink: project.notionLink,
    },
    onSubmit: async ({ value }) => {
      const request: ProjectUpdateRequest = {
        name: value.name,
        description: value.description,
        productOwnerId: value.productOwnerId,
        logoImageUrl: value.logoImageUrl,
        bannerImageUrl: value.bannerImageUrl,
        notionLink: value.notionLink,
      };

      // 프로젝트 정보 업데이트
      updateProject(
        { id: project.id, request },
        {
          onSuccess: async () => {
            try {
              // 1. 새로운 파트 생성
              const newPartCreations = Object.entries(newParts)
                .filter(([_, toCount]) => toCount > 0)
                .map(([part, toCount]) => ({
                  projectId: project.id,
                  part: part as ChallengerPart,
                  toCount,
                }));

              if (newPartCreations.length > 0) {
                await Promise.all(
                  newPartCreations.map(
                    (creation) =>
                      new Promise((resolve, reject) => {
                        createProjectTo(creation, {
                          onSuccess: resolve,
                          onError: reject,
                        });
                      }),
                  ),
                );
              }

              // 2. 기존 TO 변경사항 확인 및 업데이트
              const toUpdates = project.projectTos
                .filter((to) => toValues[to.id] !== to.toCount)
                .map((to) => ({
                  id: to.id,
                  request: { toCount: toValues[to.id] },
                }));

              if (toUpdates.length > 0) {
                await Promise.all(
                  toUpdates.map(
                    (update) =>
                      new Promise((resolve, reject) => {
                        updateProjectTo(update, {
                          onSuccess: resolve,
                          onError: reject,
                        });
                      }),
                  ),
                );
              }

              // 성공 메시지
              const hasChanges = newPartCreations.length > 0 || toUpdates.length > 0;
              if (hasChanges) {
                const messages: string[] = [];
                if (newPartCreations.length > 0) {
                  messages.push(`${newPartCreations.length}개 파트 추가`);
                }
                if (toUpdates.length > 0) {
                  messages.push(`${toUpdates.length}개 TO 수정`);
                }
                toast.success('프로젝트가 성공적으로 수정되었습니다.', {
                  description: messages.join(', '),
                });
              } else {
                toast.success('프로젝트가 성공적으로 수정되었습니다.');
              }

              router.push(ROUTES.ADMIN.PROJECTS);
            } catch (error) {
              toast.error('TO 수정 중 일부 오류가 발생했습니다.');
              // console.error(error);
            }
          },
          onError: (error) => {
            toast.error('프로젝트 수정에 실패했습니다.', {
              description: error.message,
            });
          },
        },
      );
    },
  });

  return (
    <>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          e.stopPropagation();
          form.handleSubmit();
        }}
      >
        <Card>
          <CardHeader>
            <CardTitle>프로젝트 정보</CardTitle>
          </CardHeader>
          <CardContent className="space-y-6">
            {/* 프로젝트 이름 */}
            <form.Field
              name="name"
              validators={{
                onChange: ({ value }) => {
                  if (!value || value.length === 0) {
                    return '프로젝트 이름을 입력해주세요';
                  }
                  return undefined;
                },
              }}
              children={(field) => (
                <div className="space-y-2">
                  <Label htmlFor="name">
                    프로젝트 이름 <span className="text-red-500">*</span>
                  </Label>
                  <Input
                    id="name"
                    value={field.state.value}
                    onBlur={field.handleBlur}
                    onChange={(e) => field.handleChange(e.target.value)}
                    placeholder="프로젝트 이름을 입력하세요"
                  />
                  {field.state.meta.errors && (
                    <p className="text-sm text-red-500">{field.state.meta.errors.join(', ')}</p>
                  )}
                </div>
              )}
            />

            <Separator />

            {/* 프로젝트 설명 */}
            <form.Field
              name="description"
              validators={{
                onChange: ({ value }) => {
                  if (!value || value.length === 0) {
                    return '프로젝트 설명을 입력해주세요';
                  }
                  return undefined;
                },
              }}
              children={(field) => (
                <div className="space-y-2">
                  <Label htmlFor="description">
                    프로젝트 설명 <span className="text-red-500">*</span>
                  </Label>
                  <Textarea
                    id="description"
                    value={field.state.value}
                    onBlur={field.handleBlur}
                    onChange={(e) => field.handleChange(e.target.value)}
                    placeholder="프로젝트 설명을 입력하세요 (줄바꿈 가능)"
                    rows={6}
                    className="whitespace-pre-line"
                  />
                  {field.state.meta.errors && (
                    <p className="text-sm text-red-500">{field.state.meta.errors.join(', ')}</p>
                  )}
                </div>
              )}
            />

            <Separator />

            {/* Notion 링크 */}
            <form.Field
              name="notionLink"
              validators={{
                onChange: ({ value }) => {
                  if (value && value.length !== 0) {
                    try {
                      new URL(value);
                      return undefined;
                    } catch {
                      return '올바른 URL을 입력해주세요';
                    }
                  }
                  return undefined;
                },
              }}
              children={(field) => (
                <div className="space-y-2">
                  <Label htmlFor="notionLink">
                    Notion 링크 <span className="text-red-500">*</span>
                  </Label>
                  <Input
                    id="notionLink"
                    value={field.state.value}
                    onBlur={field.handleBlur}
                    onChange={(e) => field.handleChange(e.target.value || '')}
                    placeholder="https://notion.so/..."
                    type="url"
                  />
                  {field.state.meta.errors && (
                    <p className="text-sm text-red-500">{field.state.meta.errors.join(', ')}</p>
                  )}
                </div>
              )}
            />

            <Separator />

            {/* 로고 이미지 */}
            <form.Field
              name="logoImageUrl"
              validators={{
                onChange: ({ value }) => {
                  if (value && value.length !== 0) {
                    try {
                      new URL(value);
                      return undefined;
                    } catch {
                      return '로고 이미지를 업로드해주세요.';
                    }
                  }
                  return undefined;
                },
              }}
              children={(field) => (
                <div className="space-y-2">
                  <Label htmlFor="logoImageUrl">
                    로고 이미지 <span className="text-red-500">*</span>
                  </Label>

                  {field.state.value && (
                    <div
                      className="relative h-32 w-32 overflow-hidden rounded-lg border"
                      onMouseEnter={() => setLogoImageHover(true)}
                      onMouseLeave={() => setLogoImageHover(false)}
                    >
                      <Image
                        src={field.state.value}
                        alt="로고 미리보기"
                        fill
                        className="object-cover"
                      />
                      {logoImageHover && (
                        <div className="bg-opacity-50 absolute inset-0 flex items-center justify-center bg-black">
                          <Edit2 className="h-8 w-8 text-white" />
                        </div>
                      )}
                    </div>
                  )}

                  <FileUploader
                    onUploadComplete={(url) => field.handleChange(url)}
                    accept="image/*"
                    buttonText={field.state.value ? '로고 이미지 변경' : '로고 이미지 업로드'}
                    showPreview={false}
                    value={field.state.value}
                  />

                  {field.state.meta.errors && (
                    <p className="text-sm text-red-500">{field.state.meta.errors.join(', ')}</p>
                  )}
                </div>
              )}
            />

            <Separator />

            {/* 배너 이미지 */}
            <form.Field
              name="bannerImageUrl"
              validators={{
                onChange: ({ value }) => {
                  if (value && value.length !== 0) {
                    try {
                      new URL(value);
                      return undefined;
                    } catch {
                      return '올바른 배너 이미지를 업로드해주세요.';
                    }
                  }
                  return undefined;
                },
              }}
              children={(field) => (
                <div className="space-y-2">
                  <Label htmlFor="bannerImageUrl">
                    배너 이미지 <span className="text-red-500">*</span>
                  </Label>

                  {field.state.value && (
                    <div
                      className="relative h-48 w-full overflow-hidden rounded-lg border"
                      onMouseEnter={() => setBannerImageHover(true)}
                      onMouseLeave={() => setBannerImageHover(false)}
                    >
                      <Image
                        src={field.state.value}
                        alt="배너 미리보기"
                        fill
                        className="object-cover"
                      />
                      {bannerImageHover && (
                        <div className="bg-opacity-50 absolute inset-0 flex items-center justify-center bg-black">
                          <Edit2 className="h-8 w-8 text-white" />
                        </div>
                      )}
                    </div>
                  )}

                  <FileUploader
                    onUploadComplete={(url) => field.handleChange(url)}
                    accept="image/*"
                    buttonText={field.state.value ? '배너 이미지 변경' : '배너 이미지 업로드'}
                    showPreview={false}
                    value={field.state.value}
                  />

                  {field.state.meta.errors && (
                    <p className="text-sm text-red-500">{field.state.meta.errors.join(', ')}</p>
                  )}
                </div>
              )}
            />

            <Separator />

            {/* Product Owner ID (읽기 전용) */}
            <div className="space-y-2">
              <Label>Product Owner</Label>
              <div className="bg-muted rounded-lg p-4">
                <p className="text-sm">
                  <span className="font-semibold">이름:</span> {project.productOwnerName}
                </p>
                <p className="text-sm">
                  <span className="font-semibold">닉네임:</span> {project.productOwnerNickname}
                </p>
                <p className="text-sm">
                  <span className="font-semibold">학교:</span> {project.productOwnerSchool}
                </p>
                <p className="text-muted-foreground mt-2 text-xs">
                  * Product Owner는 수정할 수 없습니다
                </p>
              </div>
            </div>

            <Separator />

            {/* 프로젝트 TO 수정 */}
            <div className="space-y-4">
              <div>
                <Label className="text-lg">파트별 모집 인원 (TO)</Label>
                <p className="text-muted-foreground mt-1 text-sm">
                  각 파트별 모집 인원을 설정할 수 있습니다.
                </p>
              </div>

              <div className="grid gap-4 sm:grid-cols-2">
                {project.projectTos
                  .sort((a, b) => {
                    // 파트 순서 정렬
                    const partOrder = Object.values(ChallengerPartEnum);
                    return partOrder.indexOf(a.part) - partOrder.indexOf(b.part);
                  })
                  .map((to) => {
                    const currentMemberCount = project.projectMembers.filter(
                      (member) => member.part === to.part && member.active,
                    ).length;

                    return (
                      <div key={to.id} className="space-y-2">
                        <div className="flex items-center justify-between">
                          <Label htmlFor={`to-${to.id}`} className="font-medium">
                            {to.part}
                          </Label>
                          <span className="text-muted-foreground text-xs">
                            현재 멤버: {currentMemberCount}명
                          </span>
                        </div>
                        <div className="flex items-center gap-2">
                          <Input
                            id={`to-${to.id}`}
                            type="number"
                            min="0"
                            max="99"
                            value={toValues[to.id] ?? to.toCount}
                            onChange={(e) => {
                              const value = parseInt(e.target.value, 10);
                              if (!isNaN(value) && value >= 0 && value <= 99) {
                                setToValues((prev) => ({
                                  ...prev,
                                  [to.id]: value,
                                }));
                              }
                            }}
                            className="w-24"
                          />
                          <span className="text-sm">명</span>
                          {toValues[to.id] !== to.toCount && (
                            <span className="text-primary text-xs font-medium">
                              (변경됨: {to.toCount} → {toValues[to.id]})
                            </span>
                          )}
                        </div>
                        {toValues[to.id] < currentMemberCount && (
                          <p className="text-destructive text-xs">
                            ⚠️ 현재 멤버 수보다 작게 설정할 수 없습니다
                          </p>
                        )}
                      </div>
                    );
                  })}
              </div>

              {/* 새 파트 추가 섹션 */}
              {availableParts.length > 0 && (
                <>
                  <Separator className="my-4" />
                  <div className="space-y-4">
                    <div>
                      <Label className="text-lg">새 파트 추가</Label>
                      <p className="text-muted-foreground mt-1 text-sm">
                        프로젝트에 새로운 파트를 추가할 수 있습니다.
                      </p>
                    </div>

                    <div className="grid gap-4 sm:grid-cols-2">
                      {availableParts.map((part) => (
                        <div key={part} className="space-y-2">
                          <div className="flex items-center justify-between">
                            <Label htmlFor={`new-${part}`} className="font-medium">
                              {part}
                            </Label>
                            <span className="text-muted-foreground text-xs">새 파트</span>
                          </div>
                          <div className="flex items-center gap-2">
                            <Input
                              id={`new-${part}`}
                              type="number"
                              min="0"
                              max="99"
                              value={newParts[part] ?? 0}
                              onChange={(e) => {
                                const value = parseInt(e.target.value, 10);
                                if (!isNaN(value) && value >= 0 && value <= 99) {
                                  setNewParts((prev) => ({
                                    ...prev,
                                    [part]: value,
                                  }));
                                }
                              }}
                              placeholder="0"
                              className="w-24"
                            />
                            <span className="text-sm">명</span>
                            {(newParts[part] ?? 0) > 0 && (
                              <span className="text-xs font-medium text-green-600">✓ 추가됨</span>
                            )}
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                </>
              )}
            </div>
          </CardContent>

          <CardFooter className="flex justify-between gap-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => router.back()}
              className="flex-1"
              disabled={isPending || isToUpdatePending || isToCreatePending}
            >
              취소
            </Button>
            <Button
              type="button"
              variant="secondary"
              onClick={() => setIsPreviewOpen(true)}
              className="flex-1"
              disabled={isPending || isToUpdatePending || isToCreatePending}
            >
              미리보기
            </Button>
            <Button
              type="submit"
              disabled={
                isPending ||
                isToUpdatePending ||
                isToCreatePending ||
                !form.state.isValid ||
                // TO가 현재 멤버 수보다 작은 경우 비활성화
                project.projectTos.some((to) => {
                  const currentMemberCount = project.projectMembers.filter(
                    (member) => member.part === to.part && member.active,
                  ).length;
                  return toValues[to.id] < currentMemberCount;
                })
              }
              className="flex-1"
            >
              {isPending || isToUpdatePending || isToCreatePending ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  {isPending
                    ? '프로젝트 수정 중...'
                    : isToCreatePending
                      ? '파트 추가 중...'
                      : 'TO 업데이트 중...'}
                </>
              ) : (
                '수정하기'
              )}
            </Button>
          </CardFooter>
        </Card>
      </form>

      {/* 미리보기 Dialog */}
      <Dialog open={isPreviewOpen} onOpenChange={setIsPreviewOpen}>
        <DialogContent className="max-w-4xl">
          <DialogHeader>
            <DialogTitle>프로젝트 카드 미리보기</DialogTitle>
          </DialogHeader>
          <div className="flex justify-center py-4">
            <ProjectInfoCard
              {...project}
              name={form.state.values.name}
              description={form.state.values.description}
              logoImageUrl={form.state.values.logoImageUrl}
              bannerImageUrl={form.state.values.bannerImageUrl}
              notionLink={form.state.values.notionLink}
            />
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}
