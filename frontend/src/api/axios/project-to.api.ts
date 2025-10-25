/**
 * 프로젝트 TO API
 */
import { BaseApi } from '@api/base/BaseApi';
import type {
  ProjectToBulkCreateRequest,
  ProjectToCreateRequest,
  ProjectToResponse,
  ProjectToUpdateRequest,
} from '@api/types/project-to';

class ProjectToApi extends BaseApi {
  // 프로젝트 TO 단건 조회
  async getProjectTo(id: string): Promise<ProjectToResponse> {
    return this.get<ProjectToResponse>(`/v1/project-to/${id}`);
  }

  // 프로젝트 TO 목록 조회
  async getProjectTos(projectId?: string): Promise<ProjectToResponse[]> {
    const params = projectId ? { projectId } : undefined;
    return this.get<ProjectToResponse[]>('/v1/project-to', { params });
  }

  // 프로젝트 TO 생성
  async createProjectTo(request: ProjectToCreateRequest): Promise<ProjectToResponse> {
    return this.post<ProjectToResponse>('/v1/project-to', request);
  }

  // 프로젝트 TO 일괄 생성
  async bulkCreateProjectTos(request: ProjectToBulkCreateRequest): Promise<ProjectToResponse[]> {
    return this.post<ProjectToResponse[]>('/v1/project-to/bulk', request);
  }

  // 프로젝트 TO 수정
  async updateProjectTo(
    id: string,
    request: ProjectToUpdateRequest,
  ): Promise<ProjectToResponse> {
    return this.put<ProjectToResponse>(`/v1/project-to/${id}`, request);
  }

  // 프로젝트 TO 삭제
  async deleteProjectTo(id: string): Promise<object> {
    return this.delete<object>(`/v1/project-to/${id}`);
  }
}

export const projectToApi = new ProjectToApi();
