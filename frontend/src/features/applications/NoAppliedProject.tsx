'use client';

import Link from 'next/link';

import { InboxIcon } from 'lucide-react';

import { Button } from '@styles/components/ui/button';

import { ROUTES } from '@common/constants/routes.constants';

const NoAppliedProject = () => {
  return (
    <div className="flex flex-col items-center justify-center py-16">
      <InboxIcon className="text-muted-foreground mb-4 h-12 w-12" />
      <p className="mb-1 text-xl font-semibold text-black">아직 지원한 프로젝트가 없습니다</p>
      <p className="text-muted-foreground mb-10 text-base">프로젝트에 지원해보세요!</p>

      <Link href={ROUTES.PROJECTS.LIST}>
        <Button variant={'outline'}>프로젝트 목록 보러 가기</Button>
      </Link>
    </div>
  );
};

export default NoAppliedProject;
