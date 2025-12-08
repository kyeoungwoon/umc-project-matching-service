package kr.kyeoungwoon.upms.domain.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChallengerDto;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterAdminDto;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterDto;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterSchoolDto;
import kr.kyeoungwoon.upms.domain.challenger.dto.SchoolDto;
import kr.kyeoungwoon.upms.domain.challenger.service.ChallengerService;
import kr.kyeoungwoon.upms.domain.challenger.service.ChapterAdminService;
import kr.kyeoungwoon.upms.domain.challenger.service.ChapterSchoolService;
import kr.kyeoungwoon.upms.domain.challenger.service.ChapterService;
import kr.kyeoungwoon.upms.domain.challenger.service.SchoolService;
import kr.kyeoungwoon.upms.domain.project.dto.ProjectDto;
import kr.kyeoungwoon.upms.domain.project.dto.ProjectToDto;
import kr.kyeoungwoon.upms.domain.project.service.ProjectService;
import kr.kyeoungwoon.upms.domain.project.service.ProjectToService;
import kr.kyeoungwoon.upms.domain.test.dummy.DummyProjects;
import kr.kyeoungwoon.upms.domain.test.dummy.DummySchools;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import kr.kyeoungwoon.upms.global.apiPayload.exception.DomainException;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;
import kr.kyeoungwoon.upms.global.enums.ChapterAdminRole;
import kr.kyeoungwoon.upms.global.enums.Gender;
import kr.kyeoungwoon.upms.security.JwtTokenProvider;
import kr.kyeoungwoon.upms.security.UserPrincipal;
import kr.kyeoungwoon.upms.security.annotation.ChapterLeadOnly;
import kr.kyeoungwoon.upms.security.annotation.Public;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "테스트", description = "Crying Developer")
@RestController
@RequiredArgsConstructor
@Profile("local | dev")
@Public
@RequestMapping("/v1/test")
public class TestController {

  private final JwtTokenProvider jwtTokenProvider;
  private final ChallengerService challengerService;
  private final SchoolService schoolService;
  private final ChapterService chapterService;
  private final ChapterSchoolService chapterSchoolService;
  private final ChapterAdminService chapterAdminService;
  private final ProjectService projectService;
  private final ProjectToService projectToService;

  //  @AdminOnly
  @ChapterLeadOnly
  @GetMapping("secured/admin")
  public ApiResponse<String> anyAdminAvailable() {
    return ApiResponse.onSuccess("운영진 역할이 하나라도 있으면 접근이 가능한 API입니다.");
  }

  @Operation(summary = "테스트용 토큰 발급", description = "챌린저 ID로 JWT 토큰을 발급합니다 (개발/테스트 환경 전용)")
  @GetMapping("/{challengerId}")
  public ApiResponse<String> getTestToken(@PathVariable Long challengerId) {
    return ApiResponse.onSuccess(jwtTokenProvider.createToken(challengerId));
  }

  @GetMapping("roles")
  public ApiResponse<List<ChapterAdminRole>> getRoles(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    return ApiResponse.onSuccess(userPrincipal.roles());
  }

  @Operation(summary = "학교 생성", description = "새로운 학교를 생성합니다 (테스트용)")
  @PostMapping("school")
  public ApiResponse<SchoolDto.Response> createSchool(@RequestBody SchoolDto.CreateRequest body) {
    return ApiResponse.onSuccess(schoolService.create(body));
  }

  @Operation(summary = "학교 대량 생성", description = "여러 학교를 한 번에 생성합니다 (테스트용)")
  @PostMapping("school/bulk")
  public ApiResponse<List<SchoolDto.Response>> bulkCreateSchool(
      @RequestBody SchoolDto.BulkCreateRequest body) {
    return ApiResponse.onSuccess(schoolService.bulkCreate(body));
  }

