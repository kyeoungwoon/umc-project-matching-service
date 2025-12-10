'use client';

import { useState } from 'react';

import { toast } from 'sonner';

import { Button } from '@styles/components/ui/button';
import { Checkbox } from '@styles/components/ui/checkbox';
import { Combobox, ComboboxOption } from '@styles/components/ui/combobox';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@styles/components/ui/dialog';
import { Label } from '@styles/components/ui/label';

import { useAddProjectMember } from '@api/tanstack/admin.queries';
import { useGetProjects } from '@api/tanstack/projects.queries';
import type { Challenger } from '@api/types/challenger';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

interface AssignProjectDialogProps {
  challenger: Challenger | null;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export function AssignProjectDialog({ challenger, open, onOpenChange }: AssignProjectDialogProps) {
  const user = useGetUser();
  const chapterId = user?.info.chapterId;

  const [selectedProjectId, setSelectedProjectId] = useState<string | undefined>();
  const [isActive, setIsActive] = useState(true);

  const { data: projects, isLoading: isProjectsLoading } = useGetProjects(chapterId);
  const { mutate: addProjectMember, isPending } = useAddProjectMember();

  const projectOptions: ComboboxOption[] = projects
    ? projects.map((project) => ({ value: project.id, label: project.name }))
    : [];

  const handleAssign = () => {
    if (!challenger || !selectedProjectId) {
      toast.error('프로젝트를 선택해주세요.');
      return;
    }

    addProjectMember(
      {
        projectId: Number(selectedProjectId),
        challengerId: Number(challenger.id),
        active: isActive,
      },
      {
        onSuccess: () => {
          toast.success(`${challenger.name}님을 프로젝트에 배정했습니다.`);
          onOpenChange(false);
          setSelectedProjectId('');
          setIsActive(true);
        },
        onError: (err) => {
          toast.error('프로젝트 배정에 실패했습니다.', {
            description: err.message,
          });
        },
      },
    );
  };

  if (!challenger) {
    return null;
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>프로젝트 배정</DialogTitle>
          <DialogDescription>{challenger.name}님을 프로젝트 멤버로 배정합니다.</DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <div className="grid gap-2">
            <Label htmlFor="project">프로젝트</Label>
            {isProjectsLoading ? (
              <p className="text-muted-foreground text-sm">프로젝트 로딩 중...</p>
            ) : (
              <Combobox
                options={projectOptions}
                value={selectedProjectId}
                onChange={setSelectedProjectId}
                placeholder="프로젝트 선택"
                searchPlaceholder="프로젝트를 검색하세요..."
                emptyPlaceholder="프로젝트가 없습니다."
              />
            )}
          </div>
          <div className="flex items-center space-x-2">
            <Checkbox
              id="active"
              checked={isActive}
              onCheckedChange={(checked) => setIsActive(!!checked)}
            />
            <Label
              htmlFor="active"
              className="text-sm leading-none font-medium peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
            >
              활성 상태로 배정
            </Label>
          </div>
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={() => onOpenChange(false)} disabled={isPending}>
            취소
          </Button>
          <Button onClick={handleAssign} disabled={isPending || !selectedProjectId}>
            {isPending ? '배정 중...' : '배정하기'}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
