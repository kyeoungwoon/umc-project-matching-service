'use client';

import { useEffect } from 'react';

import Link from 'next/link';
import { usePathname, useRouter } from 'next/navigation';

import SidebarSkeleton from '@skeletons/components/SidebarSkeleton';
import { clsx } from 'clsx';
import { LogOutIcon } from 'lucide-react';
import { toast } from 'sonner';

import { Button } from '@styles/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@styles/components/ui/dropdown-menu';
import { Label } from '@styles/components/ui/label';
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from '@styles/components/ui/sidebar';

import { useGetMe, useLogout } from '@api/tanstack/challengers.queries';

import { ROUTES } from '@common/constants/routes.constants';
import { getMenusByPart } from '@common/constants/sidebar-menu.constants';

import { useGetChallengerChapterRoles } from '@common/hooks/check-challenger-permissions';

import SchoolLogo from '@common/components/SchoolImage';
import UpmsLogo from '@common/components/upms/UpmsLogo';

import { useClearUser, useSetUserInfo } from '@features/auth/hooks/useAuthStore';

import PartIcon from '@features/auth/components/PartIcon';

const UpmsSideBar = () => {
  const { data: myInfo, isLoading } = useGetMe();
  const setUserInfo = useSetUserInfo();
  const clearUser = useClearUser();
  const router = useRouter();
  const { roles } = useGetChallengerChapterRoles();
  const { mutate: logout } = useLogout();

  // useEffect로 이동하여 렌더링 중 setState 방지
  useEffect(() => {
    if (myInfo) {
      setUserInfo(myInfo);
    }
  }, [myInfo, setUserInfo]);

  const pathname = usePathname();

  const handleLogout = () => {
    clearUser();
    logout();
    toast.success('로그아웃 되었습니다.');
    return router.push(ROUTES.AUTH.LOGIN);
  };

  if (isLoading || !myInfo) {
    return <SidebarSkeleton />;
  }

  return (
    <Sidebar>
      <SidebarHeader className={'m-3 flex items-start'}>
        <UpmsLogo />
      </SidebarHeader>
      <SidebarContent>
        {/*// TODO: 파트, 운영진 필터링 다시 적용하기*/}
        {getMenusByPart(myInfo.part, roles).map((menu, idx) => {
          return (
            <SidebarGroup key={idx}>
              {menu.label && <SidebarGroupLabel>{menu.label}</SidebarGroupLabel>}
              <SidebarGroupContent>
                <SidebarMenu>
                  {menu.items.map((item) => {
                    // NOTE: 현재 경로와 메뉴의 url이 일치하는지 확인합니다.
                    const isSelected = pathname == item.url;
                    return (
                      <SidebarMenuItem key={item.title}>
                        <SidebarMenuButton asChild>
                          {item.isExternal ? ( // 외부 링크인 경우 a 태그로 처리
                            <a
                              href={item.url}
                              className={clsx(isSelected && 'bg-primary/5')}
                              target="_blank"
                              rel="noopener noreferrer"
                            >
                              <item.icon />
                              <span>{item.title}</span>
                            </a>
                          ) : (
                            <Link href={item.url} className={clsx(isSelected && 'bg-primary/5')}>
                              <item.icon />
                              <span>{item.title}</span>
                            </Link>
                          )}
                        </SidebarMenuButton>
                      </SidebarMenuItem>
                    );
                  })}
                </SidebarMenu>
              </SidebarGroupContent>
            </SidebarGroup>
          );
        })}
      </SidebarContent>
      <SidebarFooter className={'mb-5 px-4'}>
        <DropdownMenu>
          <DropdownMenuTrigger>
            <div className={'gap-8pxr flex w-full flex-row items-center px-3 py-2'}>
              <SchoolLogo logoUrl={myInfo.schoolLogoImageUrl} width={50} height={50} />
              <div className={'ml-2 flex w-full flex-col justify-between overflow-hidden'}>
                <Label className="truncate text-xl text-black">
                  {myInfo.nickname}/{myInfo.name}
                </Label>
                <Label className="truncate text-base text-gray-700">
                  {myInfo.schoolName} <PartIcon className={'h-4 w-4'} part={myInfo.part} />
                </Label>
              </div>
            </div>
          </DropdownMenuTrigger>
          <DropdownMenuContent
            side="top"
            className={'my-2 w-[var(--radix-dropdown-menu-trigger-width)]'}
          >
            <DropdownMenuItem asChild>
              <Button variant="ghost" className="w-full justify-start" onClick={handleLogout}>
                <LogOutIcon /> 로그아웃
              </Button>
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </SidebarFooter>
    </Sidebar>
  );
};

export default UpmsSideBar;
