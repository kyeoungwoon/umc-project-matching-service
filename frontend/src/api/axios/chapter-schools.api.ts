/**
 * 챕터-학교 매핑 API
 */
import { BaseApi } from '@api/base/BaseApi';
import type {
  ChapterSchool,
  ChapterSchoolCreateRequest,
  ChapterSchoolListParams,
  ChapterSchoolUpdateRequest,
} from '@api/types/chapter-school';

class ChapterSchoolApi extends BaseApi {
  // 챕터-학교 목록 조회
  async getChapterSchools(params?: ChapterSchoolListParams): Promise<ChapterSchool[]> {
    return this.get<ChapterSchool[]>('/v1/chapter-schools', { params });
  }

  // 챕터-학교 단건 조회
  async getChapterSchool(id: string): Promise<ChapterSchool> {
    return this.get<ChapterSchool>(`/v1/chapter-schools/${id}`);
  }

  // 챕터-학교 생성
  async createChapterSchool(request: ChapterSchoolCreateRequest): Promise<ChapterSchool> {
    return this.post<ChapterSchool>('/v1/chapter-schools', request);
  }

  // 챕터-학교 리더 수정
  async updateChapterSchool(
    id: string,
    request: ChapterSchoolUpdateRequest,
  ): Promise<ChapterSchool> {
    return this.put<ChapterSchool>(`/v1/chapter-schools/${id}`, request);
  }

  // 챕터-학교 삭제
  async deleteChapterSchool(id: string): Promise<object> {
    return this.delete<object>(`/v1/chapter-schools/${id}`);
  }
}

export const chapterSchoolApi = new ChapterSchoolApi();
