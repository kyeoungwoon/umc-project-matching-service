import { CheckCircle2Icon, FileTextIcon, XCircleIcon } from 'lucide-react';

import { ApplicationStatus, ApplicationStatusEnum } from '@api/types/common';

export const getStatusBadgeVariant = (status: ApplicationStatus) => {
  switch (status) {
    case ApplicationStatusEnum.PENDING:
      return 'outline';
    case ApplicationStatusEnum.CONFIRMED:
      return 'default';
    case ApplicationStatusEnum.REJECTED:
      return 'destructive';
    default:
      return 'secondary';
  }
};

export const getStatusText = (status?: ApplicationStatus) => {
  switch (status) {
    case ApplicationStatusEnum.PENDING:
      return '제출됨';
    case ApplicationStatusEnum.CONFIRMED:
      return '합격';
    case ApplicationStatusEnum.REJECTED:
      return '불합격';
    default:
      return '-';
  }
};

export const getStatusLabelClassname = (status: ApplicationStatus): string => {
  switch (status) {
    case ApplicationStatusEnum.CONFIRMED:
      return 'bg-green-600 text-white';
    case ApplicationStatusEnum.PENDING:
      return 'bg-gray-600 text-white';
    case ApplicationStatusEnum.REJECTED:
      return 'bg-red-600 text-white';
    default:
      return status;
  }
};

export const getStatusIcon = (status: ApplicationStatus) => {
  switch (status) {
    case ApplicationStatusEnum.CONFIRMED:
      return <CheckCircle2Icon className="h-4 w-4 text-green-600" />;
    case ApplicationStatusEnum.REJECTED:
      return <XCircleIcon className="h-4 w-4 text-red-600" />;
    case ApplicationStatusEnum.PENDING:
      return <FileTextIcon className="h-4 w-4 text-blue-600" />;
    default:
      return null;
  }
};
