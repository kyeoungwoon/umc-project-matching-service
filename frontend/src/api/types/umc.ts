/**
 * UMC 관련 타입 (학교, 챕터)
 */

export interface School {
  id: string;
  name: string;
  logoImageUrl: string;
  createdAt: string;
  updatedAt: string;
}

export interface SchoolCreateRequest {
  name: string;
  logoImageUrl: string;
}

export interface Chapter {
  id: string;
  name: string;
  description: string;
  gisu: string;
  schools: School[];
  createdAt: string;
  updatedAt: string;
}

export interface ChapterCreateRequest {
  name: string;
  description: string;
  gisu: string;
}
