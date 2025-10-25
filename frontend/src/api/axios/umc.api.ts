/**
 * UMC 공통 API (학교, 챕터)
 */
import { BaseApi } from '@api/base/BaseApi';
import type { Chapter, ChapterCreateRequest, School, SchoolCreateRequest } from '@api/types/umc';

class UmcApi extends BaseApi {
  // 학교 목록 조회
  async getSchools(): Promise<School[]> {
    return this.get<School[]>('/v1/umc/school');
  }

  // 학교 생성
  async createSchool(request: SchoolCreateRequest): Promise<School> {
    return this.post<School>('/v1/umc/school', request);
  }

  // 챕터 목록 조회
  async getChapters(): Promise<Chapter[]> {
    return this.get<Chapter[]>('/v1/umc/chapter');
  }

  // 챕터 생성
  async createChapter(request: ChapterCreateRequest): Promise<Chapter> {
    return this.post<Chapter>('/v1/umc/chapter', request);
  }

  // 테스트용 - Leo 전체 생성
  async createLeoAll(): Promise<string> {
    return this.post<string>('/v1/umc/leo/all');
  }
}

export const umcApi = new UmcApi();
