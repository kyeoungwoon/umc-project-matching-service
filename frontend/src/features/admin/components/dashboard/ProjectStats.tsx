'use client';

import { useState } from 'react';

import { ColumnDef } from '@tanstack/react-table';

import {
  Item,
  ItemContent,
  ItemDescription,
  ItemHeader,
  ItemTitle,
} from '@styles/components/ui/item';
import { MultiSelectCombobox } from '@styles/components/ui/multi-select-combobox';

import { useGetApplications } from '@api/tanstack/applications.queries';
import { useGetProjectTos } from '@api/tanstack/project-to.queries';
import { useGetProject } from '@api/tanstack/projects.queries';
import { ChallengerPart } from '@api/types/common';

import { partOptionLabels } from '@common/constants/part-options.constants';

import {
  ProjectApplicationStatusByMatchingRound,
  calculateApplicationStatsByPartAndRound,
  filterStatsByParts,
} from '@common/utils/application-stats';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

import { DataTable } from '@features/admin/components/DataTable';

interface ProjectStatsProps {
  projectId: string;
}
// DataTable에 사용할 columns 정의
const columns: ColumnDef<ProjectApplicationStatusByMatchingRound>[] = [
  {
    accessorKey: 'part', // part 객체의 name 속성에 접근
    header: '파트',
    cell: ({ row }) => row.original.part,
  },
  {
    accessorKey: 'maxTo',
    header: 'TO',
    cell: ({ row }) => row.original.maxTo,
  },
  {
    accessorKey: 'matchingRoundName', // matchingRound 객체의 name 속성에 접근
    header: '매칭 차수',
    cell: ({ row }) => row.original.matchingRoundName,
  },
  {
    accessorKey: 'totalApplicants',
    header: '총 지원자',
    cell: ({ row }) => <span>{row.original.totalApplicants}</span>,
  },
  {
    accessorKey: 'confirmedApplicants',
    header: '합격',
    cell: ({ row }) => (
      <span className={'text-center font-bold text-green-700'}>
        {row.original.confirmedApplicants}
      </span>
    ),
  },
  {
    accessorKey: 'rejectedApplicants',
    header: '불합격',
    cell: ({ row }) => (
      <span className={'text-center font-bold text-red-700'}>
        {row.original.rejectedApplicants}
      </span>
    ),
  },
];

export function ProjectStats({ projectId }: ProjectStatsProps) {
  const [projectPartFilter, setProjectPartFilter] = useState<ChallengerPart[]>([]);

  const { data: projectApplications, isLoading: isProjectApplicationLoading } = useGetApplications(
    undefined,
    projectId,
  );
  const { data: project, isLoading: isProjectLoading } = useGetProject(projectId);
  const { data: projectTos, isLoading: isProjectTosLoading } = useGetProjectTos(projectId);

  if (isProjectApplicationLoading || isProjectTosLoading || isProjectLoading) {
    return (
      <Item>
        <ItemHeader className={'flex w-full flex-col items-start'}>
          <ItemTitle className={'text-xl'}>프로젝트별 지원 현황</ItemTitle>
          <ItemDescription className={'text-lg'}>데이터를 불러오는 중입니다...</ItemDescription>
        </ItemHeader>
        <ItemContent>
          <DefaultSkeleton />
        </ItemContent>
      </Item>
    );
  }

  if (!project || !projectApplications) {
    return (
      <Item>
        <ItemHeader className={'flex w-full flex-col items-start'}>
          <ItemTitle className={'text-xl'}>프로젝트별 지원 현황</ItemTitle>
          <ItemDescription className={'text-lg'}>
            해당 프로젝트에 대한 지원서가 없거나, 데이터를 불러오는 중 오류가 발생했습니다.
          </ItemDescription>
        </ItemHeader>
      </Item>
    );
  }

  // 통계 계산
  const allStats = calculateApplicationStatsByPartAndRound(projectApplications, projectTos);
  // 파트 필터링 적용
  const tableData = filterStatsByParts(allStats, projectPartFilter);

  return (
    <Item>
      <ItemHeader className={'flex w-full flex-row items-center justify-between gap-4'}>
        <div className={'flex flex-grow flex-col gap-1'}>
          <ItemTitle className={'text-xl font-semibold'}>{project?.name}</ItemTitle>{' '}
          {/* project.title 사용 */}
          <ItemDescription className={'text-base'}>
            각 파트별 지원자 수와 합격/불합격 현황
          </ItemDescription>
        </div>
        <div className={'min-w-50 flex-grow-0'}>
          <MultiSelectCombobox
            options={partOptionLabels}
            selectedValues={projectPartFilter}
            onChange={(part) => setProjectPartFilter(part as ChallengerPart[])}
            placeholder="파트 선택"
            searchPlaceholder="파트를 검색하세요..."
            emptyPlaceholder="해당 파트가 없습니다."
          />
        </div>
      </ItemHeader>
      <ItemContent>
        <DataTable columns={columns} data={tableData} />
      </ItemContent>
    </Item>
  );
}
