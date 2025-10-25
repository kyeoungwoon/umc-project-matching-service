'use client';

import { useState } from 'react';

import { useParams } from 'next/navigation';

import { SearchIcon } from 'lucide-react';

import { Card, CardContent, CardHeader, CardTitle } from '@styles/components/ui/card';
import { Input } from '@styles/components/ui/input';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@styles/components/ui/select';
import { Separator } from '@styles/components/ui/separator';

import { useGetApplicationsByForm } from '@api/tanstack/applications.queries';
import { useGetForm } from '@api/tanstack/forms.queries';
import { ApplicationStatus, ApplicationStatusEnum } from '@api/types/common';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

import ApplicantApplicationCard from '@features/projects/components/ApplicantApplicationCard';
import ApplicationCountByStatusCard from '@features/projects/components/ApplicationCountByStatusCard';

const ApplicantsPage = () => {
  const params = useParams();
  const projectId = params.projectId as string;
  const formId = params.formId as string;

  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<ApplicationStatus | 'ALL'>('ALL');

  const { data: form, isLoading: isFormLoading } = useGetForm(formId);
  const { data: formApplications, isLoading: isFormApplicationsLoading } =
    useGetApplicationsByForm(formId);

  if (isFormLoading || isFormApplicationsLoading) {
    return <DefaultSkeleton />;
  }

  if (!form || !formApplications) {
    return <div>폼 또는 지원서를 불러올 수 없습니다.</div>;
  }

  // 필터링된 지원서 목록
  const filteredApplications = formApplications?.filter((app) => {
    const matchesSearch =
      searchQuery === '' ||
      app.id.toLowerCase().includes(searchQuery.toLowerCase()) ||
      app.applicantId.toLowerCase().includes(searchQuery.toLowerCase()) ||
      app.applicantName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      app.applicantNickname.toLowerCase().includes(searchQuery.toLowerCase()) ||
      app.applicantSchoolName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      app.applicantPart.toLowerCase().includes(searchQuery.toLowerCase());

    const matchesStatus = statusFilter === 'ALL' || app.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  // 상태별 카운트
  const statusCounts = {
    PENDING: formApplications.filter((a) => a.status === ApplicationStatusEnum.PENDING).length ?? 0,
    CONFIRMED:
      formApplications.filter((a) => a.status === ApplicationStatusEnum.CONFIRMED).length ?? 0,
    REJECTED:
      formApplications.filter((a) => a.status === ApplicationStatusEnum.REJECTED).length ?? 0,
  };

  return (
    <div className="container mx-auto w-full max-w-7xl space-y-6 p-6">
      {/* 헤더 부분, 폼 제목과 설명 */}
      <div className="space-y-4">
        <div className="flex items-start justify-between">
          <div className="space-y-2">
            <div className="flex items-end gap-3 text-3xl">
              <span className={'tracking-tight text-gray-600'}>지원자 확인</span>
              <span>|</span>
              <h1 className="text-3xl font-bold tracking-tight">{form.title}</h1>
              {/*<p className="text-muted-foreground text-lg">{form.description}</p>*/}
            </div>
          </div>
        </div>
      </div>
      <Separator />

      {/* 지원 상태별 카드 */}
      <div className="grid gap-4 md:grid-cols-4">
        <ApplicationCountByStatusCard
          status={'ALL'}
          statusCount={statusCounts.PENDING + statusCounts.CONFIRMED + statusCounts.REJECTED}
        />
        <ApplicationCountByStatusCard
          status={ApplicationStatusEnum.PENDING}
          statusCount={statusCounts.PENDING}
        />
        <ApplicationCountByStatusCard
          status={ApplicationStatusEnum.CONFIRMED}
          statusCount={statusCounts.CONFIRMED}
        />
        <ApplicationCountByStatusCard
          status={ApplicationStatusEnum.REJECTED}
          statusCount={statusCounts.REJECTED}
        />
      </div>

      {/* Filters */}
      <Card>
        <CardHeader>
          <CardTitle>필터 및 검색</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex gap-4">
            <div className="relative flex-1">
              <SearchIcon className="text-muted-foreground absolute top-1/2 left-3 h-4 w-4 -translate-y-1/2" />
              <Input
                placeholder="지원자 이름, 학교, 파트로 검색"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-9"
              />
            </div>
            <Select
              value={statusFilter}
              onValueChange={(value) => setStatusFilter(value as ApplicationStatus | 'ALL')}
            >
              <SelectTrigger className="w-[200px]">
                <SelectValue placeholder="상태 필터" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">전체</SelectItem>
                <SelectItem value={ApplicationStatusEnum.PENDING}>제출됨</SelectItem>
                <SelectItem value={ApplicationStatusEnum.CONFIRMED}>합격</SelectItem>
                <SelectItem value={ApplicationStatusEnum.REJECTED}>불합격</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Applications List */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-xl font-semibold">지원서 목록 ({filteredApplications?.length}개)</h2>
        </div>

        {(filteredApplications?.length ?? 0) > 0 ? (
          <div className="flex w-full flex-col gap-y-4">
            {filteredApplications?.map(
              (application) =>
                application && (
                  <ApplicantApplicationCard key={application.id} application={application} />
                ),
            )}
          </div>
        ) : (
          <Card className="border-dashed">
            <CardContent className="flex flex-col items-center justify-center py-16">
              <SearchIcon className="text-muted-foreground mb-4 h-12 w-12" />
              <p className="text-muted-foreground text-lg font-medium">검색 결과가 없습니다</p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
};

export default ApplicantsPage;
