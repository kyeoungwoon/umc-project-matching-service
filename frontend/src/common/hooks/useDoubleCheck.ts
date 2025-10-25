'use client';

import { useRef, useState } from 'react';

import { DialogContents, DoubleCheckDialogProps } from '@common/components/DoubleCheckDialog';

export const useDoubleCheck = (content: DialogContents) => {
  const [isOpen, setIsOpen] = useState(false);
  const onConfirmRef = useRef<() => void>(() => {});

  const openAndSetOnConfirm = (onConfirm: () => void) => {
    setIsOpen(true);
    onConfirmRef.current = onConfirm;
  };

  const onClose = () => {
    setIsOpen(false);
  };

  const dialogProps: DoubleCheckDialogProps = {
    isOpen,
    onOpenChange: (isOpen?) => setIsOpen(isOpen ?? false),
    onConfirm: () => {
      onConfirmRef.current();
      onClose();
    },
    onClose,
    content,
  };

  return { onOpen: openAndSetOnConfirm, dialogProps };
};
