/**
 * Axios 클라이언트 설정
 */
import axios from 'axios';

import AuthStore from '@features/auth/stores/auth-store';

const BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

export const apiClient = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor - 토큰 추가
apiClient.interceptors.request.use(
  (config) => {
    // 로컬스토리지나 쿠키에서 토큰 가져오기
    const { user } = AuthStore.getState();
    if (user?.accessToken) {
      config.headers.Authorization = `Bearer ${user.accessToken}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// Response interceptor - 에러 처리
apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // 401 에러 시 로그인 페이지로 리다이렉트 등의 처리
    // console.error('API 요청 중 오류가 발생했습니다:', error.response);
    if (error.response?.status === 401 && error?.response?.data?.code.startsWith('JWT')) {
      console.error('JWT 토큰 관련 오류가 발생하였습니다.');
      // 토큰 제거
      AuthStore.getState().actions.clearUser();
      // 필요시 로그인 페이지로 리다이렉트
      // window.location.href = '/auth/login';
    }

    if (error?.response?.message) {
      return Promise.reject(error.response.message);
    }

    return Promise.reject(error);
  },
);
