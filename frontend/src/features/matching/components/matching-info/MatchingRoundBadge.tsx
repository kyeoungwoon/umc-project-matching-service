'use client';

const MatchingRoundBadge = ({ roundName }: { roundName: string | undefined }) => {
  return (
    <div className={'rounded-md bg-gray-100 px-3 py-1 text-lg font-medium text-black'}>
      {roundName || '매칭 차수 정보 없음'}
    </div>
  );
};

export default MatchingRoundBadge;
