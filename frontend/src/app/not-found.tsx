'use client';

import { useRouter } from 'next/navigation';

import { ArrowLeftIcon, HomeIcon, SearchXIcon } from 'lucide-react';

import { Button } from '@styles/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@styles/components/ui/card';
import { Separator } from '@styles/components/ui/separator';

import { ROUTES } from '@common/constants/routes.constants';

import KyeoungWoonContact from '@common/components/KyeoungWoonContact';

import AuthStore from '@features/auth/stores/auth-store';

const NotFoundPage = () => {
  const router = useRouter();
  const { user } = AuthStore();
  const isLoggedIn = !!user?.accessToken;

  const handleGoHome = () => {
    if (isLoggedIn) {
      router.push(ROUTES.HOME);
    } else {
      router.push(ROUTES.AUTH.LOGIN);
    }
  };

  const handleGoBack = () => {
    router.back();
  };

  return (
    <div className="from-background via-muted/20 to-background flex min-h-screen items-center justify-center bg-gradient-to-br p-4">
      <Card className="border-muted-foreground/30 w-full max-w-2xl shadow-2xl">
        <CardHeader className="text-center">
          <div className="bg-muted-foreground/10 mx-auto mb-4 flex h-20 w-20 items-center justify-center rounded-full">
            <SearchXIcon className="text-muted-foreground h-10 w-10" />
          </div>
          <CardTitle className="text-6xl font-bold">404</CardTitle>
          <CardDescription className="text-xl font-semibold">
            페이지를 찾을 수 없습니다
          </CardDescription>
          <CardDescription className="text-base">
            요청하신 페이지가 존재하지 않거나 이동되었습니다.
          </CardDescription>
        </CardHeader>
        <Separator />
        <CardContent className="space-y-6 pt-6">
          {/* 404 Info */}
          <div className="border-muted-foreground/20 bg-muted/30 rounded-lg border p-4">
            <div className="space-y-2">
              <p className="text-muted-foreground text-center text-sm leading-relaxed">
                입력하신 주소가 정확한지 다시 한번 확인해 주세요.
                <br />
                페이지 주소가 변경 또는 삭제되어 찾을 수 없습니다.
              </p>
            </div>
          </div>

          {/* Suggestions */}
          <div className="space-y-3">
            <h3 className="font-semibold">다음을 시도해보세요:</h3>
            <ul className="text-muted-foreground space-y-2 text-sm">
              <li className="flex items-center gap-2">
                <div className="bg-primary h-1.5 w-1.5 rounded-full" />
                URL 주소를 다시 확인해보세요
              </li>
              <li className="flex items-center gap-2">
                <div className="bg-primary h-1.5 w-1.5 rounded-full" />
                홈페이지로 이동하여 원하시는 페이지를 찾아보세요
              </li>
              <li className="flex items-center gap-2">
                <div className="bg-primary h-1.5 w-1.5 rounded-full" />
                이전 페이지로 돌아가서 다시 시도해보세요
              </li>
              <li className="flex items-center gap-2">
                <div className="bg-primary h-1.5 w-1.5 rounded-full" />
                문제가 계속되면 관리자에게 문의해주세요
              </li>
            </ul>
          </div>

          <Separator />

          {/* Action Buttons */}
          <div className="flex flex-col gap-3 sm:flex-row">
            <Button onClick={handleGoBack} variant="outline" className="flex-1 gap-2" size="lg">
              <ArrowLeftIcon className="h-4 w-4" />
              이전 페이지로
            </Button>
            <Button onClick={handleGoHome} className="flex-1 gap-2" size="lg">
              <HomeIcon className="h-4 w-4" />
              {isLoggedIn ? '홈으로 가기' : '로그인 페이지로'}
            </Button>
          </div>

          <KyeoungWoonContact />
        </CardContent>
      </Card>
    </div>
  );
};

export default NotFoundPage;
