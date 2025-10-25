/**
 * 프로젝트 지원 폼 API
 */
import { BaseApi } from '@api/base/BaseApi';
import type {
  ProjectApplicationForm,
  ProjectApplicationFormCreateRequest,
  ProjectApplicationFormUpdateRequest,
} from '@api/types/form';

class FormApi extends BaseApi {
  // 지원서 폼 조회
  async getForm(id: string): Promise<ProjectApplicationForm> {
    return this.get<ProjectApplicationForm>(`/v1/form/${id}`);
  }

  // 지원서 폼 목록 조회
  async getForms(projectId?: string): Promise<ProjectApplicationForm[]> {
    const params = projectId ? { projectId } : undefined;
    return this.get<ProjectApplicationForm[]>('/v1/form', { params });
  }

  // 지원서 폼 생성
  async createForm(request: ProjectApplicationFormCreateRequest): Promise<ProjectApplicationForm> {
    return this.post<ProjectApplicationForm>('/v1/form', request);
  }

  // 지원서 폼 수정
  async updateForm(
    id: string,
    request: ProjectApplicationFormUpdateRequest,
  ): Promise<ProjectApplicationForm> {
    return this.put<ProjectApplicationForm>(`/v1/form/${id}`, request);
  }

  // 지원서 폼 삭제
  async deleteForm(id: string): Promise<object> {
    return this.delete<object>(`/v1/form/${id}`);
  }
}

export const formApi = new FormApi();