  @Operation(summary = "더미 학교, 지부, 프로젝트, 챌린저, 운영진 생성", description = "개발 서버용 테스트 기능")
  @PostMapping("dummy/school-chapter")
  public ApiResponse<String> createAllSchools() {
    DummySchools dummySchools = new DummySchools();

    // 1. 학교 대량 생성
    SchoolDto.BulkCreateRequest schoolBulkRequest = SchoolDto.BulkCreateRequest.builder()
        .schools(dummySchools.createAllSchoolsRequest)
        .build();
    List<SchoolDto.Response> createdSchools = schoolService.bulkCreate(schoolBulkRequest);

    // 2. 지부 대량 생성
    ChapterDto.BulkCreateRequest chapterBulkRequest = ChapterDto.BulkCreateRequest.builder()
        .chapters(dummySchools.createAllChaptersRequest)
        .build();
    List<ChapterDto.Response> createdChapters = chapterService.bulkCreate(chapterBulkRequest);

    // 3. 지부명으로 지부 ID 찾기 위한 맵 생성
    Map<String, Long> chapterNameToIdMap = createdChapters.stream()
        .collect(java.util.stream.Collectors.toMap(
            ChapterDto.Response::name,
            ChapterDto.Response::id
        ));

    List<ChallengerDto.CreateRequest> adminRequests = new ArrayList<>();
    Map<Long, Long> schoolIdToChapterIdMap = new HashMap<>();

    // 4. 각 학교를 해당 지부에 연결 (ChapterSchool 생성)
    int totalConnections = 0;
    for (SchoolDto.Response school : createdSchools) {
      String chapterName = dummySchools.schoolToChapterMap.get(school.name());
      if (chapterName != null) {
        Long chapterId = chapterNameToIdMap.get(chapterName);
        if (chapterId != null) {
          chapterSchoolService.create(ChapterSchoolDto.CreateRequest.builder()
              .chapterId(chapterId)
              .schoolId(school.id())
              .build());

          adminRequests.add(
              ChallengerDto.CreateRequest.builder()
                  .gisu(9L)
                  .part(ChallengerPart.NO_PART)
                  .name(school.name().substring(0, 2) + "운영진")
                  .nickname(school.name().substring(0, 5))
                  .gender(Gender.MALE)
                  .schoolId(school.id())
                  .studentId("admin")
                  .password("1234")
                  .build()
          );

          schoolIdToChapterIdMap.put(school.id(), chapterId);
        }
      }
    }

    // 5. 운영진 대량 생성
    List<ChallengerDto.Response> createdAdmins = challengerService.createBulk(adminRequests);

    // 6. ChapterAdmin 연결
    for (ChallengerDto.Response admin : createdAdmins) {
      Long chapterId = schoolIdToChapterIdMap.get(admin.schoolId());
      if (chapterId != null) {
        chapterAdminService.create(ChapterAdminDto.CreateRequest.builder()
            .chapterId(chapterId)
            .challengerId(admin.id())
            .role(ChapterAdminRole.SCHOOL_LEAD)
            .build());
      }
    }

    List<ChallengerDto.CreateRequest> challengerRequests = new ArrayList<>();

    for (SchoolDto.Response school : createdSchools) {
      for (ChallengerPart challengerPart : ChallengerPart.values()) {
        challengerRequests.add(ChallengerDto.CreateRequest.builder()
            .gisu(9L)
            .part(challengerPart)
            .name(school.name().substring(0, 2) + challengerPart.name())
            .nickname(school.name().substring(0, 5))
            .gender(Gender.MALE)
            .schoolId(school.id())
            .studentId(challengerPart.toString().toLowerCase())
            .password("1234")
            .build()
        );
      }
    }

    List<ChallengerDto.Response> createdChallengers = challengerService.createBulk(
        challengerRequests);

    return ApiResponse.onSuccess(
        String.format("9기 더미 데이터 생성 완료 - 학교: %d개, 지부: %d개, 연결: %d개, 챌린저: %d명",
            createdSchools.size(),
            createdChapters.size(),
            totalConnections,
            createdAdmins.size() + createdChallengers.size())
    );
  }

  @Operation(summary = "Leo 지부 생성하기", description = "Leo 챕터와 소속 학교들을 한 번에 생성합니다 (학교 5개 + 챕터 1개)")
  @PostMapping("dummy/leo-chapter-and-school")
  public ApiResponse<String> createLeoAll() {
    DummySchools dummySchools = new DummySchools();

    // 1. 학교들 생성
    List<SchoolDto.Response> createdSchools = dummySchools.createLeoSchoolsRequest.stream()
        .map(schoolService::create)
        .toList();

    // 2. 챕터 생성
    ChapterDto.Response createdChapter = chapterService.create(
        dummySchools.createLeoChapterRequest);

    // 3. ChapterSchool에 등록
    for (SchoolDto.Response school : createdSchools) {
      chapterSchoolService.create(ChapterSchoolDto.CreateRequest.builder()
          .chapterId(createdChapter.id())
          .schoolId(school.id())
          .build());
    }

    return ApiResponse.onSuccess(
        String.format("Leo 챕터 더미 데이터 생성 완료: 학교 %d개, 챕터 1개 (ID: %d)",
            createdSchools.size(),
            createdChapter.id())
    );
  }

