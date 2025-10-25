/**
 * 프로젝트 매칭 라운드 API
 */
import { BaseApi } from '@api/base/BaseApi';
import {
  ProjectMatchingRoundCreateRequest,
  ProjectMatchingRoundGetRequest,
  ProjectMatchingRoundResponse,
  ProjectMatchingRoundUpdateRequest,
} from '@api/types/matching-round';

class MatchingRoundApi extends BaseApi {
  /**
   * 매칭 라운드 목록 조회
   */
  async getMatchingRounds(
    request: ProjectMatchingRoundGetRequest,
  ): Promise<ProjectMatchingRoundResponse[]> {
    return this.get<ProjectMatchingRoundResponse[]>('/v1/matching-rounds', {
      params: { ...request },
    });
  }

  /**
   * 매칭 라운드 단건 조회
   */
  async getMatchingRound(id: string): Promise<ProjectMatchingRoundResponse> {
    return this.get<ProjectMatchingRoundResponse>(`/v1/matching-rounds/${id}`);
  }

  /**
   * 매칭 라운드 생성
   */
  async createMatchingRound(
    request: ProjectMatchingRoundCreateRequest,
  ): Promise<ProjectMatchingRoundResponse> {
    return this.post<ProjectMatchingRoundResponse>('/v1/matching-rounds', request);
  }

  /**
   * 매칭 라운드 수정
   */
  async updateMatchingRound(
    id: string,
    request: ProjectMatchingRoundUpdateRequest,
  ): Promise<ProjectMatchingRoundResponse> {
    return this.put<ProjectMatchingRoundResponse>(`/v1/matching-rounds/${id}`, request);
  }

  /**
   * 매칭 라운드 삭제
   */
  async deleteMatchingRound(id: string): Promise<void> {
    return this.delete<void>(`/v1/matching-rounds/${id}`);
  }

  /**
   * 현재 또는 다가오는 매칭 라운드 조회
   */
  async getCurrentOrUpcomingMatchingRound(
    chapterId: string,
  ): Promise<ProjectMatchingRoundResponse> {
    return this.get<ProjectMatchingRoundResponse>('/v1/matching-rounds/current-or-closest', {
      params: { chapterId },
    });
  }

  /**
   * 현재 또는 다가오는 매칭 라운드 조회
   */
  async getCurrentMatchingRound(chapterId: string): Promise<ProjectMatchingRoundResponse> {
    return this.get<ProjectMatchingRoundResponse>('/v1/matching-rounds/current', {
      params: { chapterId },
    });
  }
}

export const matchingRoundApi = new MatchingRoundApi();
