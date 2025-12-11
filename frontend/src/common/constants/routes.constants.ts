export const ROUTES = {
  HOME: '/home',
  AUTH: {
    LOGIN: '/auth/login',
  },
  PROJECTS: {
    LIST: '/projects', // 프로젝트 목록 보기
    CREATE: '/projects/create', // 프로젝트 생성하기
    VIEW_APPLICANTS: (projectId: string, formId: string) =>
      `/projects/${projectId}/forms/${formId}/applicants`, // 프로젝트 지원자 목록 보기
    // 지원자용, 프로젝트가 가지고 있는 폼 목록 조회
    FORM_LIST: (projectId: string) => `/projects/${projectId}/forms`,
    // Plan용, 프로젝트 폼 조회 및 생성
    CREATE_FORM: (projectId: string) => `/projects/${projectId}/forms/create`,
    MY_PROJECTS: '/projects/my', // 내가 만든 프로젝트 목록 보기
    // Plan용, 폼 수정하기
    EDIT_FORM: (projectId: string, formId: string) => `/projects/${projectId}/forms/${formId}/edit`,
    // 폼에 지원하기
    APPLY_TO_FORM: (projectId: string, formId: string) =>
      `/projects/${projectId}/forms/${formId}/apply`,
    MASTERSHEET: '/projects/mastersheet',
  },
  MY: {
    INFO: '/my/info',
    SETTINGS: '/my/settings',
    APPLICATIONS: '/my/applications',
    CHANGE_PASSWORD: '/my/change-password',
  },
  ADMIN: {
    DASHBOARD: '/admin/dashboard',
    MATCHING_ROUNDS: '/admin/matching-rounds',
    PROJECTS: '/admin/project',
    CHALLENGER: '/admin/challenger',
    PROJECT_EDIT: (projectId: string) => `/admin/project/${projectId}`,
  },
} as const;
