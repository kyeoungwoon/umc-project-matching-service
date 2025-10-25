/**
 * 챕터-학교 매핑 관련 타입
 */
export interface ChapterSchool {
  id: string;
  chapterId: string;
  chapterName: string;
  schoolId: string;
  schoolName: string;
  leaderId: string;
  leaderName: string;
  viceLeaderId: string;
  viceLeaderName: string;
  createdAt: string;
  updatedAt: string;
}

export interface ChapterSchoolCreateRequest {
  chapterId: string;
  schoolId: string;
  leaderId: string;
  viceLeaderId: string;
}

export interface ChapterSchoolUpdateRequest {
  leaderId: string;
  viceLeaderId: string;
}

export interface ChapterSchoolListParams {
  chapterId?: string;
}
