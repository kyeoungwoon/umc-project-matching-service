'use client';

const ApplicationInfo = ({ id, createdAt }: { id: string; createdAt: string }) => {
  return (
    <div className={'text-16pxr mb-6 flex flex-col gap-y-1 text-gray-500'}>
      <p className={'flex flex-row'}>
        <span className={'w-20'}>지원서 ID</span>
        <span className={'text-gray-800'}>{id}</span>
      </p>
      <p className={'flex flex-row'}>
        <span className={'w-20'}>제출일</span>
        <span>
          <span className={'text-gray-800'}>{new Date(createdAt).toLocaleString('ko-KR')}</span>
        </span>
      </p>
    </div>
  );
};

export default ApplicationInfo;
