'use client';

import { clsx } from 'clsx';

import { ChallengerPart } from '@api/types/common';

import { parsePart } from '@common/utils/parse-userinfo';

const ApplicantChallengerInfo = ({
  school,
  part,
  name,
  nickname,
  className,
}: {
  school: string;
  part: ChallengerPart;
  nickname: string;
  name: string;
  className?: string;
}) => {
  return (
    <div
      className={clsx('text-30pxr mt-3 flex flex-row items-end justify-start gap-x-2', className)}
    >
      <span className={''}>{school}</span>
      <span className={'font-semibold'}>
        {nickname}/{name}
      </span>
      <span className={'text-25pxr ml-2'}>{parsePart(part)}</span>
    </div>
  );
};

export default ApplicantChallengerInfo;
