package kr.kyeoungwoon.upms.domain.test.dummy;

import java.util.List;
import java.util.Map;
import kr.kyeoungwoon.upms.domain.project.dto.ProjectDto;
import kr.kyeoungwoon.upms.global.enums.ChallengerPart;

public class DummyProjects {

  // 프로젝트별 TO 정보 (프로젝트명 -> 파트별 인원)
  public Map<String, Map<ChallengerPart, Integer>> projectTosMap = Map.ofEntries(
      Map.entry("북리플",
          Map.of(ChallengerPart.DESIGN, 1, ChallengerPart.WEB, 4, ChallengerPart.NODEJS, 5)),
      Map.entry("이음50+",
          Map.of(ChallengerPart.DESIGN, 1, ChallengerPart.WEB, 4, ChallengerPart.NODEJS, 5)),
      Map.entry("Wearly",
          Map.of(ChallengerPart.DESIGN, 1, ChallengerPart.ANDROID, 3, ChallengerPart.NODEJS, 5)),
      Map.entry("MY4CUT",
          Map.of(ChallengerPart.DESIGN, 1, ChallengerPart.ANDROID, 3, ChallengerPart.SPRINGBOOT,
              5)),
      Map.entry("BarKit",
          Map.of(ChallengerPart.DESIGN, 1, ChallengerPart.WEB, 4, ChallengerPart.SPRINGBOOT, 5)),
      Map.entry("Co:N-next",
          Map.of(ChallengerPart.DESIGN, 1, ChallengerPart.WEB, 4, ChallengerPart.SPRINGBOOT, 6)),
      Map.entry("Finders",
          Map.of(ChallengerPart.DESIGN, 1, ChallengerPart.WEB, 4, ChallengerPart.SPRINGBOOT, 5)),
      Map.entry("Proovy",
          Map.of(ChallengerPart.DESIGN, 1, ChallengerPart.WEB, 4, ChallengerPart.SPRINGBOOT, 5)),
      Map.entry("Verbly",
          Map.of(ChallengerPart.DESIGN, 1, ChallengerPart.WEB, 5, ChallengerPart.SPRINGBOOT, 6)),
      Map.entry("RealMatch",
          Map.of(ChallengerPart.DESIGN, 1, ChallengerPart.WEB, 4, ChallengerPart.SPRINGBOOT, 5)),
      Map.entry("ALLPLAY",
          Map.of(ChallengerPart.DESIGN, 1, ChallengerPart.WEB, 4, ChallengerPart.SPRINGBOOT, 5)),
      Map.entry("모아요",
          Map.of(ChallengerPart.DESIGN, 1, ChallengerPart.WEB, 4, ChallengerPart.SPRINGBOOT, 5))
  );

