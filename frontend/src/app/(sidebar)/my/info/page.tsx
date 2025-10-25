'use client';

import { Card, CardContent, CardHeader, CardTitle } from '@styles/components/ui/card';
import { Label } from '@styles/components/ui/label';

import DefaultSkeleton from '@common/components/DefaultSkeleton';

import { useGetUser } from '@features/auth/hooks/useAuthStore';

const MyInfoPage = () => {
  const userObj = useGetUser();
  const user = userObj?.info;

  if (!user) {
    return <DefaultSkeleton />;
  }

  return (
    <div className="flex w-full flex-col items-center justify-center gap-y-4 p-4">
      <Card className="w-full max-w-2xl">
        <CardHeader>
          <CardTitle>내 정보</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="flex items-center">
            <Label className="w-24">이름</Label>
            <p className="text-sm">{user.name}</p>
          </div>
          <div className="flex items-center">
            <Label className="w-24">닉네임</Label>
            <p className="text-sm">{user.nickname}</p>
          </div>
          <div className="flex items-center">
            <Label className="w-24">학교</Label>
            <p className="text-sm">{user.schoolName}</p>
          </div>
          <div className="flex items-center">
            <Label className="w-24">학번</Label>
            <p className="text-sm">{user.studentId}</p>
          </div>
          <div className="flex items-center">
            <Label className="w-24">파트</Label>
            <p className="text-sm">{user.part}</p>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default MyInfoPage;
