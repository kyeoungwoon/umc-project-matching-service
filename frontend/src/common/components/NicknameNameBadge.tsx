'use client';

import { Badge } from '@styles/components/ui/badge';

const NicknameNameBadge = ({ nickname, name }: { nickname: string; name: string }) => {
  return (
    <Badge variant={'outline'} className="text-sm">
      {nickname}/{name}
    </Badge>
  );
};

export default NicknameNameBadge;
