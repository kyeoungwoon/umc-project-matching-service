package kr.kyeoungwoon.upms.domain.test.dummy;

import java.util.List;
import java.util.Map;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChapterDto;
import kr.kyeoungwoon.upms.domain.challenger.dto.SchoolDto;

public class DummySchools {

  public List<SchoolDto.CreateRequest> createLeoSchoolsRequest = List.of(
      SchoolDto.CreateRequest.builder()
          .name("중앙대학교")
          .logoImageUrl(
              "https://github.com/user-attachments/assets/5cf40679-17c2-4145-8b84-69042a8ff57a")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("이화여자대학교")
          .logoImageUrl(
              "https://github.com/user-attachments/assets/9153aaf4-dc53-4b42-8228-72907ce2b162")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("동덕여자대학교")
          .logoImageUrl(
              "https://github.com/user-attachments/assets/452f9ad6-47f9-4e78-9476-3150a8298ffe")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("한국외국어대학교")
          .logoImageUrl(
              "https://github.com/user-attachments/assets/d6c7aa37-c332-4141-96d3-88f01805ada7")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("단국대학교")
          .logoImageUrl(
              "https://github.com/user-attachments/assets/a5c76ee9-a8d8-41ee-8508-784b962f05cc")
          .build()
  );

  public List<SchoolDto.CreateRequest> createAllSchoolsRequest = List.of(
      SchoolDto.CreateRequest.builder()
          .name("중앙대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/d7NvARiC5zcz5j922rLSpYwG5OdOg_2qfEnAyDrHIT_Lv-RclEDAp8TeX4lZtdaSLfHAWMqfSHeh8lYsCl6m3GPWihsvXuc9VRToT43WthYSdmGJvSu0_XYDRzX-nVa7_PM4CT9f8eli4921-wvQXw.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("이화여자대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/9AKUXAlfBZhPDLTHDlZQLuc_yRRz79NNH9nrc0EejOJusl21HRdIb0hKSuNCt_nOw0ZnU3JrHgeeArZ18CsNGYgqzcR6heW3kVDs70ReZt3_b3xhpqVDpAfe6x0-vGyLyMkNx2Qj_GVaMwiAXNhbRQ.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("한국외국어대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/0kAHU-oB2FuFZfx6XgRoLNhYZR7snmZv9BqCyNvenzt4KNtnx9hpfeunANW5HdP9Z_4UOirbMnoEeOJfNiFsp1dkBTG_s-kmpSFlQKN-CvZjosmYSBMrAIJDPb7AgfvPELfervjXzZlCuDVySqq8vA.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("동덕여자대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/ccOXqDJR4guW9EUheB8JNM3IvXZ-1pLylslWtUTuDZxa_cuC9--dlyfMjs-2FAG22sD5JDVRwk8BwqmVGFdZQ6KMK8uaNc1TIpfLbE1LQX5A6QCBRb8VlKIVEkH2J449ATkezL3BnmiJXFGi2YztvQ.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("단국대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/TkkyIIzJdhRoXw7C3ROvg3yDOjIB-dKgl8UllxgCsiZ9D2gMtACC-W5u-XEHCxgwANGLlIFxflDUAt30CYsq9og0fjYKLTV7TwAeNQeqatbhwLqcahrxv7lt5y0M64ntKxDckrCf3VEtPpdKSW1QIQ.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("상명대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/_8Lwmg4EvP6FkI2m79mrRe_wt0IxOje4TBgA5GjEO8HdGqpUA3yxsL-O7BHZ43_PlcBmcIGNihsX5WlL6bv2U8a2S8KmcvWIcoPwlOJI5yKTccSdcnnwduhKhELSmncY7sY3slNeLS3ufHoCU9YOpw.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("서경대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/4aFWqdyngaByE5GFVTp2Byh5NGhq5_Mr1SBaudqE9F0Pst_iwlM2aRrD-MngXKXX2FVmRGT1mSmxSEkdUBBfDw.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("국립부경대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/dgdk0aeQU582CdRDcVz65w43hIrY8p__fwnY9V33tV-vq9itqWeDoRYoByds1Vtnbw5Se_Ivz1bMDNxbiIYQaiICIh77mWBUZJAYJCsILuz7nAf4Rae4PNmFXyJepcMpbnnM3lmC_pnCcYnR3a5dxw.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("동아대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/Gu9vSHc6vi83uGvnudgF3e4DVBpMUK31MjhR6Su2f6txKh9e30e0RNuvpQjKb3QqrpAP9V_JxO4yZeNSxcOSw9_5EcXXuhcmDio3Ysxpg7RG73Q_KSeRClXe2EadJKEvEBDwMaUQkkL4-z5FzuEUcA.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("광운대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/hFWSVQvt8aM7zsPdgPPeS_BFCUQcwdKfqHH0F-0csUkrTIkZ31wfCTnUeK-f0yfzZyzwTNZ7kfUHkxhqaW6aMaVkrn-P2LIbMUrzfN7pcZs6hmrVrSK9QNeApiSvaan3OTeFWpyCy2euUwK_97AM7g.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("서울여자대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/I-eBjJ1wNlTHSgDYev2VbZtqbnt4mjRI7CSCnmhvM0-x6cZSRGJhqviCC1oXOPW_DSoYBhzCBxx9emO2_UBryVV2rmpEQpBsFketum_6k1qNDZ0rHRcJmlQuj9jwaLgBryMqanG5Lin_yr0ueQfCpw.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("성신여자대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/KegWS_nMh7y17OsVzJ7ZaM5S101Dr1Lue0OXSXtgb4PZggu9qEUohys3CEvtmz6oS_YJ6ZSl8DiiFC2ZE1NipKWLBrb_9Dr1Tr7bOEyPQnJWp4Gc_242UI9EQSAvSaL0xbVxOsH9E76v0WcTnL0YJg.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("영남대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/KFHhkVVe0BuA-QNoNczPYpFqe5pzC8Q4FjhJVnrmsJB0oANRSisMXItmwZp6OdI1KopIG7pZsOkzxyDZfA2NMg.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("한국공학대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/lmlctlbFFuqppZspF2eKxxd3GroQ2R8kumFUZgWeDaRO4UleSe38ltIJxqSFYv2Hq09RROAEtm300xm-mCEkEmH17MvOh9E9Wuzufyny3yZpsBI3GRbalxIpVpfKzZPr45xdY-ZznfSTFLYtN9xVng.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("명지대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/NepKvusBLghrItleeMbWd9lXIpHOJ1stVaRvbwlNmkms2fcFA-PqOBQIQ_CFxsXLCx9_EUjbfyIFPmq24h-MaA.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("동국대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/Px_d1yTr1v3lSMPacOIhYwTQ9Ge9snazpczS4PQPJX_IrxqWVybsQTy6ML93ZGXnJyq7hjOo4Gdiud7k3HL7SQ.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("덕성여자대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/r8MWHdMR3HzSKkziH_DcC6aSG8lyzDLIHqPbIJZUsaDKCYv3nUlZbNzkjn_6MxsRfdafM1sar8P-QUll2dhCPQZ_5C7u9l7WtdIzkUIDtFpOkgvgAW4qKqpzsYrAfBm0JTdlUlheghpOWFWM5nELPA.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("울산대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/UBvAqx93cM9-xmAAVFXsK1RxElqPx7IemfCTubAn0WUFs1X5D8u9H80OUsUUWO_QfXbpXIh9mJ5aPLnw5GN35DGYXLUQPEagHRsyMucgFoTW02l_hWhzweI7tZA3vQBVKTAzlxLFIPz1Gve-11SCXA.svg")
          .build(),
      SchoolDto.CreateRequest.builder()
          .name("인제대학교")
          .logoImageUrl(
              "https://i.namu.wiki/i/zqCp7hSL3Do4oFxsbArbJ-S8JX9TKQYKjeiRq9-IQ42q5W5KUlIExww47L9_6m7_mEyrnKjIo_RAWlWam1RuHQjBWolXuLSXCSbPA-ag01n-JVQ7KKTPCeJ3T7CQ1iOY_6XqR3bqF0Umh8f726IB5g.svg")
          .build()
  );

  public ChapterDto.CreateRequest createLeoChapterRequest = ChapterDto.CreateRequest.builder()
      .name("Leo")
      .gisu(9L)
      .description("중앙대학교, 이화여자대학교, 동덕여자대학교, 한국외국어대학교, 단국대학교 가 함께하는 UMC 9기 Leo 지부 입니다.")
      .build();

  public ChapterDto.CreateRequest createScorpioChapterRequest = ChapterDto.CreateRequest.builder()
      .name("Scorpio")
      .gisu(9L)
      .description("상명대학교, 서울여자대학교, 한국공학대학교, 명지대학교, 동국대학교가 함께하는 UMC 9기 Scorpio 지부 입니다.")
      .build();

  public ChapterDto.CreateRequest createCetusChapterRequest = ChapterDto.CreateRequest.builder()
      .name("Cetus")
      .gisu(9L)
      .description("서경대학교, 광운대학교, 성신여자대학교, 덕성여자대학교가 함께하는 UMC 9기 Cetus 지부 입니다.")
      .build();

  public ChapterDto.CreateRequest createPegasusChapterRequest = ChapterDto.CreateRequest.builder()
      .name("Pegasus")
      .gisu(9L)
      .description("국립부경대학교, 동아대학교, 영남대학교, 울산대학교, 인제대학교가 함께하는 UMC 9기 Pegasus 지부 입니다.")
      .build();

  public List<ChapterDto.CreateRequest> createAllChaptersRequest = List.of(
      createLeoChapterRequest,
      createScorpioChapterRequest,
      createCetusChapterRequest,
      createPegasusChapterRequest
  );

  // 학교명 -> 지부명 매핑
  public Map<String, String> schoolToChapterMap = Map.ofEntries(
      Map.entry("중앙대학교", "Leo"),
      Map.entry("한국외국어대학교", "Leo"),
      Map.entry("이화여자대학교", "Leo"),
      Map.entry("동덕여자대학교", "Leo"),
      Map.entry("단국대학교", "Leo"),
      Map.entry("상명대학교", "Scorpio"),
      Map.entry("서울여자대학교", "Scorpio"),
      Map.entry("한국공학대학교", "Scorpio"),
      Map.entry("명지대학교", "Scorpio"),
      Map.entry("동국대학교", "Scorpio"),
      Map.entry("서경대학교", "Cetus"),
      Map.entry("광운대학교", "Cetus"),
      Map.entry("성신여자대학교", "Cetus"),
      Map.entry("덕성여자대학교", "Cetus"),
      Map.entry("국립부경대학교", "Pegasus"),
      Map.entry("동아대학교", "Pegasus"),
      Map.entry("영남대학교", "Pegasus"),
      Map.entry("울산대학교", "Pegasus"),
      Map.entry("인제대학교", "Pegasus")
  );
}
