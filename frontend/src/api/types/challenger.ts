/**
 * 챌린저 관련 타입
 */
import type { ChallengerPart, Gender } from '@api/types/common';

export interface Challenger {
  id: string;
  umsbId: string;
  gisu: string;
  part: ChallengerPart;
  name: string;
  nickname: string;
  gender: Gender;
  schoolId: string;
  schoolName: string;
  schoolLogoImageUrl: string;
  studentId: string;
  profileImageUrl: string;
  chapterId: string;
  chapterName: string;
  createdAt: string;
  updatedAt: string;
}

export interface ChallengerCreateRequest {
  umsbId: string;
  gisu: string;
  part: ChallengerPart;
  name: string;
  nickname: string;
  gender: Gender;
  schoolId: string;
  studentId: string;
  password: string;
  profileImageUrl: string;
}

export interface ChallengerLoginRequest {
  studentId: string;
  schoolId: string;
  gisu: string;
  password: string;
}

export interface ChallengerLoginResponse {
  accessToken: string;
  challengerInfo: Challenger;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}
