'use client';

import { useMemo, useState } from 'react';

import { toast } from 'sonner';

import { Button } from '@styles/components/ui/button';
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

import { useDeleteProjectMember } from '@api/tanstack/admin.queries';
import { useGetProjects } from '@api/tanstack/projects.queries';
import type { Challenger } from '@api/types/challenger';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

interface RemoveProjectDialogProps {
  challenger: Challenger | null;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export function RemoveProjectDialog({ challenger, open, onOpenChange }: RemoveProjectDialogProps) {
  const user = useGetUser();
  const chapterId = user?.info.chapterId;

  const [selectedMemberId, setSelectedMemberId] = useState<string>('');

  const { data: projects, isLoading: isProjectsLoading } = useGetProjects(chapterId);
  const { mutate: deleteProjectMember, isPending } = useDeleteProjectMember();

  // 챌린저가 속한 프로젝트 멤버 목록 추출
  const memberOptions: ComboboxOption[] = useMemo(() => {
    if (!projects || !challenger) return [];

    const members: ComboboxOption[] = [];
    projects.forEach((project) => {
      project.projectMembers.forEach((member) => {
        if (member.challengerId === challenger.id) {
          members.push({
            value: member.id,
            label: `${project.name} (${member.active ? '활성' : '비활성'})`,
          });
        }
      });
    });

    return members;
  }, [projects, challenger]);

  const handleRemove = () => {
    if (!challenger || !selectedMemberId) {
      toast.error('제거할 프로젝트 멤버를 선택해주세요.');
      return;
    }

    deleteProjectMember(Number(selectedMemberId), {
      onSuccess: () => {
        toast.success(`${challenger.name}님을 프로젝트에서 제거했습니다.`);
        onOpenChange(false);
        setSelectedMemberId('');
      },
      onError: (err) => {
        toast.error('프로젝트 멤버 제거에 실패했습니다.', {
          description: err.message,
        });
      },
    });
  };

  if (!challenger) {
    return null;
  }

  if (isProjectsLoading) {
    return (
      <Dialog open={open} onOpenChange={onOpenChange}>
        <DialogContent className="sm:max-w-[425px]">
          <DefaultSkeleton />
        </DialogContent>
      </Dialog>
    );
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>프로젝트에서 제거</DialogTitle>
          <DialogDescription>
            {challenger.name}님을 프로젝트 멤버에서 제거합니다.
          </DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <div className="grid gap-2">
            <Label htmlFor="member">제거할 프로젝트 멤버</Label>
            {memberOptions.length === 0 ? (
              <p className="text-muted-foreground text-sm">
                이 챌린저는 현재 어떤 프로젝트에도 속해있지 않습니다.
              </p>
            ) : (
              <Combobox
                options={memberOptions}
                value={selectedMemberId}
                onChange={setSelectedMemberId}
                placeholder="프로젝트 선택"
                searchPlaceholder="프로젝트를 검색하세요..."
                emptyPlaceholder="프로젝트가 없습니다."
              />
            )}
          </div>
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={() => onOpenChange(false)} disabled={isPending}>
            취소
          </Button>
          <Button
            variant="destructive"
            onClick={handleRemove}
            disabled={isPending || !selectedMemberId || memberOptions.length === 0}
          >
            {isPending ? '제거 중...' : '제거하기'}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
