/**
 * 테스트 API
 */
import { BaseApi } from '@api/base/BaseApi';

class TestApi extends BaseApi {
  // 테스트용 토큰 발급
  async getTestToken(challengerId: string): Promise<string> {
    return this.get<string>(`/v1/test/${challengerId}`);
  }
}

export const testApi = new TestApi();
