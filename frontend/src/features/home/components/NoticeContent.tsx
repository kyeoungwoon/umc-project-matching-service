'use client';

const NoticeContent = ({ text }: { text: string }) => {
  return <span className={'text-base tracking-tight'}>- {text}</span>;
};

export default NoticeContent;
