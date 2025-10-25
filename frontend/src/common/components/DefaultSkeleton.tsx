import HeaderSkeleton from '@skeletons/components/HeaderSkeleton';

import { Skeleton } from '@styles/components/ui/skeleton';

const DefaultSkeleton = () => {
  return (
    <div className="flex h-full w-full flex-col gap-4 p-6">
      {/* Header Skeleton */}
      <HeaderSkeleton />

      {/* Main Content Skeleton */}
      <div className="flex-1 space-y-4">
        <Skeleton className="h-12 w-full" />
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {[...Array(6)].map((_, i) => (
            <div key={i} className="space-y-3 rounded-lg border p-4">
              <Skeleton className="h-6 w-3/4" />
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-5/6" />
              <div className="flex gap-2 pt-2">
                <Skeleton className="h-6 w-16" />
                <Skeleton className="h-6 w-16" />
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default DefaultSkeleton;
