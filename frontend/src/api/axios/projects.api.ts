/**
 * 프로젝트 API
 */
import { BaseApi } from '@api/base/BaseApi';
import type { Project, ProjectCreateRequest, ProjectUpdateRequest } from '@api/types/project';

class ProjectApi extends BaseApi {
  // 프로젝트 조회
  async getProject(id: string): Promise<Project> {
    return this.get<Project>(`/v1/projects/${id}`);
  }

  // 프로젝트 목록 조회
  async getProjects(chapterId?: string): Promise<Project[]> {
    const params = chapterId ? { chapterId } : undefined;
    return this.get<Project[]>('/v1/projects', { params });
  }

  // 프로젝트 생성
  async createProject(request: ProjectCreateRequest): Promise<Project> {
    return this.post<Project>('/v1/projects', request);
  }

  // 프로젝트 수정
  async updateProject(id: string, request: ProjectUpdateRequest): Promise<Project> {
    return this.put<Project>(`/v1/projects/${id}`, request);
  }

  // 프로젝트 삭제
  async deleteProject(id: string): Promise<object> {
    return this.delete<object>(`/v1/projects/${id}`);
  }
}

export const projectApi = new ProjectApi();
