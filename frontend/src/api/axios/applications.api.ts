/**
 * 프로젝트 지원 API
 */
import { BaseApi } from '@api/base/BaseApi';
import type {
  ChallengerApplicationSummary,
  ProjectApplication,
  ProjectApplicationCreateRequest,
  ProjectApplicationDetail,
  ProjectApplicationUpdateRequest,
} from '@api/types/application';

class ApplicationApi extends BaseApi {
  // 지원서 조회
  async getApplication(id: string): Promise<ProjectApplication> {
    return this.get<ProjectApplication>(`/v1/application/${id}`);
  }

  // 지원서 목록 조회
  async getApplications(
    chapterId?: string,
    projectId?: string,
    challengerId?: string,
  ): Promise<ProjectApplication[]> {
    return this.get<ProjectApplication[]>('/v1/application', {
      params: { chapterId, projectId, challengerId },
    });
  }

  async getMyApplications(): Promise<ProjectApplicationDetail[]> {
    return this.get<ProjectApplicationDetail[]>('/v1/application/me');
  }

  // 폼별 지원서 상세 목록 조회
  async getApplicationsByForm(formId: string): Promise<ProjectApplicationDetail[]> {
    return this.get<ProjectApplicationDetail[]>(`/v1/application/form/${formId}`);
  }

  // 챕터별 지원 현황 요약 조회
  async getChapterApplicationSummary(chapterId: string): Promise<ChallengerApplicationSummary[]> {
    return this.get<ChallengerApplicationSummary[]>(`/v1/application/chapter/${chapterId}/summary`);
  }

  // 지원서 제출
  async createApplication(request: ProjectApplicationCreateRequest): Promise<ProjectApplication> {
    return this.post<ProjectApplication>('/v1/application', request);
  }

  // 지원서 상태 수정
  async updateApplication(
    id: string,
    request: ProjectApplicationUpdateRequest,
  ): Promise<ProjectApplication> {
    return this.put<ProjectApplication>(`/v1/application/${id}`, request);
  }

  // 지원서 삭제
  async deleteApplication(id: string): Promise<object> {
    return this.delete<object>(`/v1/application/${id}`);
  }
}

export const applicationApi = new ApplicationApi();
