/**
 * 지원서 질문 API
 */
import { BaseApi } from '@api/base/BaseApi';
import type {
  ApplicationFormQuestion,
  ApplicationFormQuestionBulkCreateRequest,
  ApplicationFormQuestionCreateRequest,
  ApplicationFormQuestionUpdateRequest,
} from '@api/types/question';

class QuestionApi extends BaseApi {
  // 질문 조회
  async getQuestion(id: string): Promise<ApplicationFormQuestion> {
    return this.get<ApplicationFormQuestion>(`/v1/form-questions/${id}`);
  }

  // 질문 목록 조회
  async getQuestions(formId?: string): Promise<ApplicationFormQuestion[]> {
    const params = formId ? { formId } : undefined;
    return this.get<ApplicationFormQuestion[]>('/v1/form-questions', { params });
  }

  // 질문 생성
  async createQuestion(
    request: ApplicationFormQuestionCreateRequest,
  ): Promise<ApplicationFormQuestion> {
    return this.post<ApplicationFormQuestion>('/v1/form-questions', request);
  }

  // 질문 대량 생성
  async createQuestionsBulk(
    request: ApplicationFormQuestionBulkCreateRequest,
  ): Promise<ApplicationFormQuestion[]> {
    return this.post<ApplicationFormQuestion[]>('/v1/form-questions/bulk', request);
  }

  // 질문 수정
  async updateQuestion(
    id: string,
    request: ApplicationFormQuestionUpdateRequest,
  ): Promise<ApplicationFormQuestion> {
    return this.put<ApplicationFormQuestion>(`/v1/form-questions/${id}`, request);
  }

  // 질문 삭제
  async deleteQuestion(id: string): Promise<object> {
    return this.delete<object>(`/v1/form-questions/${id}`);
  }
}

export const questionApi = new QuestionApi();
