import { Skeleton } from '@styles/components/ui/skeleton';

const ProjectCardSkeleton = () => {
  return (
    <div className="w-[350px] rounded-xl border bg-white p-4 shadow-sm">
      {/* 이미지/썸네일 영역 */}
      <Skeleton className="mb-4 h-32 w-full rounded-md" />

      {/* 제목 */}
      <div className="mb-1 text-lg font-bold">
        <Skeleton className="h-6 w-20" />
      </div>

      {/* 설명 */}
      <div className="mb-3">
        <Skeleton className="mb-1 h-4 w-full" />
        <Skeleton className="h-4 w-2/3" />
      </div>

      {/* 팀원 모집 현황 3줄 */}
      <div className="mb-5 space-y-1">
        <Skeleton className="h-4 w-24" />
        <Skeleton className="h-4 w-24" />
        <Skeleton className="h-4 w-24" />
      </div>

      {/* 버튼 두 개 */}
      <div className="flex gap-2">
        <Skeleton className="h-10 w-32 rounded-md" />
        <Skeleton className="bg-muted h-10 w-32 rounded-md" />
      </div>
    </div>
  );
};

export default ProjectCardSkeleton;
