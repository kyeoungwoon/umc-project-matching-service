import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export function proxy(req: NextRequest) {
  // 환경 변수 확인 (Vercel 대시보드에서 설정할 값)
  const isMaintenanceMode = process.env.MAINTENANCE_MODE === 'true';

  // 이미 점검 페이지에 있거나, 정적 파일 요청 등은 제외
  if (
    req.nextUrl.pathname.startsWith('/maintenance') ||
    req.nextUrl.pathname.startsWith('/_next') ||
    req.nextUrl.pathname.match(/\.(ico|png|jpg|jpeg|svg|css|js|json|xml|txt)$/)
  ) {
    return NextResponse.next();
  }

  // 점검 모드면 /maintenance로 리라이트 (URL은 유지하되 내용은 점검 페이지)
  if (isMaintenanceMode) {
    return NextResponse.rewrite(new URL('/maintenance', req.url));
  }

  return NextResponse.next();
}
