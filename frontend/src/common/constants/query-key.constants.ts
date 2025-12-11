/**
 * Query key store
 * @lukemorales/query-key-factory 기반으로 모든 Query Key를 중앙에서 관리합니다.
 */
import { createQueryKeyStore } from '@lukemorales/query-key-factory';

export const queryKeyStore = createQueryKeyStore({
  applications: {
    list: null,
    form: (formId: string) => [formId],
    detail: (id: string) => [id],
    chapterSummary: (chapterId: string) => ['chapter-summary', chapterId],
    myApplications: null,
  },
  application: {
    singleApplication: (applicationId: string, projectId: string) => [applicationId, { projectId }],
  },
  applicationResponses: {
    list: (applicationId?: string) => [applicationId ?? 'all'],
    detail: (id: string) => [id],
  },
  chapterSchools: {
    list: (params?: { chapterId?: string }) => [params],
    detail: (id: string) => [id],
  },
  projects: {
    list: (chapterId?: string) => [chapterId ?? 'all'],
    detail: (id: string) => [id],
  },
  projectTos: {
    list: (projectId?: string) => [projectId ?? 'all'],
    detail: (id: string) => [id],
  },
  forms: {
    list: (projectId?: string) => [projectId ?? 'all'],
    detail: (id: string) => [id],
  },
  umc: {
    schools: null,
    chapters: null,
  },
  challengers: {
    me: null,
    info: (params: { challengerId?: string; umsbId?: string }) => [params],
    chapter: (challengerId: string) => [challengerId],
  },
  me: {
    info: null,
  },
  chapterAdmins: {
    list: (params?: { chapterId?: string; challengerId?: string }) => [params],
    detail: (id: string) => [id],
    role: (params: { chapterId: string; challengerId: string }) => [params],
  },
  questions: {
    list: (formId?: string) => [formId ?? 'all'],
    detail: (id: string) => [id],
  },
  test: {
    token: (challengerId: string) => [challengerId],
  },
  matchingRounds: {
    list: (params?: { chapterId?: string; startTime?: string; endTime?: string }) => [params],
    detail: (id: string) => [id],
    currentOrClosest: (chapterId: string) => [chapterId],
    current: (chapterId: string) => [chapterId],
    byTimeRange: (params: { chapterId: string; startTime: string; endTime: string }) => [params],
  },
  s3: {
    file: (fileId: number) => [fileId],
  },
  admin: {
    challengerSearch: (name: string) => ['search', name],
  },
});

// 프로젝트 지원 Query Keys
export const applicationQueryKeys = {
  all: queryKeyStore.applications._def,
  my: () => queryKeyStore.applications.myApplications.queryKey,
  lists: () => queryKeyStore.applications.list.queryKey,
  list: () => queryKeyStore.applications.list.queryKey,
  formLists: () => queryKeyStore.applications.form._def,
  formList: (formId: string) => queryKeyStore.applications.form(formId).queryKey,
  chapterSummaries: () => queryKeyStore.applications.chapterSummary._def,
  chapterSummary: (chapterId: string) =>
    queryKeyStore.applications.chapterSummary(chapterId).queryKey,
  details: () => queryKeyStore.applications.detail._def,
  detail: (id: string) => queryKeyStore.applications.detail(id).queryKey,
};

// 프로젝트 지원 응답 Query Keys
export const applicationResponseQueryKeys = {
  all: queryKeyStore.applicationResponses._def,
  lists: () => queryKeyStore.applicationResponses.list._def,
  list: (applicationId?: string) => queryKeyStore.applicationResponses.list(applicationId).queryKey,
  details: () => queryKeyStore.applicationResponses.detail._def,
  detail: (id: string) => queryKeyStore.applicationResponses.detail(id).queryKey,
};

// 챕터-학교 매핑 Query Keys
export const chapterSchoolQueryKeys = {
  all: queryKeyStore.chapterSchools._def,
  lists: () => queryKeyStore.chapterSchools.list._def,
  list: (params?: { chapterId?: string }) => queryKeyStore.chapterSchools.list(params).queryKey,
  details: () => queryKeyStore.chapterSchools.detail._def,
  detail: (id: string) => queryKeyStore.chapterSchools.detail(id).queryKey,
};

// 프로젝트 Query Keys
export const projectQueryKeys = {
  all: queryKeyStore.projects._def,
  lists: () => queryKeyStore.projects.list._def,
  list: (chapterId?: string) => queryKeyStore.projects.list(chapterId).queryKey,
  details: () => queryKeyStore.projects.detail._def,
  detail: (id: string) => queryKeyStore.projects.detail(id).queryKey,
};

