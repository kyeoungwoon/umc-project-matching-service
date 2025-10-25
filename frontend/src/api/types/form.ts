/**
 * 프로젝트 지원 폼 관련 타입
 */

export interface ProjectApplicationForm {
  id: string;
  projectId: string;
  projectName: string;
  title: string;
  description: string;
  createdAt: string;
  updatedAt: string;
}

export interface ProjectApplicationFormCreateRequest {
  projectId: string;
  title: string;
  description: string;
}

export interface ProjectApplicationFormUpdateRequest {
  title: string;
  description: string;
}
