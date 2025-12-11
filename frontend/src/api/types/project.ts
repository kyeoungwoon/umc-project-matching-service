/**
 * 프로젝트 관련 타입
 */
import type { ChallengerPart } from '@api/types/common';

export interface ProjectTo {
  id: string;
  part: ChallengerPart;
  toCount: number;
}

export interface ProjectMember {
  id: string;
  challengerId: string;
  challengerName: string;
  challengerNickname: string;
  part: ChallengerPart;
  schoolId: string;
  schoolName: string;
  active: boolean;
}

export interface Project {
  id: string;
  name: string;
  description: string;
  productOwnerId: string;
  productOwnerName: string;
  productOwnerNickname: string;
  productOwnerSchool: string;
  chapterId: string;
  chapterName: string;
  logoImageUrl: string;
  bannerImageUrl: string;
  notionLink: string;
  createdAt: string;
  updatedAt: string;
  projectTos: ProjectTo[];
  projectMembers: ProjectMember[];
}

export interface ProjectCreateRequest {
  name: string;
  description: string;
  productOwnerId: string;
  chapterId: string;
  logoImageUrl: string;
  bannerImageUrl: string;
  notionLink: string;
}

export interface ProjectUpdateRequest {
  name: string;
  description: string;
  productOwnerId: string;
  logoImageUrl: string;
  bannerImageUrl: string;
  notionLink: string;
}
