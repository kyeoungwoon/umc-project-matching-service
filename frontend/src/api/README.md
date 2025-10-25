# API 구조 문서

OpenAPI 명세를 기반으로 생성된 Axios API 함수 및 TanStack Query 훅입니다.

## 📁 폴더 구조

```
src/api/
├── types/                      # 타입 정의
│   ├── common.types.ts         # 공통 타입 (ApiResponse, Enum, 기본 응답 타입)
│   ├── projects.types.ts       # 프로젝트 관련 타입
│   ├── forms.types.ts          # 지원서 폼 관련 타입
│   ├── questions.types.ts      # 지원서 질문 관련 타입
│   ├── applications.types.ts   # 지원서 관련 타입
│   ├── challengers.types.ts    # 챌린저 관련 타입
│   ├── umc.types.ts            # UMC(학교, 챕터) 관련 타입
│   └── index.ts                # 타입 통합 export
│
├── axios/                      # Axios API 함수
│   ├── index.ts                # Axios 인스턴스 및 인터셉터 설정
│   ├── projects.ts             # 프로젝트 API
│   ├── forms.ts                # 지원서 폼 API
│   ├── questions.ts            # 지원서 질문 API
│   ├── applications.ts         # 지원서 API
│   ├── challengers.ts          # 챌린저 API
│   ├── umc.ts                  # UMC API
│   └── test.ts                 # 테스트 API
│
└── query/                      # TanStack Query 훅
    ├── keys.ts                 # Query Key Factory
    ├── use-projects.ts         # 프로젝트 훅
    ├── use-forms.ts            # 지원서 폼 훅
    ├── use-questions.ts        # 지원서 질문 훅
    ├── use-applications.ts     # 지원서 훅
    ├── use-challengers.ts      # 챌린저 훅
    ├── use-umc.ts              # UMC 훅
    ├── use-test.ts             # 테스트 훅
    └── index.ts                # Query 훅 통합 export
```

## 🎯 사용 방법

### 1. 타입 import

```typescript
import type {
  ProjectResponse,
  ProjectCreateRequest,
  ChallengerResponse,
  ApplicationStatus,
} from '@/api/types';
```

### 2. Axios API 직접 사용 (권장하지 않음)

```typescript
import {projectsApi} from '@/api/axios';

// 프로젝트 목록 조회
const projects = await projectsApi.getProjects();

// 프로젝트 생성
const newProject = await projectsApi.createProject({
  name: '프로젝트명',
  productOwnerId: 1,
  chapterId: 1,
});
```

### 3. TanStack Query 훅 사용 (권장)

```typescript
import {
  useProjects,
  useProject,
  useCreateProject,
  useUpdateProject,
  useDeleteProject,
} from '@/api/query';

function ProjectList() {
  // 프로젝트 목록 조회
  const {data: projects, isLoading, error} = useProjects();
  
  // 프로젝트 생성
  const createProjectMutation = useCreateProject();
  
  const handleCreate = async () => {
    await createProjectMutation.mutateAsync({
      name: '새 프로젝트',
      productOwnerId: 1,
      chapterId: 1,
    });
  };
  
  return (
      <div>
          {isLoading && <div>로딩
  중
...
  </div>}
  {
    error && <div>에러
    발생 < /div>}
    {
      projects?.map((project) => (
          <div key = {project.id} > {project.name} < /div>
      ))
    }
    </div>
  )
    ;
  }
```

## 📚 도메인별 API 및 훅

### 프로젝트 (Projects)

**API 함수** (`@/api/axios/projects`)

- `getProjects()` - 프로젝트 목록 조회
- `getProject(id)` - 프로젝트 상세 조회
- `createProject(data)` - 프로젝트 생성
- `updateProject(id, data)` - 프로젝트 수정
- `deleteProject(id)` - 프로젝트 삭제

**Query 훅** (`@/api/query`)

- `useProjects()` - 프로젝트 목록 조회
- `useProject(id)` - 프로젝트 상세 조회
- `useCreateProject()` - 프로젝트 생성
- `useUpdateProject()` - 프로젝트 수정
- `useDeleteProject()` - 프로젝트 삭제

### 지원서 폼 (Forms)

**API 함수** (`@/api/axios/forms`)

- `getForms()` - 지원서 폼 목록 조회
- `getForm(id)` - 지원서 폼 상세 조회
- `createForm(data)` - 지원서 폼 생성
- `updateForm(id, data)` - 지원서 폼 수정
- `deleteForm(id)` - 지원서 폼 삭제

**Query 훅** (`@/api/query`)

