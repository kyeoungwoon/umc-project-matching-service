/**
 * 프로젝트 TO 관련 타입
 */
import type { ChallengerPart } from '@api/types/common';

export interface ProjectToResponse {
  id: string;
  projectId: string;
  projectName: string;
  part: ChallengerPart;
  toCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface ProjectToCreateRequest {
  projectId: string;
  part: ChallengerPart;
  toCount: number;
}

export interface ProjectToItem {
  part: ChallengerPart;
  toCount: number;
}

export interface ProjectToBulkCreateRequest {
  projectId: string;
  toItems: ProjectToItem[];
}

export interface ProjectToUpdateRequest {
  toCount: number;
}
