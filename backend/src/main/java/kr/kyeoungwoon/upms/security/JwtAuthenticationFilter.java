package kr.kyeoungwoon.upms.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kr.kyeoungwoon.upms.domain.challenger.repository.ChapterAdminRepository;
import kr.kyeoungwoon.upms.global.enums.ChapterAdminRole;
import kr.kyeoungwoon.upms.global.enums.SecurityRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final ChapterAdminRepository chapterAdminRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String token = resolveToken(request); // 토큰 추출

//    if (token == null) {
//      throw new DomainException(DomainType.JWT, ErrorStatus.JWT_TOKEN_NOT_RECEIVED);
//    }

    if (token != null && jwtTokenProvider.validateToken(token)) {
      Long challengerId = jwtTokenProvider.getMemberId(token);
      List<ChapterAdminRole> roles = jwtTokenProvider.getRoles(token);

      UserPrincipal principal = UserPrincipal.builder()
          .challengerId(challengerId)
          .roles(roles)
          .build();

      // GrantedAuthority 목록 생성 (SecurityRole enum 사용)
      List<GrantedAuthority> authorities = buildAuthorities(challengerId, roles);

      // 인증 객체 생성
      // principal, credentials, authorities (누구, 어떻게, 권한)
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          principal,  // principal: memberId 저장
          null,       // credentials: email 저장
          authorities // authorities: 권한 목록
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

  /**
   * ChapterAdmin 레코드 존재 여부와 역할을 기반으로 GrantedAuthority 목록 생성 SecurityRole enum을 사용하여 타입 안정성 보장
   */
  private List<GrantedAuthority> buildAuthorities(Long challengerId, List<ChapterAdminRole> roles) {
    List<GrantedAuthority> authorities = new ArrayList<>();

    // 모든 인증된 사용자는 USER 역할 부여
    authorities.add(new SimpleGrantedAuthority(SecurityRole.USER.getAuthority()));

    // JWT에 roles가 있으면 해당 역할 추가
    if (roles != null && !roles.isEmpty()) {
      authorities.add(new SimpleGrantedAuthority(SecurityRole.ADMIN.getAuthority()));

      // 세부 역할도 추가 (ROLE_CHAPTER_LEAD, ROLE_SCHOOL_LEAD 등)
      for (ChapterAdminRole role : roles) {
        SecurityRole securityRole = SecurityRole.from(role);
        authorities.add(new SimpleGrantedAuthority(securityRole.getAuthority()));
      }
    } else {
      // JWT에 roles가 없으면 DB에서 확인
      boolean isAdmin = chapterAdminRepository.existsByChallengerId(challengerId);
      if (isAdmin) {
        authorities.add(new SimpleGrantedAuthority(SecurityRole.ADMIN.getAuthority()));
      }
    }

    return authorities;
  }

  /**
   * Request Header에서 Bearer Token 추출
   */
  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }

    return null;
  }
}