- `useForms()` - 지원서 폼 목록 조회
- `useForm(id)` - 지원서 폼 상세 조회
- `useCreateForm()` - 지원서 폼 생성
- `useUpdateForm()` - 지원서 폼 수정
- `useDeleteForm()` - 지원서 폼 삭제

### 지원서 질문 (Questions)

**API 함수** (`@/api/axios/questions`)

- `getQuestions()` - 질문 목록 조회
- `getQuestion(id)` - 질문 상세 조회
- `createQuestion(data)` - 질문 생성
- `createQuestionsBulk(data)` - 질문 대량 생성
- `updateQuestion(id, data)` - 질문 수정
- `deleteQuestion(id)` - 질문 삭제

**Query 훅** (`@/api/query`)

- `useQuestions()` - 질문 목록 조회
- `useQuestion(id)` - 질문 상세 조회
- `useCreateQuestion()` - 질문 생성
- `useCreateQuestionsBulk()` - 질문 대량 생성
- `useUpdateQuestion()` - 질문 수정
- `useDeleteQuestion()` - 질문 삭제

### 지원서 (Applications)

**API 함수** (`@/api/axios/applications`)

- `getApplications()` - 지원서 목록 조회
- `getApplication(id)` - 지원서 상세 조회
- `createApplication(data)` - 지원서 제출
- `updateApplication(id, data)` - 지원서 상태 수정
- `deleteApplication(id)` - 지원서 삭제

**Query 훅** (`@/api/query`)

- `useApplications()` - 지원서 목록 조회
- `useApplication(id)` - 지원서 상세 조회
- `useCreateApplication()` - 지원서 제출
- `useUpdateApplication()` - 지원서 상태 수정
- `useDeleteApplication()` - 지원서 삭제

### 챌린저 (Challengers)

**API 함수** (`@/api/axios/challengers`)

- `register(data)` - 회원가입
- `registerBulk(data)` - 대량 회원가입
- `login(data)` - 로그인
- `getMe()` - 내 정보 조회

**Query 훅** (`@/api/query`)

- `useRegister()` - 회원가입
- `useRegisterBulk()` - 대량 회원가입
- `useLogin()` - 로그인
- `useMe()` - 내 정보 조회

### UMC (Schools & Chapters)

**API 함수** (`@/api/axios/umc`)

- `getSchools()` - 학교 목록 조회
- `createSchool(data)` - 학교 생성
- `getChapters()` - 챕터 목록 조회
- `createChapter(data)` - 챕터 생성
- `createLeoAll()` - 대량 운영진 생성 (테스트용)

**Query 훅** (`@/api/query`)

- `useSchools()` - 학교 목록 조회
- `useCreateSchool()` - 학교 생성
- `useChapters()` - 챕터 목록 조회
- `useCreateChapter()` - 챕터 생성
- `useCreateLeoAll()` - 대량 운영진 생성 (테스트용)

### 테스트 (Test)

**API 함수** (`@/api/axios/test`)

- `getTestToken(challengerId)` - 테스트용 토큰 발급

**Query 훅** (`@/api/query`)

- `useGetTestToken()` - 테스트용 토큰 발급

## 🔑 Query Key 구조

Query Key Factory를 통해 일관된 캐시 관리:

```typescript
import {queryKeys} from '@/api/query';

// 프로젝트
queryKeys.projects.all            // ['projects']
queryKeys.projects.lists()        // ['projects', 'list']
queryKeys.projects.detail(1)      // ['projects', 'detail', 1]

// 지원서 폼
queryKeys.forms.all               // ['forms']
queryKeys.forms.lists()           // ['forms', 'list']
queryKeys.forms.detail(1)         // ['forms', 'detail', 1]

// 챌린저
queryKeys.challengers.me()        // ['challengers', 'me']

// UMC
queryKeys.umc.schools.lists()     // ['umc', 'schools', 'list']
queryKeys.umc.chapters.lists()    // ['umc', 'chapters', 'list']
```

## 🔄 캐시 무효화 전략

모든 Mutation 훅은 자동으로 관련 Query 캐시를 무효화합니다:

- **생성**: 목록 캐시 무효화
- **수정**: 상세 + 목록 캐시 무효화
- **삭제**: 상세 캐시 제거 + 목록 캐시 무효화

예시:

```typescript
const updateProjectMutation = useUpdateProject();

// 프로젝트 수정 시 자동으로:
// 1. queryKeys.projects.detail(id) 무효화
// 2. queryKeys.projects.lists() 무효화
await updateProjectMutation.mutateAsync({
  id: 1,
  data: {name: '수정된 이름'},
});
```

## 🎨 공통 Enum 타입

