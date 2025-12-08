/**
 * 공통 API 응답 타입
 */

export interface ApiResponse<T> {
  isSuccess: boolean;
  code: string;
  message: string;
  result: T;
}

export interface ApiResponseVoid {
  isSuccess: boolean;
  code: string;
  message: string;
  result: object;
}

export const ChallengerPartEnum = {
  PLAN: 'PLAN',
  DESIGN: 'DESIGN',
  WEB: 'WEB',
  ANDROID: 'ANDROID',
  IOS: 'IOS',
  SPRINGBOOT: 'SPRINGBOOT',
  NODEJS: 'NODEJS',
  NO_PART: 'NO_PART',
} as const;
export type ChallengerPart = (typeof ChallengerPartEnum)[keyof typeof ChallengerPartEnum];

export const GenderEnum = {
  MALE: 'MALE',
  FEMALE: 'FEMALE',
} as const;
export type Gender = (typeof GenderEnum)[keyof typeof GenderEnum];

export const QuestionTypeEnum = {
  SINGLE_CHOICE: 'SINGLE_CHOICE',
  MULTIPLE_CHOICE: 'MULTIPLE_CHOICE',
  SUBJECTIVE: 'SUBJECTIVE',
  FILE: 'FILE',
} as const;
export type QuestionType = (typeof QuestionTypeEnum)[keyof typeof QuestionTypeEnum];

export const ApplicationStatusEnum = {
  PENDING: 'PENDING',
  CONFIRMED: 'CONFIRMED',
  REJECTED: 'REJECTED',
} as const;
export type ApplicationStatus = (typeof ApplicationStatusEnum)[keyof typeof ApplicationStatusEnum];

export const ChapterAdminRoleEnum = {
  CENTRAL_LEAD: 'CENTRAL_LEAD',
  CENTRAL_DEPARTMENT_LEAD: 'CENTRAL_DEPARTMENT_LEAD',
  CENTRAL_ADMIN: 'CENTRAL_ADMIN',
  CHAPTER_LEAD: 'CHAPTER_LEAD',
  SCHOOL_LEAD: 'SCHOOL_LEAD',
  SCHOOL_VICE_LEAD: 'SCHOOL_VICE_LEAD',
  SCHOOL_ADMIN: 'SCHOOL_ADMIN',
} as const;
export type ChapterAdminRoleType = (typeof ChapterAdminRoleEnum)[keyof typeof ChapterAdminRoleEnum];
