/**
 * 지원서 질문 관련 타입
 */
import type { QuestionType } from '@api/types/common';

export interface ApplicationFormQuestion {
  id: string;
  formId: string;
  formTitle: string;
  questionNo: number;
  title: string;
  description: string;
  type: QuestionType;
  options: string[];
  required: boolean;
  deleted: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface QuestionItem {
  questionNo: number;
  title: string;
  description?: string;
  type: QuestionType;
  options: string[];
  required: boolean;
}

export interface ApplicationFormQuestionCreateRequest {
  formId: string;
  questionNo: number;
  title: string;
  description: string;
  type: QuestionType;
  options: string[];
  required: boolean;
}

export interface ApplicationFormQuestionUpdateRequest {
  questionNo: number;
  title: string;
  description: string;
  type: QuestionType;
  options: string[];
  required: boolean;
  deleted: boolean;
}

export interface ApplicationFormQuestionBulkCreateRequest {
  formId: string;
  questions: QuestionItem[];
}
