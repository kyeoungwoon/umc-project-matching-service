package kr.kyeoungwoon.upms.domain.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterDto;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterSchoolDto;
import kr.kyeoungwoon.upms.domain.challenger.dto.SchoolDto;
import kr.kyeoungwoon.upms.domain.challenger.service.ChallengerService;
import kr.kyeoungwoon.upms.domain.challenger.service.ChapterAdminService;
import kr.kyeoungwoon.upms.domain.challenger.service.ChapterSchoolService;
import kr.kyeoungwoon.upms.domain.challenger.service.ChapterService;
import kr.kyeoungwoon.upms.domain.challenger.service.SchoolService;
import kr.kyeoungwoon.upms.domain.project.service.ProjectService;
import kr.kyeoungwoon.upms.domain.project.service.ProjectToService;
import kr.kyeoungwoon.upms.domain.test.dummy.DummySchools;
import kr.kyeoungwoon.upms.global.apiPayload.ApiResponse;
import kr.kyeoungwoon.upms.global.enums.ChapterAdminRole;
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
  @PostMapping("init/chapter-and-schools")
  public ApiResponse<String> createAllSchools() {
    DummySchools dummySchools = new DummySchools();

    // 학교 생성
    List<SchoolDto.CreateRequest> schools = dummySchools.schoolToChapterMap.keySet().stream().map(
        schoolName -> SchoolDto.CreateRequest.builder()
            .name(schoolName)
            .logoImageUrl(dummySchools.schoolLogos.get(schoolName))
            .build()
    ).toList();

    List<SchoolDto.Response> createdSchools = schoolService.bulkCreate(
        SchoolDto.BulkCreateRequest.builder()
            .schools(schools)
            .build()
    );

    // 지부 생성
    ChapterDto.BulkCreateRequest createChapterRequest = ChapterDto.BulkCreateRequest.builder()
        .chapters(dummySchools.initChapters)
        .build();
    List<ChapterDto.Response> createdChapters = chapterService.bulkCreate(createChapterRequest);

    // 생성된 지부명과 ID를 mapping
    Map<String, Long> chapterNameToIdMap = createdChapters.stream()
        .collect(java.util.stream.Collectors.toMap(
            ChapterDto.Response::name,
            ChapterDto.Response::id
        ));

    // 4. 각 학교를 해당 지부에 연결 (ChapterSchool 생성)
    for (SchoolDto.Response school : createdSchools) {
      // 각 학교가 속한 지부명 조회
      String chapterName = dummySchools.schoolToChapterMap.get(school.name());
      if (chapterName != null) {
        // 지부 생성할 때 생성한 map에서 지부 ID 가져오기
        Long chapterId = chapterNameToIdMap.get(chapterName);
        if (chapterId != null) {
          // 지부에 학교 생성
          chapterSchoolService.create(ChapterSchoolDto.CreateRequest.builder()
              .chapterId(chapterId)
              .schoolId(school.id())
              .build());
        }
      }
    }
    
    return ApiResponse.onSuccess(
        "UPMS를 사용하는 9기 학교 및 지부 초기화를 완료하였습니다."
    );
  }
}