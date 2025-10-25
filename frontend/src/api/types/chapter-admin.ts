/**
 * 챕터 관리자 타입 정의
 */
import { ChapterAdminRoleType } from '@api/types/common';

export interface ChapterAdmin {
  id: string;
  chapterId: string;
  chapterName: string;
  challengerId: string;
  challengerName: string;
  challengerNickname: string;
  role: ChapterAdminRoleType;
  createdAt: string;
  updatedAt: string;
}

export interface ChapterAdminCreateRequest {
  chapterId: string;
  challengerId: string;
  role: ChapterAdminRoleType;
}

export interface ChapterAdminUpdateRequest {
  role: ChapterAdminRole;
}

export interface ChapterAdminListParams {
  chapterId?: string;
  challengerId?: string;
}

export interface ChapterAdminRoleParams {
  chapterId: string;
  challengerId: string;
}

export interface ChapterAdminRole {
  chapterId: string;
  challengerId: string;
  role: ChapterAdminRoleType | null;
}
