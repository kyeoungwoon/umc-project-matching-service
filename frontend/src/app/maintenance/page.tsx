'use client';

import { ClockIcon, HomeIcon, WrenchIcon } from 'lucide-react';

import { Button } from '@styles/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@styles/components/ui/card';
import { Separator } from '@styles/components/ui/separator';

import KyeoungWoonContact from '@common/components/KyeoungWoonContact';

const MaintenancePage = () => {
  const handleButtonClick = () => {
    window.open(
      'https://chunganguniv.notion.site/UPMS-Service-Status-2c039ff67177806fac8ad24ee827efd9?source=copy_link ',
      '_blank',
    );
  };

  return (
    <div className="from-background via-muted/20 to-background flex min-h-screen items-center justify-center bg-gradient-to-br p-4">
      <Card className="border-primary/50 w-full max-w-2xl shadow-2xl">
        <CardHeader className="text-center">
          <div className="bg-primary/10 mx-auto mb-4 flex h-20 w-20 items-center justify-center rounded-full">
            <WrenchIcon className="text-primary h-10 w-10" />
          </div>
          <CardTitle className="text-3xl font-bold">UPMS 점검 중</CardTitle>
          <CardDescription className="text-base">
            더 나은 서비스를 제공하기 위해 잠시 점검 중입니다.
          </CardDescription>
        </CardHeader>
        <Separator />
        <CardContent className="space-y-6 pt-6">
          {/* Maintenance Info */}
          <div className="border-primary/20 bg-primary/5 rounded-lg border p-4">
            <div className="space-y-3">
              <div className="flex items-center gap-2">
                <ClockIcon className="text-primary h-5 w-5" />
                <span className="text-primary font-semibold">점검 안내</span>
              </div>
              <p className="text-muted-foreground text-sm leading-relaxed">
                서비스 점검을 진행 중에 있습니다.
                <br />
                점검 시간 동안 일시적으로 서비스 이용이 제한됩니다.
              </p>
            </div>
          </div>

          {/* Notice List */}
          <div className="space-y-3">
            <h3 className="font-semibold">안내사항</h3>
            <ul className="text-muted-foreground space-y-2 text-sm">
              <li className="flex items-center gap-2">
                <div className="bg-primary h-1.5 w-1.5 rounded-full" />
                점검이 완료될 때까지 서비스 이용이 불가능합니다.
              </li>
              <li className="flex items-center gap-2">
                <div className="bg-primary h-1.5 w-1.5 rounded-full" />
                점검과 관련된 자세한 사항은 아래 버튼을 클릭해 Notion에서 확인하실 수 있습니다.
              </li>
            </ul>
          </div>

          <Separator />

          {/* Action Button */}
          <div className="flex flex-col gap-3">
            <Button onClick={handleButtonClick} className="w-full gap-2" size="lg">
              <HomeIcon className="h-4 w-4" />
              {/*{isLoggedIn ? '홈으로 가기' : '로그인 페이지로'}*/}
              UPMS 서비스 상태 보기
            </Button>
          </div>

          <KyeoungWoonContact />
        </CardContent>
      </Card>
    </div>
  );
};

export default MaintenancePage;
