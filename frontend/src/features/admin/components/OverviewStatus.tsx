'use client';

import { useMemo } from 'react';

import { BarChart, CheckCircle, Clock, Users, XCircle } from 'lucide-react';

import { Card, CardContent, CardHeader, CardTitle } from '@styles/components/ui/card';

import type { ProjectApplication } from '@api/types/application';
import { ApplicationStatusEnum } from '@api/types/common';

interface OverviewStatsProps {
  applications: ProjectApplication[];
}

export function OverviewStatus({ applications }: OverviewStatsProps) {
  const stats = useMemo(() => {
    const uniqueApplicants = new Set(applications.map((app) => app.applicantId)).size;
    const uniqueProjects = new Set(applications.map((app) => app.projectId)).size;
    const submittedCount = applications.filter(
      (app) => app.status === ApplicationStatusEnum.PENDING,
    ).length;
    const acceptedCount = applications.filter(
      (app) => app.status === ApplicationStatusEnum.CONFIRMED,
    ).length;
    const rejectedCount = applications.filter(
      (app) => app.status === ApplicationStatusEnum.REJECTED,
    ).length;

    return {
      totalApplications: applications.length,
      uniqueApplicants,
      uniqueProjects,
      submittedCount,
      acceptedCount,
      rejectedCount,
    };
  }, [applications]);

  return (
    <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
      <Card>
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-medium">총 지원서</CardTitle>
          <BarChart className="text-muted-foreground h-4 w-4" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-bold">{stats.totalApplications}</div>
          <p className="text-muted-foreground text-xs">전체 지원서 수</p>
        </CardContent>
      </Card>

      <Card>
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-medium">지원자 수</CardTitle>
          <Users className="text-muted-foreground h-4 w-4" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-bold">{stats.uniqueApplicants}</div>
          <p className="text-muted-foreground text-xs">고유 지원자 수</p>
        </CardContent>
      </Card>

      <Card>
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-medium">프로젝트 수</CardTitle>
          <BarChart className="text-muted-foreground h-4 w-4" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-bold">{stats.uniqueProjects}</div>
          <p className="text-muted-foreground text-xs">지원자를 받은 프로젝트 수</p>
        </CardContent>
      </Card>

      <Card>
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-medium">제출됨</CardTitle>
          <Clock className="text-muted-foreground h-4 w-4" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-bold">{stats.submittedCount}</div>
          <p className="text-muted-foreground text-xs">합격/불합격을 기다리고 있는 지원서</p>
        </CardContent>
      </Card>

      <Card>
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-medium">합격</CardTitle>
          <CheckCircle className="text-muted-foreground h-4 w-4" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-bold">{stats.acceptedCount}</div>
          <p className="text-muted-foreground text-xs">합격한 지원서 수</p>
        </CardContent>
      </Card>

      <Card>
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-medium">불합격</CardTitle>
          <XCircle className="text-muted-foreground h-4 w-4" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-bold">{stats.rejectedCount}</div>
          <p className="text-muted-foreground text-xs">불합격한 지원서 수</p>
        </CardContent>
      </Card>
    </div>
  );
}
