import { ChallengerPart, ChallengerPartEnum } from '@api/types/common';

export const parsePart = (part: ChallengerPart) => {
  switch (part) {
    case ChallengerPartEnum.PLAN:
      return 'Plan';
    case ChallengerPartEnum.DESIGN:
      return 'Design';
    case ChallengerPartEnum.SPRINGBOOT:
      return 'SpringBoot';
    case ChallengerPartEnum.WEB:
      return 'Web';
    case ChallengerPartEnum.IOS:
      return 'iOS';
    case ChallengerPartEnum.ANDROID:
      return 'Android';
    case ChallengerPartEnum.NODEJS:
      return 'Node.js';
    case ChallengerPartEnum.NO_PART:
      return '운영진';
    default:
      return part;
  }
};