  public List<ProjectDto.CreateRequest> leoProjectLists = List.of(
      ProjectDto.CreateRequest.builder()
          .chapterId(1L)
          .name("북리플")
          .description("독서를 기록하고 나누며, 질문과 연결을 통해 책 너머의 경험을 확장하는 서비스.")
          .logoImageUrl("https://example.com/logo.png")
          .bannerImageUrl("https://example.com/banner.png")
          .notionLink(
              "https://makeus-challenge.notion.site/2a4b57f4596b8063b891fa8f86146af7?source=copy_link")
          .productOwnerId(2L)
          .build(),
      ProjectDto.CreateRequest.builder()
          .chapterId(1L)
          .name("이음50+")
          .description("싱글 시니어를 위한 음성 기반 데이팅 앱")
          .logoImageUrl("https://example.com/logo.png")
          .bannerImageUrl("https://example.com/banner.png")
          .notionLink(
              "https://makeus-challenge.notion.site/50-2a3b57f4596b80349dabdf40760ed604?source=copy_link")
          .productOwnerId(8L)
          .build(),
      ProjectDto.CreateRequest.builder()
          .chapterId(1L)
          .name("Wearly")
          .description("Wearly는 내 옷을 시각적으로 관리하고 코디를 계획할 수 있는 스마트 옷장 앱입니다.")
          .logoImageUrl("https://example.com/logo.png")
          .bannerImageUrl("https://example.com/banner.png")
          .notionLink(
              "https://makeus-challenge.notion.site/Wearly-2a3b57f4596b802781c7f110616bbc2e?source=copy_link")
          .productOwnerId(9L)
          .build(),
      ProjectDto.CreateRequest.builder()
          .chapterId(1L)
          .name("MY4CUT")
          .description("네컷 사진 아카이빙 및 통합 관리 서비스")
          .logoImageUrl("https://example.com/logo.png")
          .bannerImageUrl("https://example.com/banner.png")
          .notionLink(
              "https://makeus-challenge.notion.site/MY4CUT-2a3b57f4596b80cfbb65d5343430f563?source=copy_link")
          .productOwnerId(19L)
          .build(),
      ProjectDto.CreateRequest.builder()
          .chapterId(1L)
          .name("BarKit")
          .description("Barkit은 여러 멤버십·포인트 바코드를 한곳에 모아 매장별로 자동 매칭·즉시 제시할 수 있는 통합 멤버십 지갑 서비스 입니다.")
          .logoImageUrl("https://example.com/logo.png")
          .bannerImageUrl("https://example.com/banner.png")
          .notionLink(
              "https://makeus-challenge.notion.site/BarKit-2a2b57f4596b808585d6f2a7dcf1e23f?source=copy_link")
          .productOwnerId(37L)
          .build(),
      ProjectDto.CreateRequest.builder()
          .chapterId(1L)
          .name("Co:N-next")
          .description("공연장 안내 및 메이트 찾기 서비스")
          .logoImageUrl("https://example.com/logo.png")
          .bannerImageUrl("https://example.com/banner.png")
          .notionLink(
              "https://makeus-challenge.notion.site/Co-N-next-2a3b57f4596b800597f0e7f0a7f43797?source=copy_link")
          .productOwnerId(38L)
          .build(),
      ProjectDto.CreateRequest.builder()
          .chapterId(1L)
          .name("Finders")
          .description("필름 현상소 통합/추천 플랫폼")
          .logoImageUrl("https://example.com/logo.png")
          .bannerImageUrl("https://example.com/banner.png")
          .notionLink(
              "https://makeus-challenge.notion.site/Finders-2a3b57f4596b8079bdfdc041557b9a68?pvs=25")
          .productOwnerId(59L)
          .build(),
      ProjectDto.CreateRequest.builder()
          .chapterId(1L)
          .name("Proovy")
          .description("Proovy(프루비)는 '이공계 대학생들을 위한, 단순한 정답을 넘어, 약점을 찾아 완성시키는 AI 튜터'입니다.")
          .logoImageUrl("https://example.com/logo.png")
          .bannerImageUrl("https://example.com/banner.png")
          .notionLink(
              "https://makeus-challenge.notion.site/Proovy-2a3b57f4596b809fbfe1edd30c33c621?pvs=25")
          .productOwnerId(60L)
          .build(),
      ProjectDto.CreateRequest.builder()
          .chapterId(1L)
          .name("Verbly")
          .description("구동사 학습 앱")
          .logoImageUrl("https://example.com/logo.png")
          .bannerImageUrl("https://example.com/banner.png")
          .notionLink(
              "https://makeus-challenge.notion.site/Verbly-2a4b57f4596b800d93bfd11df92bc88a?pvs=25")
          .productOwnerId(70L)
          .build(),
      ProjectDto.CreateRequest.builder()
          .chapterId(1L)
          .name("RealMatch")
          .description("브랜드와 인플루언서를 톤앤매너 중심으로 정교하게 연결해주는 매칭 플랫폼")
          .logoImageUrl("https://example.com/logo.png")
          .bannerImageUrl("https://example.com/banner.png")
          .notionLink(
              "https://makeus-challenge.notion.site/RealMatch-2a3b57f4596b80e7b248cc9d059aefdd?pvs=25")
          .productOwnerId(100L)
          .build(),
      ProjectDto.CreateRequest.builder()
          .chapterId(1L)
          .name("ALLPLAY")
          .description(
              "ALLPLAY는 'Play · Link · Achieve · You Better' 라는 가치를 중심에 두고, 누구나 쉽고 간편하게 운동을 시작하고 지속할 수 있도록 돕습니다.")
          .logoImageUrl("https://example.com/logo.png")
          .bannerImageUrl("https://example.com/banner.png")
          .notionLink(
              "https://makeus-challenge.notion.site/ALLPLAY-2a3b57f4596b80a6a449d8ded32063a5?pvs=25")
          .productOwnerId(98L)
          .build(),
      ProjectDto.CreateRequest.builder()
          .chapterId(1L)
          .name("모아요")
          .description("모아요는 대학생의 이력 관리와 팀 빌딩을 한 번에 해결하는 플랫폼입니다.")
          .logoImageUrl("https://example.com/logo.png")
          .bannerImageUrl("https://example.com/banner.png")
          .notionLink(
              "https://makeus-challenge.notion.site/2a1b57f4596b80ecafeadba8766900d1?pvs=25")
          .productOwnerId(99L)
          .build()
  );
}