// 프로젝트 TO Query Keys
export const projectToQueryKeys = {
  all: queryKeyStore.projectTos._def,
  lists: () => queryKeyStore.projectTos.list._def,
  list: (projectId?: string) => queryKeyStore.projectTos.list(projectId).queryKey,
  details: () => queryKeyStore.projectTos.detail._def,
  detail: (id: string) => queryKeyStore.projectTos.detail(id).queryKey,
};

// 프로젝트 지원 폼 Query Keys
export const formQueryKeys = {
  all: queryKeyStore.forms._def,
  lists: () => queryKeyStore.forms.list._def,
  list: (projectId?: string) => queryKeyStore.forms.list(projectId).queryKey,
  details: () => queryKeyStore.forms.detail._def,
  detail: (id: string) => queryKeyStore.forms.detail(id).queryKey,
};

// UMC 공통 Query Keys (학교, 챕터)
export const umcQueryKeys = {
  all: queryKeyStore.umc._def,
  schools: () => queryKeyStore.umc.schools.queryKey,
  chapters: () => queryKeyStore.umc.chapters.queryKey,
};

// 챌린저(회원) Query Keys
export const challengerQueryKeys = {
  all: queryKeyStore.challengers._def,
  me: () => queryKeyStore.challengers.me.queryKey,
  info: (params: { challengerId?: string; umsbId?: string }) =>
    queryKeyStore.challengers.info(params).queryKey,
  chapter: (challengerId: string) => queryKeyStore.challengers.chapter(challengerId).queryKey,
};

// 챕터 관리자 Query Keys
export const chapterAdminQueryKeys = {
  all: queryKeyStore.chapterAdmins._def,
  lists: () => queryKeyStore.chapterAdmins.list._def,
  list: (params?: { chapterId?: string; challengerId?: string }) =>
    queryKeyStore.chapterAdmins.list(params).queryKey,
  details: () => queryKeyStore.chapterAdmins.detail._def,
  detail: (id: string) => queryKeyStore.chapterAdmins.detail(id).queryKey,
  roles: () => queryKeyStore.chapterAdmins.role._def,
  role: (params: { chapterId: string; challengerId: string }) =>
    queryKeyStore.chapterAdmins.role(params).queryKey,
};

// 지원서 질문 Query Keys
export const questionQueryKeys = {
  all: queryKeyStore.questions._def,
  lists: () => queryKeyStore.questions.list._def,
  list: (formId?: string) => queryKeyStore.questions.list(formId).queryKey,
  details: () => queryKeyStore.questions.detail._def,
  detail: (id: string) => queryKeyStore.questions.detail(id).queryKey,
};

// 테스트 Query Keys
export const testQueryKeys = {
  all: queryKeyStore.test._def,
  token: (challengerId: string) => queryKeyStore.test.token(challengerId).queryKey,
};

// 프로젝트 매칭 라운드 Query Keys
export const matchingRoundQueryKeys = {
  all: queryKeyStore.matchingRounds._def,
  lists: () => queryKeyStore.matchingRounds.list._def,
  list: (params?: { chapterId?: string; startTime?: string; endTime?: string }) =>
    queryKeyStore.matchingRounds.list(params).queryKey,
  details: () => queryKeyStore.matchingRounds.detail._def,
  detail: (id: string) => queryKeyStore.matchingRounds.detail(id).queryKey,
  currentOrClosest: (chapterId: string) =>
    queryKeyStore.matchingRounds.currentOrClosest(chapterId).queryKey,
  current: (chapterId: string) => queryKeyStore.matchingRounds.current(chapterId).queryKey,
  byTimeRange: (params: { chapterId: string; startTime: string; endTime: string }) =>
    queryKeyStore.matchingRounds.byTimeRange(params).queryKey,
};

// S3 파일 Query Keys
export const s3QueryKeys = {
  all: queryKeyStore.s3._def,
  files: () => queryKeyStore.s3.file._def,
  file: (fileId: number) => queryKeyStore.s3.file(fileId).queryKey,
};

// 관리자 Query Keys
export const adminQueryKeys = {
  all: queryKeyStore.admin._def,
  challengerSearches: () => queryKeyStore.admin.challengerSearch._def,
  challengerSearch: (name: string) => queryKeyStore.admin.challengerSearch(name).queryKey,
};
