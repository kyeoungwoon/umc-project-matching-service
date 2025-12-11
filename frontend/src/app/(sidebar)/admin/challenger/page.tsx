'use client';

import { useState } from 'react';

import { ColumnDef } from '@tanstack/react-table';
import { MoreHorizontal, Search } from 'lucide-react';

import { Button } from '@styles/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@styles/components/ui/card';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@styles/components/ui/dropdown-menu';
import { Input } from '@styles/components/ui/input';

import { useSearchChallengerByName } from '@api/tanstack/admin.queries';
import type { Challenger } from '@api/types/challenger';

import { partOptionLabels } from '@common/constants/part-options.constants';

import DefaultSkeleton from '@common/components/DefaultSkeleton';
import UpmsHeader from '@common/components/upms/UpmsHeader';

import { DataTable } from '@features/admin/components/DataTable';
import { AssignProjectDialog } from '@features/admin/components/challenger/AssignProjectDialog';
import { RemoveProjectDialog } from '@features/admin/components/challenger/RemoveProjectDialog';

const AdminChallengerSearchPage = () => {
  const [searchName, setSearchName] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedChallenger, setSelectedChallenger] = useState<Challenger | null>(null);
  const [isAssignDialogOpen, setIsAssignDialogOpen] = useState(false);
  const [isRemoveDialogOpen, setIsRemoveDialogOpen] = useState(false);

  const { data: challengers, isLoading } = useSearchChallengerByName(
    searchQuery,
    searchQuery.length > 0,
  );

  const handleAssignProject = (challenger: Challenger) => {
    setSelectedChallenger(challenger);
    setIsAssignDialogOpen(true);
  };

  const handleRemoveProject = (challenger: Challenger) => {
    setSelectedChallenger(challenger);
    setIsRemoveDialogOpen(true);
  };

  const handleSearch = () => {
    setSearchQuery(searchName);
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  const columns: ColumnDef<Challenger>[] = [
    {
      accessorKey: 'id',
      header: 'ID',
      cell: ({ row }) => <div className="font-medium">{row.getValue('id')}</div>,
    },
    {
      accessorKey: 'name',
      header: '이름',
      cell: ({ row }) => <div>{row.getValue('name')}</div>,
    },
    {
      accessorKey: 'nickname',
      header: '닉네임',
      cell: ({ row }) => <div>{row.getValue('nickname')}</div>,
    },
    {
      accessorKey: 'part',
      header: '파트',
      cell: ({ row }) => {
        const part = row.getValue('part') as string;
        const partLabel = partOptionLabels.find((option) => option.value === part)?.label || part;
        return <div>{partLabel}</div>;
      },
    },
    {
      accessorKey: 'schoolName',
      header: '학교',
      cell: ({ row }) => <div>{row.getValue('schoolName')}</div>,
    },
    {
      accessorKey: 'chapterName',
      header: '챕터',
      cell: ({ row }) => <div>{row.getValue('chapterName')}</div>,
    },
    {
      accessorKey: 'gisu',
      header: '기수',
      cell: ({ row }) => <div>{row.getValue('gisu')}기</div>,
    },
    {
      accessorKey: 'studentId',
      header: '학번',
      cell: ({ row }) => <div>{row.getValue('studentId')}</div>,
    },
    {
      id: 'actions',
      header: '작업',
      cell: ({ row }) => {
        const challenger = row.original;

        return (
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" className="h-8 w-8 p-0">
                <span className="sr-only">메뉴 열기</span>
                <MoreHorizontal className="h-4 w-4" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuItem onClick={() => handleAssignProject(challenger)}>
                프로젝트에 배정하기
              </DropdownMenuItem>
              <DropdownMenuItem onClick={() => handleRemoveProject(challenger)}>
                프로젝트에서 제거하기
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        );
      },
    },
  ];

  const section = {
    title: '챌린저 검색',
    description: '이름으로 챌린저를 검색하여 정보를 조회할 수 있습니다.',
  };

  return (
    <>
      <div className="container space-y-6 p-6">
        <UpmsHeader section={section} />

        <Card>
          <CardHeader>
            <CardTitle>챌린저 검색</CardTitle>
            <CardDescription>검색할 챌린저의 이름을 입력하세요.</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="flex gap-2">
              <Input
                placeholder="이름을 입력하세요..."
                value={searchName}
                onChange={(e) => setSearchName(e.target.value)}
                onKeyDown={handleKeyDown}
                className="max-w-sm"
              />
              <Button onClick={handleSearch} disabled={searchName.length === 0}>
                <Search className="mr-2 h-4 w-4" />
                검색
              </Button>
            </div>
          </CardContent>
        </Card>

        {isLoading && <DefaultSkeleton />}

        {!isLoading && searchQuery && challengers && challengers.length > 0 && (
          <Card>
            <CardHeader>
              <CardTitle>검색 결과</CardTitle>
              <CardDescription>총 {challengers.length}명의 챌린저를 찾았습니다.</CardDescription>
            </CardHeader>
            <CardContent>
              <DataTable columns={columns} data={challengers} />
            </CardContent>
          </Card>
        )}

        {!isLoading && searchQuery && challengers && challengers.length === 0 && (
          <Card>
            <CardContent className="flex h-32 items-center justify-center">
              <p className="text-muted-foreground">검색 결과가 없습니다.</p>
            </CardContent>
          </Card>
        )}
      </div>

      {/* 프로젝트 배정 Dialog */}
      <AssignProjectDialog
        challenger={selectedChallenger}
        open={isAssignDialogOpen}
        onOpenChange={setIsAssignDialogOpen}
      />

      {/* 프로젝트 제거 Dialog */}
      <RemoveProjectDialog
        challenger={selectedChallenger}
        open={isRemoveDialogOpen}
        onOpenChange={setIsRemoveDialogOpen}
      />
    </>
  );
};

export default AdminChallengerSearchPage;
