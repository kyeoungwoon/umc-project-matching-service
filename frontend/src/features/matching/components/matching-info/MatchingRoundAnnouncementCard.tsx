'use client';

import HeaderSkeleton from '@skeletons/components/HeaderSkeleton';
import { toast } from 'sonner';

import { useGetCurrentOrUpcomingMatchingRound } from '@api/tanstack/matching-rounds.queries';

import MatchingRoundInfoCard from '@features/matching/components/matching-info/MatchingRoundInfoCard';
import NoCurrentMatchingRound from '@features/matching/components/matching-info/NoCurrentMatchingRound';

const MatchingRoundAnnouncementCard = ({ chapterId }: { chapterId: string }) => {
  const {
    data: matchingRound,
    isLoading,
    isError,
  } = useGetCurrentOrUpcomingMatchingRound(chapterId);

  if (isLoading) {
    return <HeaderSkeleton />;
  }

  if (isError) {
    // console.error('매칭 차수 조회 오류:', error);
    toast.error('미래 매칭 차수가 존재하지 않습니다.', {
      richColors: true,
      description: '지부 운영진에게 문의해주세요.',
    });
  }

  return (
    <>
      {matchingRound ? <MatchingRoundInfoCard data={matchingRound} /> : <NoCurrentMatchingRound />}
    </>
  );
};

export default MatchingRoundAnnouncementCard;
