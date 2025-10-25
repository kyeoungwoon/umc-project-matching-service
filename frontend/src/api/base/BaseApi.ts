/**
 * Base API Class
 * 모든 API 클래스의 베이스가 되는 추상 클래스
 */
import type { AxiosRequestConfig } from 'axios';

import { apiClient } from '@api/client';
import type { ApiResponse } from '@api/types/common';

/**
 * API 에러 클래스
 * isSuccess: false 또는 HTTP 에러 시 사용
 */
export class ApiError extends Error {
  constructor(
    message: string,
    public code?: string,
    public status?: number,
  ) {
    super(message);
    this.name = 'ApiError';
    Object.setPrototypeOf(this, ApiError.prototype);
  }
}

/**
 * BaseApi 클래스
 * 모든 API 클래스는 이 클래스를 상속받아 구현
 */
export class BaseApi {
  /**
   * GET 요청
   * ApiResponse<T>를 자동으로 언래핑하여 T를 반환
   */
  protected async get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    try {
      const { data } = await apiClient.get<ApiResponse<T>>(url, config);
      return this.unwrapResponse(data);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * POST 요청
   * ApiResponse<T>를 자동으로 언래핑하여 T를 반환
   */
  protected async post<T>(url: string, body?: unknown, config?: AxiosRequestConfig): Promise<T> {
    try {
      const { data } = await apiClient.post<ApiResponse<T>>(url, body, config);
      return this.unwrapResponse(data);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * PUT 요청
   * ApiResponse<T>를 자동으로 언래핑하여 T를 반환
   */
  protected async put<T>(url: string, body?: unknown, config?: AxiosRequestConfig): Promise<T> {
    try {
      const { data } = await apiClient.put<ApiResponse<T>>(url, body, config);
      return this.unwrapResponse(data);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * DELETE 요청
   * ApiResponse<T>를 자동으로 언래핑하여 T를 반환
   */
  protected async delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    try {
      const { data } = await apiClient.delete<ApiResponse<T>>(url, config);
      return this.unwrapResponse(data);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * PATCH 요청
   * ApiResponse<T>를 자동으로 언래핑하여 T를 반환
   */
  protected async patch<T>(url: string, body?: unknown, config?: AxiosRequestConfig): Promise<T> {
    try {
      const { data } = await apiClient.patch<ApiResponse<T>>(url, body, config);
      return this.unwrapResponse(data);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  /**
   * ApiResponse<T>를 언래핑하여 T를 반환
   * isSuccess가 false이면 ApiError를 throw
   */
  private unwrapResponse<T>(response: ApiResponse<T>): T {
    if (!response.isSuccess) {
      throw new ApiError(response.message || 'API request failed', response.code);
    }
    return response.result;
  }

  /**
   * 에러 처리
   * axios 에러를 ApiError로 변환
   */
  private handleError(error: unknown): ApiError {
    if (error instanceof ApiError) {
      return error;
    }

    if (typeof error === 'object' && error !== null && 'response' in error) {
      const axiosError = error as {
        response?: { status?: number; data?: { message?: string; code?: string } };
        message?: string;
      };

      const status = axiosError.response?.status;
      const message = axiosError.response?.data?.message || axiosError.message || 'Unknown error';
      const code = axiosError.response?.data?.code;

      return new ApiError(message, code, status);
    }

    return new ApiError('Unknown error occurred');
  }
}