  @Operation(
      summary = "Leo 프로젝트 생성하기",
      description = "Leo 챕터의 12개 프로젝트와 각 프로젝트의 TO(파트별 모집 인원)를 생성합니다")
  @PostMapping("dummy/leo-projects")
  public ApiResponse<String> createLeoProjects() {
    DummyProjects dummyProjects = new DummyProjects();

    int totalProjects = 0;
    int totalTos = 0;

    // 각 프로젝트 생성 및 TO 설정
    for (ProjectDto.CreateRequest projectRequest : dummyProjects.leoProjectLists) {
      // 1. 프로젝트 생성
      ProjectDto.Response createdProject = projectService.create(projectRequest);
      totalProjects++;

      // 2. 해당 프로젝트의 TO 정보 가져오기
      String projectName = createdProject.name();
      Map<ChallengerPart, Integer> tosForProject = dummyProjects.projectTosMap.get(projectName);

      if (tosForProject != null) {
        // 3. 각 파트별 TO 생성
        for (Map.Entry<ChallengerPart, Integer> entry : tosForProject.entrySet()) {
          ProjectToDto.CreateRequest toRequest = ProjectToDto.CreateRequest.builder()
              .projectId(createdProject.id())
              .part(entry.getKey())
              .toCount(entry.getValue())
              .build();

          projectToService.create(toRequest);
          totalTos++;
        }
      }
    }

    return ApiResponse.onSuccess(
        String.format("Leo 프로젝트 더미 데이터 생성 완료: 프로젝트 %d개, TO %d개",
            totalProjects, totalTos)
    );
  }

  @Operation(summary = "지부별 더미 프로젝트 생성", description = "4개 지부(Leo, Scorpio, Cetus, Pegasus) 각각에 3개씩 프로젝트와 TO 생성")
  @PostMapping("dummy/projects")
  public ApiResponse<String> createAllChapterProjects() {
    // 1. 모든 Chapter 조회
    List<ChapterDto.Response> chapters = chapterService.findAll();

    if (chapters.isEmpty()) {
      throw new DomainException(DomainType.TEST, ErrorStatus._BAD_REQUEST);
    }

    int totalProjects = 0;
    int totalTos = 0;

    // 2. 각 지부별로 3개의 프로젝트 생성
    for (ChapterDto.Response chapter : chapters) {
      // productOwnerId를 챕터의 운영진(첫 번째 챌린저)로 설정
      // 실제로는 해당 챕터의 실제 운영진 ID를 사용해야 함
      Long productOwnerId = 1L; // 기본값

      for (int i = 1; i <= 3; i++) {
        // 프로젝트 생성
        ProjectDto.CreateRequest projectRequest = ProjectDto.CreateRequest.builder()
            .chapterId(chapter.id())
            .name(chapter.name() + " 프로젝트 " + i)
            .description(chapter.name() + " 지부의 " + i + "번째 프로젝트입니다.")
            .logoImageUrl("https://example.com/logo.png")
            .bannerImageUrl("https://example.com/banner.png")
            .notionLink(
                "https://chunganguniv.notion.site/UPMS-UMC-Project-Matching-System-2a639ff6717780a4a83eec9ffe3e74f3?source=copy_link")
            .productOwnerId(productOwnerId)
            .build();

        ProjectDto.Response createdProject = projectService.create(projectRequest);
        totalProjects++;

        // 각 프로젝트에 파트별 TO 생성
        Map<ChallengerPart, Integer> to1 = Map.of(
            ChallengerPart.DESIGN, 1,
            ChallengerPart.WEB, 3,
            ChallengerPart.SPRINGBOOT, 4
        );

        Map<ChallengerPart, Integer> to2 = Map.of(
            ChallengerPart.DESIGN, 1,
            ChallengerPart.ANDROID, 4,
            ChallengerPart.NODEJS, 3
        );

        // to1과 to2 중 랜덤 선택
        Random random = new Random();
        Map<ChallengerPart, Integer> selectedTo = random.nextBoolean() ? to1 : to2;

        for (Map.Entry<ChallengerPart, Integer> entry : selectedTo.entrySet()) {
          ProjectToDto.CreateRequest toRequest = ProjectToDto.CreateRequest.builder()
              .projectId(createdProject.id())
              .part(entry.getKey())
              .toCount(entry.getValue())
              .build();

          projectToService.create(toRequest);
          totalTos++;
        }
      }
    }

    return ApiResponse.onSuccess(
        String.format("전체 지부 프로젝트 더미 데이터 생성 완료 - 지부: %d개, 프로젝트: %d개, TO: %d개",
            chapters.size(), totalProjects, totalTos)
    );
  }
}
