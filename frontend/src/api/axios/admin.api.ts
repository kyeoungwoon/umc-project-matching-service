/**
 * 관리자 API
 */
import { BaseApi } from '@api/base/BaseApi';
import type { AdminProjectMemberResponse, ProjectMemberCreateRequest } from '@api/types/admin';
import type { Challenger } from '@api/types/challenger';

class AdminApi extends BaseApi {
  // 관리자 권한으로 프로젝트 멤버 추가
  async addProjectMember(request: ProjectMemberCreateRequest): Promise<AdminProjectMemberResponse> {
    return this.post<AdminProjectMemberResponse>('/v1/admin/project-member', request);
  }

  // 관리자 권한으로 프로젝트 멤버 삭제
  async deleteProjectMember(projectMemberId: number): Promise<string> {
    return this.delete<string>('/v1/admin/project-member', {
      data: projectMemberId,
    });
  }

  // 이름으로 챌린저 검색
  // Note: API 스펙에서 GET 요청에 requestBody를 사용하지만, params로 전달
  async searchChallengerByName(name: string): Promise<Challenger[]> {
    return this.get<Challenger[]>('/v1/admin/challenger', {
      params: { name },
    });
  }
}

export const adminApi = new AdminApi();
