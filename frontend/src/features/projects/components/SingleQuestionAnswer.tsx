'use client';

import { ProjectApplicationResponseItem } from '@api/types/application';

// 질문 1개에 대한 응답을 나타내는 컴포넌트
const SingleQuestionAnswer = ({ answer }: { answer: ProjectApplicationResponseItem }) => {
  return (
    <div className="flex flex-col gap-y-2">
      {/*질문 제목*/}
      {/*질문 배지 삭제 처리 하였음, 디자인 상 어떤게 더 좋을까?*/}
      <p className="ml-1 text-base leading-relaxed font-semibold tracking-tight">
        {answer.questionTitle}
      </p>
      {/*질문 답변*/}
      <div className="flex flex-col gap-y-4">
        {answer.values.length > 0 && answer.values[0] !== '' ? (
          <div className="space-y-2">
            {answer.values.map((val, idx) => (
              <div key={idx} className="bg-muted/50 border-border/50 rounded-lg border p-3 text-sm">
                {val}
              </div>
            ))}
          </div>
        ) : (
          <p className="text-muted-foreground text-sm italic">답변 없음</p>
        )}
      </div>
    </div>
  );
};

export default SingleQuestionAnswer;
