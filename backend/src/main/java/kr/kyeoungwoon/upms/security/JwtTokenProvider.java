package kr.kyeoungwoon.upms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import kr.kyeoungwoon.upms.global.enums.ChapterAdminRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  private final SecretKey secretKey;
  private final long expiration;

  public JwtTokenProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration}") long expiration) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expiration = expiration;
  }

  // 토큰 생성
  public String createToken(Long memberId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .subject(String.valueOf(memberId))
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(secretKey)
        .compact();
  }

  public String createToken(Long memberId, List<ChapterAdminRole> roles) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);

    List<String> roleNames = roles.stream()
        .map(Enum::name)
        .toList();

    return Jwts.builder()
        .subject(String.valueOf(memberId))
        .claim("roles", roleNames)  // ✅ 역할 추가
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(secretKey)
        .compact();
  }

  // 토큰에서 memberId 추출
  public Long getMemberId(String token) {
    Claims claims = parseClaims(token);
    return Long.valueOf(claims.getSubject());
  }

  public List<ChapterAdminRole> getRoles(String token) {
    Claims claims = parseClaims(token);
    @SuppressWarnings("unchecked")
    List<String> roleStrings = claims.get("roles", List.class);

    if (roleStrings == null) {
      return List.of();
    }

    return roleStrings.stream()
        .map(ChapterAdminRole::valueOf)
        .toList();
  }

  // 토큰 유효성 검증
  public boolean validateToken(String token) {
    try {
      parseClaims(token); // 토큰을 파싱해서 올바르면 유효한 것으로 간주
      return true;
    } catch (ExpiredJwtException expiredJwtException) {
      log.info("JWT 토큰이 만료되었습니다: {}", expiredJwtException.getMessage());
      return false;
//      throw new DomainException(DomainType.JWT, ErrorStatus.JWT_ACCESS_TOKEN_EXPIRED);
    } catch (Exception e) {
      log.info("JWT 토큰이 유효하지 않습니다: {}", e.getMessage());
      return false;
//      throw new DomainException(DomainType.JWT, ErrorStatus.AUTHORIZATION_EXCEPTION);
    }
  }

  // 토큰에서 Claims 파싱
  private Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}