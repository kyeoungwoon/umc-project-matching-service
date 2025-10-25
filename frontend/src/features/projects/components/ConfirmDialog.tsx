'use client';

import { Button } from '@styles/components/ui/button';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@styles/components/ui/dialog';

import { ApplicationStatus } from '@api/types/common';

const ConfirmDialog = ({
  isOpen,
  pendingAction,
  onChange,
  handleConfirm,
}: {
  isOpen: boolean;
  pendingAction: ApplicationStatus | null;
  onChange: (state: ApplicationStatus | null) => void;
  handleConfirm: () => void;
}) => {
  if (!pendingAction) return null;

  // 2. 확인 메시지 유틸
  const getConfirmMessage = (
    status: ApplicationStatus,
  ): {
    title: string;
    description: string;
    variant: 'default' | 'destructive' | 'outline';
  } => {
    // if (!status) throw new Error('pending 상태값이 null 입니다.');
    switch (status) {
      case 'CONFIRMED':
        return {
          title: '합격 처리',
          description: '이 지원자를 합격 처리하시겠습니까? 이 작업은 되돌릴 수 없습니다.',
          variant: 'outline',
        };
      case 'REJECTED':
        return {
          title: '불합격 처리',
          description: '이 지원자를 불합격 처리하시겠습니까?\n이 작업은 되돌릴 수 없습니다.',
          variant: 'destructive',
        };
      default:
        return {
          title: 'ERR: 올바르지 않은 상태',
          description: '무언가 잘못되었습니다. 관리자에게 문의해주세요.',
          variant: 'default',
        };
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={() => onChange(null)}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{getConfirmMessage(pendingAction).title}</DialogTitle>
          <DialogDescription>{getConfirmMessage(pendingAction).description}</DialogDescription>
        </DialogHeader>
        <DialogFooter>
          <Button onClick={() => onChange(null)}>취소</Button>
          <Button variant={getConfirmMessage(pendingAction).variant} onClick={handleConfirm}>
            확인
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default ConfirmDialog;
