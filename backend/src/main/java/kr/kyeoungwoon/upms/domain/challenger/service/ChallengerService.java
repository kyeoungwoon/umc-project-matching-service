package kr.kyeoungwoon.upms.domain.challenger.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kr.kyeoungwoon.upms.domain.challenger.dto.ChallengerDto;
import kr.kyeoungwoon.upms.domain.challenger.entity.Challenger;
import kr.kyeoungwoon.upms.domain.challenger.entity.ChapterAdmin;
import kr.kyeoungwoon.upms.domain.challenger.entity.School;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChallengerRepository;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChapterAdminRepository;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChapterRepository;
import kr.kyeoungwoon.upms.domain.challenger.repository.SchoolRepository;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import kr.kyeoungwoon.upms.global.apiPayload.exception.DomainException;
import kr.kyeoungwoon.upms.global.enums.ChapterAdminRole;
import kr.kyeoungwoon.upms.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengerService {

  private final ChallengerRepository challengerRepository;
  private final ChapterRepository chapterRepository;
  private final ChapterAdminRepository chapterAdminRepository;
  private final SchoolRepository schoolRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  @Transactional
  public ChallengerDto.Response create(ChallengerDto.CreateRequest request) {
    School school = schoolRepository.findById(request.schoolId())
        .orElseThrow(
            () -> new DomainException(DomainType.SCHOOL, ErrorStatus.SCHOOL_NOT_FOUND));

    Challenger challenger = Challenger.builder()
        .umsbId(request.umsbId())
        .gisu(request.gisu())
        .part(request.part())
        .name(request.name())
        .nickname(request.nickname())
        .gender(request.gender())
        .school(school)
        .studentId(request.studentId())
        .password(passwordEncoder.encode(request.password()))
        .profileImageUrl(request.profileImageUrl())
        .build();

    Challenger saved = challengerRepository.save(challenger);
    return toResponse(saved);
  }

  @Transactional
  public List<ChallengerDto.Response> createBulk(List<ChallengerDto.CreateRequest> requests) {
    // 모든 학교 ID를 미리 조회하여 캐싱
    Set<Long> schoolIds = requests.stream()
        .map(ChallengerDto.CreateRequest::schoolId)
        .collect(Collectors.toSet());

    Map<Long, School> schoolMap = schoolRepository.findAllById(schoolIds).stream()
        .collect(Collectors.toMap(School::getId, school -> school));

    // Challenger 엔티티 리스트 생성
    List<Challenger> challengers = requests.stream()
        .map(request -> {
          School school = schoolMap.get(request.schoolId());
          if (school == null) {
            throw new DomainException(DomainType.SCHOOL, ErrorStatus.SCHOOL_NOT_FOUND);
          }

          return Challenger.builder()
              .umsbId(request.umsbId())
              .gisu(request.gisu())
              .part(request.part())
              .name(request.name())
              .nickname(request.nickname())
              .gender(request.gender())
              .school(school)
              .studentId(request.studentId())
              .password(passwordEncoder.encode(request.password()))
              .profileImageUrl(request.profileImageUrl())
              .build();
        })
        .toList();

    // 배치로 저장
    List<Challenger> savedChallengers = challengerRepository.saveAll(challengers);

    // Response로 변환
    return savedChallengers.stream()
        .map(this::toResponse)
        .toList();
  }

  public ChallengerDto.LoginResponse login(ChallengerDto.LoginRequest request) {
    // 학번, 학교, 기수로 사용자 조회
    Challenger challenger = challengerRepository.findByStudentIdAndSchoolIdAndGisu(
            request.studentId(), request.schoolId(), request.gisu())
        .orElseThrow(() -> new DomainException(DomainType.CHALLENGER,
            // 없으면 에러
            ErrorStatus.CHALLENGER_INVALID_CREDENTIALS));

    // 비밀번호 검증
    if (!passwordEncoder.matches(request.password(), challenger.getPassword())) {
      throw new DomainException(DomainType.CHALLENGER, ErrorStatus.CHALLENGER_INVALID_CREDENTIALS);
    }

    // 사용자의 역할을 조회해서 반환
    List<ChapterAdminRole> roles = chapterAdminRepository.findByChallengerId(challenger.getId())
        .stream().map(ChapterAdmin::getRole).toList();

    // JWT 토큰 생성
    String token = jwtTokenProvider.createToken(challenger.getId(), roles);

    // 로그인 응답 생성
    return ChallengerDto.LoginResponse.builder()
        .accessToken(token)
        .challengerInfo(toResponse(challenger))
        .build();
  }

  public ChallengerDto.Response findById(Long id) {
    Challenger challenger = challengerRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.CHALLENGER,
            ErrorStatus.CHALLENGER_NOT_FOUND));
    return toResponse(challenger);
  }

  /**
   * UMSB ID로 Challenger 조회
   */
  public ChallengerDto.Response findByUmsbId(Long umsbId) {
    Challenger challenger = challengerRepository.findByUmsbId(umsbId)
        .orElseThrow(
            () -> new DomainException(DomainType.CHALLENGER,
                ErrorStatus.CHALLENGER_UMSB_ID_NOT_FOUND));
    return toResponse(challenger);
  }

  public Page<ChallengerDto.Response> findAll(Pageable pageable) {
    return challengerRepository.findAll(pageable)
        .map(this::toResponse);
  }

  @Transactional
  public ChallengerDto.Response update(Long id, ChallengerDto.UpdateRequest request) {
    Challenger challenger = challengerRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.CHALLENGER,
            ErrorStatus.CHALLENGER_NOT_FOUND));

    challenger.updateProfile(request.name(), request.nickname(), request.profileImageUrl());
    if (request.password() != null) {
      challenger.changePassword(passwordEncoder.encode(request.password()));
    }
    challenger.changePart(request.part());
    challenger.changeGender(request.gender());

    return toResponse(challenger);
  }

  @Transactional
  public void delete(Long id) {
    if (!challengerRepository.existsById(id)) {
      throw new DomainException(DomainType.CHALLENGER, ErrorStatus.CHALLENGER_NOT_FOUND);
    }
    challengerRepository.deleteById(id);
  }

  /**
   * 비밀번호 변경
   */
  @Transactional
  public void changePassword(Long id, ChallengerDto.ChangePasswordRequest request) {
    Challenger challenger = challengerRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.CHALLENGER,
            ErrorStatus.CHALLENGER_NOT_FOUND));

    // 현재 비밀번호 검증
    if (!passwordEncoder.matches(request.currentPassword(), challenger.getPassword())) {
      throw new DomainException(DomainType.CHALLENGER,
          ErrorStatus.CHALLENGER_PREV_PASSWORD_INCORRECT);
    }

    // 새 비밀번호 암호화 및 저장
    challenger.changePassword(passwordEncoder.encode(request.newPassword()));
  }

  public List<ChallengerDto.Response> findChallengerByName(String name) {

    return challengerRepository.findByNameContaining(name)
        .stream().map(this::toResponse)
        .toList();
  }

  private ChallengerDto.Response toResponse(Challenger challenger) {
    // Chapter 정보 조회
    var chapter = chapterRepository.findByChallengerId(challenger.getId())
        .orElse(null);

    return ChallengerDto.Response.builder()
        .id(challenger.getId())
        .umsbId(challenger.getUmsbId())
        .gisu(challenger.getGisu())
        .part(challenger.getPart())
        .name(challenger.getName())
        .nickname(challenger.getNickname())
        .gender(challenger.getGender())
        .schoolId(challenger.getSchool().getId())
        .schoolName(challenger.getSchool().getName())
        .schoolLogoImageUrl(challenger.getSchool().getLogoImageUrl())
        .studentId(challenger.getStudentId())
        .profileImageUrl(challenger.getProfileImageUrl())
        .chapterId(chapter != null ? chapter.getId() : null)
        .chapterName(chapter != null ? chapter.getName() : null)
        .createdAt(challenger.getCreatedAt())
        .updatedAt(challenger.getUpdatedAt())
        .build();
  }

  /**
   * 공개용 응답 변환 (민감 정보 제외) studentId 등 민감한 정보를 제외한 공개 정보만 반환
   */
  private ChallengerDto.PublicResponse toPublicResponse(Challenger challenger) {
    // Chapter 정보 조회
    var chapter = chapterRepository.findByChallengerId(challenger.getId())
        .orElse(null);

    return ChallengerDto.PublicResponse.builder()
        .id(challenger.getId())
        .umsbId(challenger.getUmsbId())
        .gisu(challenger.getGisu())
        .part(challenger.getPart())
        .name(challenger.getName())
        .nickname(challenger.getNickname())
        .gender(challenger.getGender())
        .schoolId(challenger.getSchool().getId())
        .schoolName(challenger.getSchool().getName())
        .schoolLogoImageUrl(challenger.getSchool().getLogoImageUrl())
        // studentId 제외 (민감 정보)
        .profileImageUrl(challenger.getProfileImageUrl())
        .chapterId(chapter != null ? chapter.getId() : null)
        .chapterName(chapter != null ? chapter.getName() : null)
        .createdAt(challenger.getCreatedAt())
        .updatedAt(challenger.getUpdatedAt())
        .build();
  }

  /**
   * ID로 Challenger 공개 정보 조회 (민감 정보 제외)
   */
  public ChallengerDto.PublicResponse findByIdPublic(Long id) {
    Challenger challenger = challengerRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.CHALLENGER,
            ErrorStatus.CHALLENGER_NOT_FOUND));
    return toPublicResponse(challenger);
  }

  /**
   * UMSB ID로 Challenger 공개 정보 조회 (민감 정보 제외)
   */
  public ChallengerDto.PublicResponse findByUmsbIdPublic(Long umsbId) {
    Challenger challenger = challengerRepository.findByUmsbId(umsbId)
        .orElseThrow(
            () -> new DomainException(DomainType.CHALLENGER,
                ErrorStatus.CHALLENGER_UMSB_ID_NOT_FOUND));
    return toPublicResponse(challenger);
  }
}