```typescript
// Part (파트)
enum Part {
  PLAN = 'PLAN',
  DESIGN = 'DESIGN',
  WEB = 'WEB',
  ANDROID = 'ANDROID',
  IOS = 'IOS',
  SPRINGBOOT = 'SPRINGBOOT',
  NODEJS = 'NODEJS',
  NO_PART = 'NO_PART',
}

// Gender (성별)
enum Gender {
  MALE = 'MALE',
  FEMALE = 'FEMALE',
}

// QuestionType (질문 유형)
enum QuestionType {
  SINGLE_CHOICE = 'SINGLE_CHOICE',
  MULTIPLE_CHOICE = 'MULTIPLE_CHOICE',
  SUBJECTIVE = 'SUBJECTIVE',
}

// ApplicationStatus (지원 상태)
enum ApplicationStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  REJECTED = 'REJECTED',
}
```

## ⚙️ API 응답 구조

모든 API는 공통 응답 구조를 사용하지만, Axios 함수에서 자동으로 `result` 추출:

```typescript
// 실제 API 응답
{
  "isSuccess"
:
  true,
      "code"
:
  "SUCCESS",
      "message"
:
  "성공",
      "result"
:
  { /* 실제 데이터 */
  }
}

// Axios 함수 반환값 (result만 추출)
{ /* 실제 데이터 */
}
```

## 🔐 인증

- `Authorization` 헤더는 `api` 인스턴스의 request interceptor에서 자동 추가
- JWT 토큰은 `AuthStore`에서 관리
- JWT 에러 발생 시 자동으로 사용자 정보 초기화

## 📝 예제

### 프로젝트 CRUD

```typescript
import {
  useProjects,
  useProject,
  useCreateProject,
  useUpdateProject,
  useDeleteProject,
} from '@/api/query';

function ProjectManagement() {
  const {data: projects} = useProjects();
  const {data: project} = useProject(1);
  
  const createMutation = useCreateProject();
  const updateMutation = useUpdateProject();
  const deleteMutation = useDeleteProject();
  
  const handleCreate = () => {
    createMutation.mutate({
      name: '새 프로젝트',
      productOwnerId: 1,
      chapterId: 1,
    });
  };
  
  const handleUpdate = () => {
    updateMutation.mutate({
      id: 1,
      data: {name: '수정된 프로젝트'},
    });
  };
  
  const handleDelete = () => {
    deleteMutation.mutate(1);
  };
  
  return <div>
...
  </div>;
}
```

### 로그인 및 내 정보 조회

```typescript
import {useLogin, useMe} from '@/api/query';

function LoginPage() {
  const loginMutation = useLogin();
  const {data: me} = useMe();
  
  const handleLogin = async () => {
    const result = await loginMutation.mutateAsync({
      studentId: '20241234',
      schoolId: 1,
      gisu: 8,
      password: 'password123',
    });
    
    // result.accessToken과 result.challengerInfo 사용 가능
    console.log('Access Token:', result.accessToken);
  };
  
  return <div>
...
  </div>;
}
```

### 지원서 폼 및 질문 생성

```typescript
import {
  useCreateForm,
  useCreateQuestionsBulk,
} from '@/api/query';

function FormCreation() {
  const createFormMutation = useCreateForm();
  const createQuestionsMutation = useCreateQuestionsBulk();
  
  const handleCreateFormWithQuestions = async () => {
    // 1. 폼 생성
    const form = await createFormMutation.mutateAsync({
      projectId: 1,
      title: '7기 프로젝트 지원서',
      description: '프로젝트 지원을 위한 지원서입니다',
    });
    
    // 2. 질문 대량 생성
    await createQuestionsMutation.mutateAsync({
      formId: form.id,
      questions: [
        {
          questionNo: 1,
          title: '지원 동기를 작성해주세요',
          type: 'SUBJECTIVE',
          required: true,
        },
        {
          questionNo: 2,
          title: '선호하는 직무를 선택해주세요',
          type: 'SINGLE_CHOICE',
          options: ['백엔드', '프론트엔드', '디자인'],
          required: true,
        },
      ],
    });
  };
  
  return <div>
...
  </div>;
}
```

## 🚀 확장 가능성

추후 필요 시 다음 기능을 추가할 수 있습니다:

1. **Pagination 지원**: Query 파라미터 추가
2. **Filter 지원**: 목록 조회 시 필터 옵션
3. **Optimistic Update**: UI 즉시 업데이트
4. **에러 핸들링**: 공통 에러 토스트 메시지
5. **Retry 전략**: 실패한 요청 재시도

## 📄 참고

- 모든 타입은 OpenAPI 명세 기반으로 자동 생성되었습니다
- 타입 중복을 최소화하기 위해 공통 타입은 `common.types.ts`에 정의했습니다
- Query Key는 Factory 패턴을 사용하여 일관성을 보장합니다

