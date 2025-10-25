/**
 * 챌린저(회원) API
 */
import { BaseApi } from '@api/base/BaseApi';
import {
  Challenger,
  ChallengerCreateRequest,
  ChallengerLoginRequest,
  ChallengerLoginResponse,
  ChangePasswordRequest,
} from '@api/types/challenger';
import type { Chapter } from '@api/types/umc';

class ChallengerApi extends BaseApi {
  // 회원가입
  async register(request: ChallengerCreateRequest): Promise<Challenger> {
    return this.post<Challenger>('/v1/challenger/register', request);
  }

  // 대량 회원가입
  async registerBulk(requests: ChallengerCreateRequest[]): Promise<Challenger[]> {
    return this.post<Challenger[]>('/v1/challenger/register/bulk', requests);
  }

  // 로그인
  async login(request: ChallengerLoginRequest): Promise<ChallengerLoginResponse> {
    return this.post<ChallengerLoginResponse>('/v1/challenger/login', request);
  }

  // 내 정보 조회
  async getMe(): Promise<Challenger> {
    return this.get<Challenger>('/v1/challenger/me');
  }

  // 챌린저 정보 조회 (ID 또는 UMSB ID)
  async getChallenger(params: { challengerId?: string; umsbId?: string }): Promise<Challenger> {
    return this.get<Challenger>('/v1/challenger', {
      params,
    });
  }

  // 챌린저의 챕터 조회
  async getChallengerChapter(challengerId: string): Promise<Chapter> {
    return this.get<Chapter>('/v1/challenger/chapter', {
      params: { challengerId },
    });
  }

  async changePassword(request: ChangePasswordRequest) {
    return this.post<void>('/v1/challenger/change-password', request);
  }
}

export const challengerApi = new ChallengerApi();
