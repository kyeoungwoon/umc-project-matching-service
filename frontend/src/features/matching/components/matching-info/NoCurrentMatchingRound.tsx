'use client';

import { InfoIcon } from 'lucide-react';

const NoCurrentMatchingRound = () => {
  return (
    <div className="flex h-full w-full flex-row items-center justify-center gap-x-2 py-8 text-3xl">
      {/*동그라미 아이콘*/}
      <div className="bg-muted flex h-12 w-12 items-center justify-center rounded-full">
        <InfoIcon className="text-muted-foreground h-6 w-6" />
      </div>

      <p className="text-gray-800">진행 예정인 매칭 차수가 존재하지 않습니다.</p>
      <p className="text-muted-foreground">프로젝트 화이팅하세요!</p>
    </div>
  );
};

export default NoCurrentMatchingRound;
