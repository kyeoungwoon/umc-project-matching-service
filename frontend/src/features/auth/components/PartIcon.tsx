'use client';

import { clsx } from 'clsx';
import {
  AppleIcon,
  BadgeQuestionMarkIcon,
  BookOpenIcon,
  BotIcon,
  BrushIcon,
  EarthIcon,
  HexagonIcon,
  LeafIcon,
  ShieldUserIcon,
} from 'lucide-react';

import { ChallengerPart, ChallengerPartEnum } from '@api/types/common';

const PartIcon = ({ part, className }: { part: ChallengerPart; className?: string }) => {
  const iconClassName = clsx(className);

  switch (part) {
    case ChallengerPartEnum.PLAN:
      return <BookOpenIcon className={iconClassName} />;
    case ChallengerPartEnum.DESIGN:
      return <BrushIcon className={iconClassName} />;
    case ChallengerPartEnum.WEB:
      return <EarthIcon className={iconClassName} />;
    case ChallengerPartEnum.IOS:
      return <AppleIcon className={iconClassName} />;
    case ChallengerPartEnum.ANDROID:
      return <BotIcon className={iconClassName} />;
    case ChallengerPartEnum.SPRINGBOOT:
      return <LeafIcon className={iconClassName} />;
    case ChallengerPartEnum.NODEJS:
      return <HexagonIcon className={iconClassName} />;
    case ChallengerPartEnum.NO_PART:
      return <ShieldUserIcon className={iconClassName} />;
    default:
      return <BadgeQuestionMarkIcon className={iconClassName} />;
  }
};

export default PartIcon;
