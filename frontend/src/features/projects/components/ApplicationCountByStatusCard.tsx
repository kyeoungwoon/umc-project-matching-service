'use client';

import { CheckCircle2Icon, ListIcon, SendIcon, XIcon } from 'lucide-react';

import { Card, CardContent, CardHeader, CardTitle } from '@styles/components/ui/card';

import { ApplicationStatus, ApplicationStatusEnum } from '@api/types/common';

const ApplicationCountByStatusCard = ({
  statusCount,
  status,
}: {
  statusCount: number;
  status: ApplicationStatus | 'ALL';
}) => {
  // Application에 따라서 Card title을 변경
  const getCardTitle = (status: ApplicationStatus | 'ALL') => {
    switch (status) {
      case ApplicationStatusEnum.PENDING:
        return '제출됨';
      case ApplicationStatusEnum.CONFIRMED:
        return '합격';
      case ApplicationStatusEnum.REJECTED:
        return '불합격';
      case 'ALL':
        return '전체';
      default:
        return '상태 없음';
    }
  };

  // Application Status에 따라서 색깔 변경
  const getStatusIcon = (status: ApplicationStatus | 'ALL') => {
    switch (status) {
      case ApplicationStatusEnum.PENDING:
        return <SendIcon className={'text-blue-600'} />;
      case ApplicationStatusEnum.CONFIRMED:
        return <CheckCircle2Icon className={'text-green-600'} />;
      case ApplicationStatusEnum.REJECTED:
        return <XIcon className={'text-red-600'} />;
      default:
        return <ListIcon className={'text-black-800'} />;
    }
  };

  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-2xl font-semibold">{getCardTitle(status)}</CardTitle>
        {getStatusIcon(status)}
      </CardHeader>
      <CardContent>
        <div className="text-2xl font-bold">{statusCount}</div>
      </CardContent>
    </Card>
  );
};

export default ApplicationCountByStatusCard;
