'use client';

import Link from 'next/link';

import { MegaphoneIcon } from 'lucide-react';

import { Item, ItemFooter, ItemTitle } from '@styles/components/ui/item';

import GitHubIcon from '@icons/GitHub';

import NoticeContent from '@features/home/components/NoticeContent';
import NoticeTitle from '@features/home/components/NoticeTitle';

import { notice } from '@features/home/docs/notice';

const NoticeCard = () => {
  const PROJECT_GUIDE_PAGE =
    'https://makeus-challenge.notion.site/Project-Guideline-2a0b57f4596b8085b536cfec845c3e2b';
  const GITHUB_LINK = 'https://github.com/kyeoungwoon/umc-project-matching-service';

  const HANEUL_LINK = 'https://github.com/kyeoungwoon';
  const BONNY_LINK = 'https://github.com/Bowoon1216';
  const BELLA_LINK = 'https://github.com/hwangjeewon';
  const MATTY_LINK = 'https://avanturation.com/';

  return (
    <Item className={'flex flex-col items-start p-5'}>
      <ItemTitle className={'mb-4 flex flex-row items-center gap-x-2 text-3xl font-bold'}>
        <MegaphoneIcon className={'h-8 w-8'} />
        공지사항
      </ItemTitle>
      {/*ItemDescription은 내부적으로 p 태그를 사용하고 있어 hydration error 발생*/}
      <div className={'text-17pxr'}>
        {notice.map((content, idx) => {
          return (
            <div className={'mb-4 flex flex-col gap-y-2'} key={idx}>
              <NoticeTitle text={content.title} />
              {content.content.map((c, i) => {
                return <NoticeContent text={c} key={i} />;
              })}
            </div>
          );
        })}
        {/*구분선*/}
      </div>
      <ItemFooter className={'text-18pxr mt-2 flex flex-col items-start gap-y-2'}>
        <span className={'text-2xl font-bold text-gray-800'}>UPMS 제작 및 도움을 준 사람들</span>
        <div className={'grid grid-cols-2 gap-x-4 gap-y-1'}>
          <Link className={'text-blue-600'} href={HANEUL_LINK}>
            중앙대학교 회장 하늘/박경운
          </Link>
          <Link className={'text-violet-600'} href={BONNY_LINK}>
            중앙대학교 부회장 보니/정보운
          </Link>
          <Link className={'text-[#E65787]'} href={BELLA_LINK}>
            중앙대학교 Plan 파트장 벨라/황지원
          </Link>
          <Link className={'text-[#808080]'} href={MATTY_LINK}>
            숭실대학교 Design 파트장 매티/조현우
          </Link>
        </div>
        <Link
          href={GITHUB_LINK}
          className={'mt-3 flex flex-row items-center gap-x-2 text-base font-semibold text-black'}
        >
          <GitHubIcon /> UPMS Source Code
        </Link>
      </ItemFooter>
    </Item>
  );
};

export default NoticeCard;
