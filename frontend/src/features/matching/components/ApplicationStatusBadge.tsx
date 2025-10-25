'use client';

import { clsx } from 'clsx';

import { ApplicationStatus, ApplicationStatusEnum } from '@api/types/common';

import { getStatusText } from '@features/projects/utils/get-by-application-status';

const ApplicationStatusBadge = ({ status }: { status: ApplicationStatus }) => {
  return (
    <div
      className={clsx(
        'w-16 rounded-md px-2 py-1 text-center text-lg font-semibold',
        status === ApplicationStatusEnum.REJECTED && 'bg-red-500 text-white',
        status === ApplicationStatusEnum.CONFIRMED && 'bg-green-500 text-white',
        status === ApplicationStatusEnum.PENDING && 'bg-blue-500 text-white',
      )}
    >
      {getStatusText(status)}
    </div>
  );
};

export default ApplicationStatusBadge;
