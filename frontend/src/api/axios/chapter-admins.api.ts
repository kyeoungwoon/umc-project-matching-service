/**
 * 챕터 관리자 API
 */
import { BaseApi } from '@api/base/BaseApi';
import type {
  ChapterAdmin,
  ChapterAdminCreateRequest,
  ChapterAdminListParams,
  ChapterAdminRole,
  ChapterAdminRoleParams,
  ChapterAdminUpdateRequest,
} from '@api/types/chapter-admin';

class ChapterAdminApi extends BaseApi {
  // 챕터 관리자 단건 조회
  async getChapterAdmin(id: string): Promise<ChapterAdmin> {
    return this.get<ChapterAdmin>(`/v1/chapter-admins/${id}`);
  }

  // 챕터 관리자 목록 조회
  async getChapterAdmins(params?: ChapterAdminListParams): Promise<ChapterAdmin[]> {
    return this.get<ChapterAdmin[]>('/v1/chapter-admins', { params });
  }

  // 챕터 관리자 생성
  async createChapterAdmin(request: ChapterAdminCreateRequest): Promise<ChapterAdmin> {
    return this.post<ChapterAdmin>('/v1/chapter-admins', request);
  }

  // 챕터 관리자 수정
  async updateChapterAdmin(id: string, request: ChapterAdminUpdateRequest): Promise<ChapterAdmin> {
    return this.put<ChapterAdmin>(`/v1/chapter-admins/${id}`, request);
  }

  // 챕터 관리자 삭제
  async deleteChapterAdmin(id: string): Promise<void> {
    return this.delete<void>(`/v1/chapter-admins/${id}`);
  }

  // 챌린저의 챕터 역할 조회
  async getChapterAdminRole(params: ChapterAdminRoleParams): Promise<ChapterAdminRole> {
    return this.get<ChapterAdminRole>('/v1/chapter-admins/role', { params });
  }
}

export const chapterAdminApi = new ChapterAdminApi();
