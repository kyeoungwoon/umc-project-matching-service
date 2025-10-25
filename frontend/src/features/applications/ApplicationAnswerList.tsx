'use client';

import { ProjectApplicationResponseItem } from '@api/types/application';

import SingleQuestionAnswer from '@features/projects/components/SingleQuestionAnswer';

// 응답 목록을 렌더링하는 중간 컴포넌트
const ApplicationAnswerList = ({ responses }: { responses: ProjectApplicationResponseItem[] }) => {
  return (
    <div className="flex w-full flex-col gap-y-4">
      {responses &&
        responses.length > 0 &&
        // 답변 배열이 존재하고, 그 길이가 0보다 클 때만 렌더링
        responses.map((answer, idx) => <SingleQuestionAnswer key={idx} answer={answer} />)}
    </div>
  );
};

export default ApplicationAnswerList;
