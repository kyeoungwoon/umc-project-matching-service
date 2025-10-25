package kr.kyeoungwoon.upms.global.config;


import java.util.Arrays;
import java.util.List;
import kr.kyeoungwoon.upms.security.ApiAccessDeniedHandler;
import kr.kyeoungwoon.upms.security.ApiAuthenticationEntryPoint;
import kr.kyeoungwoon.upms.security.CustomAuthorizationManager;
import kr.kyeoungwoon.upms.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize, @PostAuthorize 활성화
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomAuthorizationManager customAuthorizationManager;
  private final ApiAuthenticationEntryPoint authenticationEntryPoint;
  private final ApiAccessDeniedHandler accessDeniedHandler;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)   // 폼 로그인 비활성화
        .httpBasic(AbstractHttpConfigurer::disable)   // HTTP Basic 비활성
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // Health Check
            .requestMatchers("/actuator/**").permitAll()
            // Swagger API
            .requestMatchers(
                "/swagger-ui/**",
                "/swagger",
                "/v3/api-docs/**",
                "/api-docs/**",
                "/swagger-resources/**",
                "/webjars/**"
            ).permitAll()
            .anyRequest().access(customAuthorizationManager)  // 커스텀 매니저 사용
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(authenticationEntryPoint) // 인증 실패 시
            .accessDeniedHandler(accessDeniedHandler)           // 인가 실패 시
        );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {

//    return new BCryptPasswordEncoder();
    // BCrypt에서 Argon2로 변경
    return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
  }

  /**
   * UserDetailsService Bean을 제공하여 Spring Security의 기본 사용자 자동 생성 방지 이 애플리케이션은 JWT 기반 인증을 사용하므로
   * UserDetailsService는 실제로 호출되지 않음 Spring Security가 "인증 체계가 구성되어 있다"고 인식하도록 하기 위한 더미 Bean
   */
  @Bean
  public UserDetailsService userDetailsService() {
    return username -> {
      throw new UsernameNotFoundException(
          "This application uses JWT authentication, not UserDetailsService");
    };
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(
        Arrays.asList("http://localhost:3000", "https://upms.haru.it.kr",
            "https://upms.kyeoungwoon.kr", "https://dev-upms.kyeoungwoon.kr/"));
    configuration.setAllowedMethods(List.of("*"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}