'use client';

import Link from 'next/link';

const KyeoungWoonContact = () => {
  const KAKAO_TALK_LINK = 'https://open.kakao.com/o/sBNfZS4h';

  return (
    <div className="bg-muted/50 text-muted-foreground rounded-lg p-4 text-center text-xs">
      <p>
        문의사항이 있으신 경우, <span className={'font-extrabold'}>중앙대학교 하늘/박경운</span>{' '}
        에게
      </p>
      <p>
        <Link href="mailto:saveearth1@naver.com" className="text-primary font-medium underline">
          이메일 (saveearth1@naver.com)
        </Link>{' '}
        이나{' '}
        <Link href={KAKAO_TALK_LINK} className="text-primary font-medium underline">
          카카오톡 오픈 채팅방
        </Link>{' '}
        을 통해서 연락해주세요.
      </p>
    </div>
  );
};

export default KyeoungWoonContact;
