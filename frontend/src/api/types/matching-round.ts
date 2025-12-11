/**
 * 프로젝트 매칭 라운드 관련 타입 정의
 */

export interface ProjectMatchingRoundGetRequest {
  chapterId: string;
  startTime: string;
  endTime: string;
}

/**
 * 매칭 라운드 생성 요청
 */
export interface ProjectMatchingRoundCreateRequest {
  name: string;
  description?: string;
  chapterId: string;
  startAt: string;
  endAt: string;
  decisionDeadlineAt: string;
}

/**
 * 매칭 라운드 수정 요청
 */
export interface ProjectMatchingRoundUpdateRequest {
  name?: string;
  description?: string;
  startAt?: string;
  endAt?: string;
  decisionDeadlineAt?: string;
}

/**
 * 매칭 라운드 응답
 */
export interface ProjectMatchingRoundResponse {
  id: string;
  name: string;
  description?: string;
  chapterId: string;
  chapterName: string;
  startAt: string;
  endAt: string;
  decisionDeadlineAt: string;
  createdAt: string;
  updatedAt: string;
}
