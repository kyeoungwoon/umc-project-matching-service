'use client';

import { useState } from 'react';

import Image from 'next/image';
import { useRouter } from 'next/navigation';

import { ExternalLink, Settings, Trash2 } from 'lucide-react';
import { toast } from 'sonner';

import { Badge } from '@styles/components/ui/badge';
import { Button } from '@styles/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@styles/components/ui/card';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@styles/components/ui/dialog';
import { Label } from '@styles/components/ui/label';

import { useDeleteProject, useGetProjects } from '@api/tanstack/projects.queries';
import type { Project } from '@api/types/project';

import { ROUTES } from '@common/constants/routes.constants';

import { calculateCurrentAndMaxProjectTo } from '@common/utils/project-response-card';

import DefaultSkeleton from '@common/components/DefaultSkeleton';
import UpmsHeader from '@common/components/upms/UpmsHeader';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

const AdminProjectListPage = () => {
  const user = useGetUser();
  const chapterId = user?.info.chapterId;
  const router = useRouter();

  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [projectToDelete, setProjectToDelete] = useState<Project | null>(null);

  const { data: projects, isLoading } = useGetProjects(chapterId);
  const { mutate: deleteProject, isPending: isDeleting } = useDeleteProject();

  const handleManageProject = (projectId: string) => {
    router.push(ROUTES.ADMIN.PROJECT_EDIT(projectId));
  };

  const handleViewNotion = (notionLink: string) => {
    if (notionLink) {
      window.open(notionLink, '_blank');
    }
  };

  const handleDeleteClick = (project: Project) => {
    setProjectToDelete(project);
    setDeleteDialogOpen(true);
  };

  const handleConfirmDelete = () => {
    if (!projectToDelete) return;

    deleteProject(projectToDelete.id, {
      onSuccess: () => {
        toast.success(`${projectToDelete.name} 프로젝트를 삭제했습니다.`);
        setDeleteDialogOpen(false);
        setProjectToDelete(null);
      },
      onError: (err) => {
        toast.error('프로젝트 삭제에 실패했습니다.', {
          description: err.message,
        });
      },
    });
  };

  const section = {
    title: '프로젝트 관리',
    description: '지부 내 모든 프로젝트를 조회하고 관리할 수 있습니다.',
  };

  if (isLoading) {
    return <DefaultSkeleton />;
  }

  if (!projects || projects.length === 0) {
    return (
      <>
        <UpmsHeader section={section} />
        <div className="flex h-full items-center justify-center">
          <div className="text-center">
            <h2 className="text-2xl font-bold">등록된 프로젝트가 없습니다.</h2>
            <p className="text-muted-foreground">프로젝트를 생성해주세요.</p>
          </div>
        </div>
      </>
    );
  }

  return (
    <div className="container space-y-6 p-6">
      <UpmsHeader section={section} />

      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        {projects.map((project) => (
          <ProjectCard
            key={project.id}
            project={project}
            onManage={handleManageProject}
            onViewNotion={handleViewNotion}
            onDelete={handleDeleteClick}
          />
        ))}
      </div>

      {/* 삭제 확인 Dialog */}
      <Dialog open={deleteDialogOpen} onOpenChange={setDeleteDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>프로젝트 삭제</DialogTitle>
            <DialogDescription>
              정말로 <strong>{projectToDelete?.name}</strong> 프로젝트를 삭제하시겠습니까?
              <br />이 작업은 되돌릴 수 없습니다.
            </DialogDescription>
          </DialogHeader>
          <DialogFooter>
            <Button variant="outline" onClick={() => setDeleteDialogOpen(false)} disabled={isDeleting}>
              취소
            </Button>
            <Button variant="destructive" onClick={handleConfirmDelete} disabled={isDeleting}>
              {isDeleting ? '삭제 중...' : '삭제'}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

interface ProjectCardProps {
  project: Project;
  onManage: (projectId: string) => void;
  onViewNotion: (notionLink: string) => void;
  onDelete: (project: Project) => void;
}

const ProjectCard = ({ project, onManage, onViewNotion, onDelete }: ProjectCardProps) => {
  const {
    id,
    name,
    description,
    productOwnerName,
    chapterName,
    logoImageUrl,
    bannerImageUrl,
    notionLink,
    projectTos,
    projectMembers,
  } = project;

  const partData = calculateCurrentAndMaxProjectTo(projectTos, projectMembers);

  return (
    <Card className="flex h-full flex-col overflow-hidden">
      {/* 배너 이미지 */}
      <div className="relative h-40 w-full overflow-hidden">
        <Image
          src={bannerImageUrl || 'https://placehold.co/600x400'}
          alt={`${name} 배너`}
          fill
          className="object-cover"
        />
      </div>

      <CardHeader className="pb-3">
        <div className="flex items-start justify-between gap-2">
          <div className="flex-1 space-y-1">
            <CardTitle className="line-clamp-1 text-xl">{name}</CardTitle>
            <CardDescription className="text-xs">
              PO: {productOwnerName} | {chapterName}
            </CardDescription>
          </div>
          {logoImageUrl && (
            <div className="relative h-12 w-12 flex-shrink-0 overflow-hidden rounded-md border">
              <Image src={logoImageUrl} alt={`${name} 로고`} fill className="object-cover" />
            </div>
          )}
        </div>
      </CardHeader>

      <CardContent className="flex flex-1 flex-col gap-4 pb-4">
        {/* 프로젝트 설명 */}
        <p className="text-muted-foreground line-clamp-2 text-sm">{description}</p>

        {/* 파트별 TO 현황 */}
        <div className="space-y-2">
          <Label className="text-xs font-semibold">파트별 모집 현황</Label>
          <div className="space-y-1">
            {partData.map((item, index) => (
              <div key={index} className="flex items-center justify-between text-sm">
                <span className="text-muted-foreground min-w-20">{item.part}</span>
                <div className="flex items-center gap-2">
                  <span className="text-xs">
                    {item.currentTo}/{item.maxTo}
                  </span>
                  <Badge
                    variant={item.currentTo >= item.maxTo ? 'destructive' : 'default'}
                    className="h-5 text-xs"
                  >
                    {item.currentTo >= item.maxTo ? '완료' : '모집중'}
                  </Badge>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="flex-1" />

        {/* 액션 버튼들 */}
        <div className="flex flex-col gap-2">
          <div className="flex gap-2">
            {notionLink && (
              <Button variant="outline" size="sm" onClick={() => onViewNotion(notionLink)} className="flex-1">
                <ExternalLink className="mr-1 h-3 w-3" />
                Notion
              </Button>
            )}
            <Button variant="default" size="sm" onClick={() => onManage(id)} className="flex-1">
              <Settings className="mr-1 h-3 w-3" />
              관리
            </Button>
          </div>
          <Button variant="destructive" size="sm" onClick={() => onDelete(project)} className="w-full">
            <Trash2 className="mr-1 h-3 w-3" />
            삭제
          </Button>
        </div>
      </CardContent>
    </Card>
  );
};

export default AdminProjectListPage;
