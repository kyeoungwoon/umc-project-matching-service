import { Skeleton } from '@styles/components/ui/skeleton';

const HeaderSkeleton = () => {
  return (
    <div className="flex w-full items-center justify-between">
      <div className="space-y-2">
        <Skeleton className="h-6 w-48" />
        <Skeleton className="h-4 w-64" />
      </div>
      <Skeleton className="h-10 w-24" />
    </div>
  );
};

export default HeaderSkeleton;
