'use client';

import { ColumnDef } from '@tanstack/react-table';
import { format } from 'date-fns';
import { MoreHorizontal } from 'lucide-react';

import { Badge } from '@styles/components/ui/badge';
import { Button } from '@styles/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSub,
  DropdownMenuSubContent,
  DropdownMenuSubTrigger,
  DropdownMenuTrigger,
} from '@styles/components/ui/dropdown-menu';
import { Label } from '@styles/components/ui/label';

import type { ProjectApplication } from '@api/types/application';
import { ApplicationStatus, ApplicationStatusEnum } from '@api/types/common';

import { parsePart } from '@common/utils/parse-userinfo';

import {
  getStatusLabelClassname,
  getStatusText,
} from '@features/projects/utils/get-by-application-status';

// 테이블 행들이 어떻게 이루어질 것인지를 정의한다.

// 테이블 행들이 어떻게 이루어질 것인지를 정의한다.
export const AdminInfoTableColumn = (
  onViewApplication: (applicationId: string) => void,
  onViewProject: (projectName: string) => void,
  onApplicationStatusChange: (applicationId: string, status: ApplicationStatus) => void,
  onApplicationDelete: (applicationId: string) => void,
): ColumnDef<ProjectApplication>[] => [
  {
    accessorKey: 'createdAt',
    header: '지원 시간',
    cell: ({ row }) => {
      const date = new Date(row.getValue('createdAt'));
      return <div className="whitespace-nowrap">{format(date, 'yyyy-MM-dd HH:mm')}</div>;
    },
  },
  {
    accessorKey: 'matchingRoundName',
    header: '지원 차수',
    cell: ({ row }) => {
      const matchingRoundName = row.original.matchingRoundName;
      return matchingRoundName ? (
        <Label>{matchingRoundName}</Label>
      ) : (
        <span className="text-muted-foreground text-sm">-</span>
      );
    },
  },
  {
    accessorKey: 'applicantSchoolName',
    header: '학교',
    cell: ({ row }) => {
      const applicantSchoolName = row.original.applicantSchoolName;

      // Handle case where school might not be an array or might be undefined
      if (!applicantSchoolName) {
        return <span className="text-muted-foreground text-sm">-</span>;
      }

      return <Label>{applicantSchoolName}</Label>;
    },
  },
  {
    accessorKey: 'applicantNickname',
    header: '닉네임',
    cell: ({ row }) => <div>{row.original.applicantNickname}</div>,
  },
  {
    accessorKey: 'applicantName',
    header: '이름',
    cell: ({ row }) => <div>{row.original.applicantName}</div>,
  },
  {
    accessorKey: 'applicantPart',
    header: '파트',
    cell: ({ row }) => {
      const part = row.original.applicantPart;
      return part ? (
        <Label>{parsePart(part)}</Label>
      ) : (
        <span className="text-muted-foreground text-sm">-</span>
      );
    },
  },
  {
    accessorKey: 'projectName',
    header: '지원한 프로젝트',
    cell: ({ row }) => {
      const projectName = row.original.projectName;
      return (
        <button
          onClick={() => onViewProject(row.original.projectId)}
          className="max-w-30 truncate text-left text-blue-600 hover:underline"
        >
          {projectName}
        </button>
      );
    },
  },
  {
    accessorKey: 'formTitle',
    header: '지원서 폼',
    cell: ({ row }) => (
      <button
        className="max-w-30 justify-start truncate hover:text-blue-600 hover:underline"
        onClick={() => onViewProject(row.original.projectId)}
      >
        {row.original.formTitle}
      </button>
    ),
  },
  {
    accessorKey: 'status',
    header: '상태',
    cell: ({ row }) => {
      const status = row.getValue('status') as ApplicationStatus;
      return <Badge className={getStatusLabelClassname(status)}>{getStatusText(status)}</Badge>;
    },
  },
  {
    id: 'actions',
    header: '작업',
    cell: ({ row }) => {
      const application = row.original;

      return (
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" className="h-8 w-8 p-0">
              <span className="sr-only">메뉴 열기</span>
              <MoreHorizontal className="h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            {/*<DropdownMenuLabel>작업</DropdownMenuLabel>*/}
            {/*지원서 상세보기 dialog open*/}
            <DropdownMenuItem onClick={() => onViewApplication(application.id)}>
              지원서 상세보기
            </DropdownMenuItem>
            {/*서브 메뉴 표출 - 각종 정보 복사*/}
            <DropdownMenuSub>
              <DropdownMenuSubTrigger>복사하기</DropdownMenuSubTrigger>
              <DropdownMenuSubContent>
                <DropdownMenuItem onClick={() => navigator.clipboard.writeText(application.id)}>
                  UPMS 챌린저 ID
                </DropdownMenuItem>
                <DropdownMenuItem onClick={() => navigator.clipboard.writeText(application.formId)}>
                  Form ID
                </DropdownMenuItem>
              </DropdownMenuSubContent>
            </DropdownMenuSub>
            {/*manual 지원서 합/불 처리*/}
            <DropdownMenuSub>
              <DropdownMenuSubTrigger>지원서 상태 변경</DropdownMenuSubTrigger>
              <DropdownMenuSubContent>
                <DropdownMenuItem
                  onClick={() =>
                    onApplicationStatusChange(application.id, ApplicationStatusEnum.PENDING)
                  }
                >
                  제출됨으로 변경
                </DropdownMenuItem>
                <DropdownMenuItem
                  onClick={() =>
                    onApplicationStatusChange(application.id, ApplicationStatusEnum.CONFIRMED)
                  }
                >
                  합격으로 변경
                </DropdownMenuItem>
                <DropdownMenuItem
                  onClick={() =>
                    onApplicationStatusChange(application.id, ApplicationStatusEnum.REJECTED)
                  }
                >
                  불합격으로 변경
                </DropdownMenuItem>
              </DropdownMenuSubContent>
            </DropdownMenuSub>
            <DropdownMenuItem onClick={() => onApplicationDelete(application.id)}>
              지원서 삭제
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      );
    },
  },
];
