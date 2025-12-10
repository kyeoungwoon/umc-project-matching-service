/**
 * 관리자 API 관련 타입
 */

// 프로젝트 멤버 추가 요청
export interface ProjectMemberCreateRequest {
  projectId: number;
  challengerId: number;
  active: boolean;
}

// 프로젝트 멤버 응답 (관리자 API)
export interface AdminProjectMemberResponse {
  id: number;
  projectId: number;
  projectName: string;
  challengerId: number;
  challengerName: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}
