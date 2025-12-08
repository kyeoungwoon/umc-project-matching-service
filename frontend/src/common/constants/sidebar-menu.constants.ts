import {
  CircleUserIcon,
  FileExclamationPointIcon,
  FilePenIcon,
  FileUserIcon,
  FolderOpen,
  HomeIcon,
  RectangleEllipsisIcon,
  Settings,
  SheetIcon,
  ShieldUserIcon,
  SparklesIcon,
  SquarePenIcon,
  SquarePlusIcon,
} from 'lucide-react';

import {
  ChallengerPart,
  ChallengerPartEnum,
  ChapterAdminRoleEnum,
  ChapterAdminRoleType,
} from '@api/types/common';

import { ROUTES } from '@common/constants/routes.constants';

// 🎯 개선된 타입 정의
export interface MenuItem {
  title: string;
  url: string;
  icon: any;
  isExternal?: boolean;
  // 권한 체크를 하나의 함수로 통합
  isVisible?: (part?: ChallengerPart, roles?: ChapterAdminRoleType[]) => boolean;
}

export interface SidebarMenus {
  label?: string;
  items: MenuItem[];
}

// 🔐 권한 체크 헬퍼 함수들
const requiresPart = (requiredPart: ChallengerPart) => {
  return (part?: ChallengerPart) => part === requiredPart;
};

const requiresAnyRole = (requiredRoles: ChapterAdminRoleType[]) => {
  return (_part?: ChallengerPart, roles?: ChapterAdminRoleType[]) =>
    roles?.some((role) => requiredRoles.includes(role)) ?? false;
};

const requiresMasterRole = requiresAnyRole([
  ChapterAdminRoleEnum.CHAPTER_LEAD,
  ChapterAdminRoleEnum.CENTRAL_LEAD,
  ChapterAdminRoleEnum.SCHOOL_LEAD,
]);

const requiresAdminRole = requiresAnyRole([
  ChapterAdminRoleEnum.CENTRAL_LEAD,
  ChapterAdminRoleEnum.CHAPTER_LEAD,
  ChapterAdminRoleEnum.SCHOOL_LEAD,
]);

// 🌐 외부 링크 상수
const EXTERNAL_LINKS = {
  GOOGLE_FORM: 'https://forms.gle/KNamMGSzk6r166mg6',
  MASTER_SHEET:
    'https://docs.google.com/spreadsheets/d/1L6tEzM3KVizPdI_e7tIlEDsXpLOuZtispyfJCQzXeiM/edit?gid=445694956#gid=445694956',
  LEO_IDEAS:
    'https://makeus-challenge.notion.site/9th-UMC-Leo-IDEAS-25ab57f4596b810fb951c6b370db4cf7',
  KAKAO_TALK_LINK: 'https://open.kakao.com/o/sBNfZS4h',
} as const;

// 📋 메뉴 정의 (단일 배열로 통합)
const ALL_MENUS: SidebarMenus[] = [
  {
    label: undefined,
    items: [
      {
        title: '홈',
        url: ROUTES.HOME,
        icon: HomeIcon,
      },
      {
        title: '프로젝트 목록 및 지원',
        url: ROUTES.PROJECTS.LIST,
        icon: FolderOpen,
      },
      {
        title: '프로젝트 멤버 현황',
        url: ROUTES.PROJECTS.MASTERSHEET,
        icon: SheetIcon,
      },
    ],
  },
  {
    label: 'Plan',
    items: [
      {
        title: '내 프로젝트 조회',
        url: ROUTES.PROJECTS.MY_PROJECTS,
        icon: Settings,
        isVisible: requiresPart(ChallengerPartEnum.PLAN),
      },
      {
        title: '프로젝트 만들기',
        url: ROUTES.PROJECTS.CREATE,
        icon: SquarePlusIcon,
        isVisible: requiresPart(ChallengerPartEnum.PLAN),
      },
    ],
  },
  {
    label: '운영진',
    items: [
      {
        title: '운영진 대시보드',
        url: ROUTES.ADMIN.DASHBOARD,
        icon: ShieldUserIcon,
        isVisible: requiresAdminRole,
      },
      {
        title: '매칭 차수 설정',
        url: ROUTES.ADMIN.MATCHING_ROUNDS,
        icon: SquarePenIcon,
        isVisible: requiresMasterRole,
      },
      {
        title: '프로젝트 생성',
        url: ROUTES.PROJECTS.CREATE,
        icon: SquarePlusIcon,
        isVisible: requiresAdminRole,
      },
    ],
  },
  {
    label: 'MY',
    items: [
      {
        title: '내 프로필',
        url: ROUTES.MY.INFO,
        icon: CircleUserIcon,
      },
      {
        title: '내 지원 현황',
        url: ROUTES.MY.APPLICATIONS,
        icon: FileUserIcon,
      },
      {
        title: '비밀번호 변경',
        url: ROUTES.MY.CHANGE_PASSWORD,
        icon: RectangleEllipsisIcon,
      },
    ],
  },
  {
    label: '외부 링크',
    items: [
      {
        title: '문의 및 건의사항 구글 폼',
        icon: FilePenIcon,
        url: EXTERNAL_LINKS.GOOGLE_FORM,
        isExternal: true,
      },
      {
        title: '팀 매칭 마스터시트',
        icon: SheetIcon,
        url: EXTERNAL_LINKS.MASTER_SHEET,
        isExternal: true,
      },
      {
        title: '9th Leo IDEAS',
        icon: SparklesIcon,
        url: EXTERNAL_LINKS.LEO_IDEAS,
        isExternal: true,
      },
      {
        title: '버그 및 문의 오픈채팅방',
        icon: FileExclamationPointIcon,
        url: EXTERNAL_LINKS.KAKAO_TALK_LINK,
        isExternal: true,
      },
    ],
  },
];

// 🎯 메뉴 필터링 함수 (개선됨)
export const getMenusByPart = (
  part: ChallengerPart | undefined,
  roles: ChapterAdminRoleType[] = [],
): SidebarMenus[] => {
  return ALL_MENUS.map((menu) => ({
    ...menu,
    items: menu.items.filter((item) => {
      // isVisible이 없으면 공용 메뉴 (모두에게 표시)
      if (!item.isVisible) return true;

      // isVisible 함수로 권한 체크
      return item.isVisible(part, roles);
    }),
  })).filter((menu) => menu.items.length > 0);
};

// 🔍 특정 메뉴 검색 헬퍼 (추가 기능)
export const findMenuByUrl = (url: string): MenuItem | undefined => {
  for (const menu of ALL_MENUS) {
    const found = menu.items.find((item) => item.url === url);
    if (found) return found;
  }
  return undefined;
};

// 📊 권한별 메뉴 개수 계산 (디버깅용)
export const getMenuStats = (part?: ChallengerPart, roles: ChapterAdminRoleType[] = []) => {
  const visibleMenus = getMenusByPart(part, roles);
  return {
    totalCategories: visibleMenus.length,
    totalItems: visibleMenus.reduce((sum, menu) => sum + menu.items.length, 0),
    byCategory: visibleMenus.map((menu) => ({
      label: menu.label ?? '기본',
      count: menu.items.length,
    })),
  };
};
