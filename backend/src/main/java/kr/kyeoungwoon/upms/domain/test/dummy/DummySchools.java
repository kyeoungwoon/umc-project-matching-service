package kr.kyeoungwoon.upms.domain.test.dummy;

import java.util.List;
import java.util.Map;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterDto;

public class DummySchools {

  public List<ChapterDto.CreateRequest> initChapters =
      List.of(ChapterDto.CreateRequest.builder().name("Leo").gisu(9L).build(),
          // ChapterDto.CreateRequest.builder()
          // .name("Scorpio")
          // .gisu(9L)
          // .build(),
          ChapterDto.CreateRequest.builder().name("Cetus").gisu(9L).build(),
          ChapterDto.CreateRequest.builder().name("Pegasus").gisu(9L).build(),
          ChapterDto.CreateRequest.builder().name("Cassiopeia").gisu(9L).build());

  // 학교명 -> 지부명 매핑
  public Map<String, String> schoolToChapterMap = Map.ofEntries(
      // 레오
      Map.entry("중앙대학교", "Leo"), Map.entry("한국외국어대학교", "Leo"), Map.entry("이화여자대학교", "Leo"),
      Map.entry("동덕여자대학교", "Leo"), Map.entry("단국대학교", "Leo"),
      // 스콜피오
      // Map.entry("상명대학교", "Scorpio"),
      // Map.entry("서울여자대학교", "Scorpio"),
      // Map.entry("한국공학대학교", "Scorpio"),
      // Map.entry("명지대학교", "Scorpio"),
      // Map.entry("동국대학교", "Scorpio"),
      // 케투스
      Map.entry("서경대학교", "Cetus"), Map.entry("광운대학교", "Cetus"), Map.entry("성신여자대학교", "Cetus"),
      Map.entry("덕성여자대학교", "Cetus"),
      // 페가수스
      Map.entry("국립부경대학교", "Pegasus"), Map.entry("동아대학교", "Pegasus"),
      Map.entry("영남대학교", "Pegasus"), Map.entry("울산대학교", "Pegasus"),
      Map.entry("인제대학교", "Pegasus"),
      // 카시오페아
      Map.entry("동양미래대학교", "Cassiopeia"), Map.entry("숙명여자대학교", "Cassiopeia"),
      Map.entry("인하대학교", "Cassiopeia"), Map.entry("한성대학교", "Cassiopeia"),
      Map.entry("한양대학교 ERICA", "Cassiopeia"));

  public Map<String, String> schoolLogos = Map.ofEntries(
      Map.entry("광운대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/kwangwoon.svg"),
      Map.entry("국립부경대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/pukyong.svg"),
      Map.entry("단국대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/danguk.svg"),
      Map.entry("덕성여자대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/duksung.svg"),
      Map.entry("동덕여자대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/dongduk.svg"),
      Map.entry("동아대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/dona.svg"),
      Map.entry("동양미래대학교",
          "https://d3l3u4je64pway.cloudfront.net/logos/schools/dongyang.svg"),
      Map.entry("서경대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/seokyeong.svg"),
      Map.entry("성신여자대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/sswu.svg"),
      Map.entry("숙명여자대학교",
          "https://d3l3u4je64pway.cloudfront.net/logos/schools/sookmyung.svg"),
      Map.entry("영남대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/yeungnam.svg"),
      Map.entry("울산대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/ulsan.svg"),
      Map.entry("이화여자대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/ewha.svg"),
      Map.entry("인제대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/inje.svg"),
      Map.entry("인하대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/inha.svg"),
      Map.entry("중앙대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/cau.svg"),
      Map.entry("한국외국어대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/hufs.svg"),
      Map.entry("한성대학교", "https://d3l3u4je64pway.cloudfront.net/logos/schools/hansung.svg"),
      Map.entry("한양대학교 ERICA",
          "https://d3l3u4je64pway.cloudfront.net/logos/schools/dongduk.svg"));
}
