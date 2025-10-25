/**
 * 프로젝트 지원 관련 타입
 */
import type { ApplicationStatus, ChallengerPart } from '@api/types/common';

export interface ProjectApplication {
  id: string;
  formId: string;
  formTitle: string;
  projectId: string;
  projectName: string;
  applicantId: string;
  applicantName: string;
  applicantNickname: string;
  applicantPart: ChallengerPart;
  applicantSchoolId: string;
  applicantSchoolName: string;
  matchingRoundId: string;
  matchingRoundName: string;
  status: ApplicationStatus;
  createdAt: string;
  updatedAt: string;
}

export interface ProjectApplicationCreateRequest {
  formId: string;
  responses: SingleQuestionResponseDto[];
}

export interface ProjectApplicationUpdateRequest {
  status: ApplicationStatus;
}

export interface ProjectApplicationResponseItem {
  id: string;
  applicationId: string;
  questionId: string;
  questionTitle: string;
  values: string[];
  createdAt: string;
  updatedAt: string;
}

export interface ProjectApplicationResponseCreateRequest {
  applicationId: string;
  questionId: string;
  values: string[];
}

export interface SingleQuestionResponseDto {
  questionId: string;
  values: string[];
}

export interface ProjectApplicationResponseBulkCreateRequest {
  applicationId: string;
  responses: SingleQuestionResponseDto[];
}

export interface ProjectApplicationResponseUpdateRequest {
  values: string[];
}

export interface ProjectApplicationDetail extends ProjectApplication {
  responses: ProjectApplicationResponseItem[];
}

export interface ApplicationBrief {
  applicationId: string;
  projectId: string;
  projectName: string;
  status: ApplicationStatus;
  appliedAt: string;
}

export interface MatchingRoundApplications {
  matchingRoundId: string;
  matchingRoundName: string;
  startAt: string;
  endAt: string;
  application: ApplicationBrief | null;
}

export interface ChallengerApplicationSummary {
  challengerId: string;
  challengerName: string;
  challengerNickname: string;
  challengerPart: ChallengerPart;
  challengerSchoolId: string;
  challengerSchoolName: string;
  matchingRoundApplications: MatchingRoundApplications[];
  memberProjects: {
    projectId: string;
    projectName: string;
    projectDescription: string;
  };
}
