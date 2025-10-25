'use client';

import { Separator } from '@styles/components/ui/separator';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

import NoticeCard from '@features/home/components/NoticeCard';
import MatchingRoundAnnouncementCard from '@features/matching/components/matching-info/MatchingRoundAnnouncementCard';

const HomePage = () => {
  const user = useGetUser();

  if (!user) {
    return <DefaultSkeleton />;
  }

  // const { data: challengerChapter, isLoading: isChallengerChapterLoading } =
  //   useGetChallengerChapter(user.info.id);
  //
  // if (!challengerChapter || isChallengerChapterLoading) {
  //   return <DefaultSkeleton />;
  // }

  return (
    <>
      <div className={'flex w-full flex-col items-start justify-center gap-y-3 p-4'}>
        {/* 현재 차수가 보여야 함 */}
        <MatchingRoundAnnouncementCard chapterId={user.info.chapterId} />
        <Separator />
        <NoticeCard />
      </div>
    </>
  );
};

export default HomePage;
