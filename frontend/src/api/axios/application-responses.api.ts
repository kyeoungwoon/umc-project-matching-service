/**
 * 프로젝트 지원서 응답 API
 */
import { BaseApi } from '@api/base/BaseApi';
import {
  ProjectApplicationResponseBulkCreateRequest,
  ProjectApplicationResponseCreateRequest,
  ProjectApplicationResponseItem,
  ProjectApplicationResponseUpdateRequest,
} from '@api/types/application';

class ApplicationResponseApi extends BaseApi {
  // 지원서 응답 단건 조회
  async getApplicationResponse(id: string): Promise<ProjectApplicationResponseItem> {
    return this.get<ProjectApplicationResponseItem>(`/v1/application-responses/${id}`);
  }

  // 지원서 응답 목록 조회
  async getApplicationResponses(applicationId?: string): Promise<ProjectApplicationResponseItem[]> {
    const params = applicationId ? { applicationId } : undefined;
    return this.get<ProjectApplicationResponseItem[]>('/v1/application-responses', { params });
  }

  // 지원서 응답 생성
  async createApplicationResponse(
    request: ProjectApplicationResponseCreateRequest,
  ): Promise<ProjectApplicationResponseItem> {
    return this.post<ProjectApplicationResponseItem>('/v1/application-responses', request);
  }

  // 지원서 응답 생성
  async createBulkApplicationResponse(
    request: ProjectApplicationResponseBulkCreateRequest,
  ): Promise<ProjectApplicationResponseItem> {
    return this.post<ProjectApplicationResponseItem>('/v1/application-responses/bulk', request);
  }

  // 지원서 응답 수정
  async updateApplicationResponse(
    id: string,
    request: ProjectApplicationResponseUpdateRequest,
  ): Promise<ProjectApplicationResponseItem> {
    return this.put<ProjectApplicationResponseItem>(`/v1/application-responses/${id}`, request);
  }

  // 지원서 응답 삭제
  async deleteApplicationResponse(id: string): Promise<object> {
    return this.delete<object>(`/v1/application-responses/${id}`);
  }
}

export const applicationResponseApi = new ApplicationResponseApi();
