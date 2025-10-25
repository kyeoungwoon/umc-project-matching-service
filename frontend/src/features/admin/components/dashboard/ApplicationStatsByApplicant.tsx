'use client';

import { useMemo } from 'react';

// useMemo 임포트
import { ColumnDef } from '@tanstack/react-table';
import { clsx } from 'clsx';

import { useGetChapterApplicationSummary } from '@api/tanstack/applications.queries';
import type { ChallengerApplicationSummary } from '@api/types/application';
import { ApplicationStatus } from '@api/types/common';

import { parsePart } from '@common/utils/parse-userinfo';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

import { getStatusText } from '@features/projects/utils/get-by-application-status';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

import { DataTable } from '@features/admin/components/DataTable';

// 기본 고정 열
const baseColumns: ColumnDef<ChallengerApplicationSummary>[] = [
  {
    accessorKey: 'challengerId',
    header: '챌린저 ID',
  },
  {
    accessorKey: 'challengerSchoolName',
    header: '학교',
  },
  {
    accessorKey: 'challengerNickname',
    header: '닉네임',
  },
  {
    accessorKey: 'challengerName',
    header: '이름',
  },
  {
    accessorKey: 'challengerPart',
    header: '파트',
    accessorFn: (data) => parsePart(data.challengerPart),
  },
  {
    accessorKey: 'memberProjects.projectName',
    // chaining 표기로는 배열 접근 불가능하다는 점 알아두시길 ..
    // 여기는 그냥 단순 객체임, undefined 날 수는 있음

    header: '최종 프로젝트',
    // accessorFn: (data) => {
    //   // memberProjects는 배열이므로 첫 번째 항목의 projectName을 반환
    //   if (data.memberProjects && data.memberProjects.length > 0) {
    //     return data.memberProjects[0].projectName;
    //   }
    //   return '-'; // 프로젝트가 없을 때
    // },
  },
];

const ApplicationStatsByApplicant = () => {
  const user = useGetUser();
  const { data: chapterApplicationSummary, isLoading } = useGetChapterApplicationSummary(
    user?.info.chapterId || '',
  );

  // 고유한 학교 목록 추출
  const schoolOptions = useMemo(() => {
    if (!chapterApplicationSummary) return [];

    const uniqueSchools = Array.from(
      new Set(chapterApplicationSummary.map((item) => item.challengerSchoolName)),
    );

    return uniqueSchools.map((school) => ({
      label: school,
      value: school,
    }));
  }, [chapterApplicationSummary]);

  // 파트 옵션 (고정값)
  const partOptions = [
    { label: '기획', value: 'PLAN' },
    { label: '디자인', value: 'DESIGN' },
    { label: 'iOS', value: 'IOS' },
    { label: 'Android', value: 'ANDROID' },
    { label: 'Web', value: 'WEB' },
    { label: 'Node.js', value: 'NODE' },
    { label: 'Spring', value: 'SPRING' },
  ];

  // useMemo를 사용하여 data가 변경될 때만 columns를 새로 계산
  const columns = useMemo<ColumnDef<ChallengerApplicationSummary>[]>(() => {
    if (!chapterApplicationSummary) {
      return baseColumns; // 데이터가 없으면 기본 열만 반환
    }

    const uniqueMatchingRounds = chapterApplicationSummary?.[0].matchingRoundApplications.map(
      (mr) => ({
        matchingRoundId: mr.matchingRoundId,
        matchingRoundName: mr.matchingRoundName,
        matchingRoundStart: mr.startAt,
        matchingRoundEnd: mr.endAt,
      }),
    );

    // 2. 고유 매칭 회차 목록으로 동적 열 생성
    const dynamicColumns: ColumnDef<ChallengerApplicationSummary>[] = Array.from(
      uniqueMatchingRounds.values(),
    ).flatMap((matchingResult) => [
      {
        id: `matchingResults_${matchingResult.matchingRoundId}_project`,
        header: `${matchingResult.matchingRoundName}`,
        accessorFn: (row) => {
          const resultForRound = row.matchingRoundApplications.find(
            (r) => r.matchingRoundId === matchingResult.matchingRoundId,
          );
          return resultForRound ? resultForRound.application?.projectName : '-';
        },
      },
      {
        id: `matchingResults_${matchingResult.matchingRoundId}_status`,
        header: `결과`,

        // 1. accessorFn: 순수한 데이터(applicationStatus 문자열)만 반환
        accessorFn: (row) => {
          const resultForRound = row.matchingRoundApplications.find(
            (r) => r.matchingRoundId === matchingResult.matchingRoundId,
          );
          // 결과가 있으면 status 문자열을, 없으면 'N/A'를 반환
          return resultForRound ? resultForRound.application?.status : undefined;
        },

        // 2. cell: accessorFn이 반환한 값을 받아 JSX로 렌더링
        cell: ({ getValue }) => {
          // getValue()는 accessorFn이 반환한 값('CONFIRMED', 'REJECTED', 'N/A' 등)을 가져옵니다.
          const status = getValue<ApplicationStatus | undefined>();

          return (
            <span
              className={clsx(
                'text-center font-semibold',
                status === 'CONFIRMED'
                  ? 'text-green-700'
                  : status === 'REJECTED'
                    ? 'text-red-700'
                    : 'text-gray-700',
              )}
            >
              {/* getStatusText 함수에는 status 문자열이 전달됩니다. */}
              {getStatusText(status)}
            </span>
          );
        },
      },
    ]);

    // 3. 기본 열과 동적 열을 합쳐서 반환
    return [...baseColumns, ...dynamicColumns];
  }, [chapterApplicationSummary]);

  if (isLoading || !chapterApplicationSummary) {
    return <DefaultSkeleton />;
  }

  return (
    <DataTable
      columns={columns}
      data={chapterApplicationSummary}
      filterConfigs={[
        {
          columnId: 'challengerSchoolName',
          placeholder: '학교 선택',
          type: 'select',
          options: schoolOptions,
        },
        {
          columnId: 'challengerPart',
          placeholder: '파트 선택',
          type: 'select',
          options: partOptions,
        },
        {
          columnId: 'challengerNickname',
          placeholder: '닉네임으로 검색',
          type: 'text',
        },
      ]}
    />
  );
};

export default ApplicationStatsByApplicant;
