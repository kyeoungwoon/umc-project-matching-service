package kr.kyeoungwoon.upms.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Spring Security에서 사용할 역할 enum ChapterAdminRole과 별개로 시스템 전체 역할을 정의
 */
@Getter
@RequiredArgsConstructor
public enum SecurityRole {
  // 기본 역할
  USER("ROLE_USER", "인증된 사용자"),
  ADMIN("ROLE_ADMIN", "관리자 (ChapterAdmin 레코드 보유자)"),

  // ChapterAdminRole 기반 세부 역할
  CENTRAL_LEAD("ROLE_CENTRAL_LEAD", "중앙 대표"),
  CENTRAL_DEPARTMENT_LEAD("ROLE_CENTRAL_DEPARTMENT_LEAD", "중앙 부서장"),
  CENTRAL_ADMIN("ROLE_CENTRAL_ADMIN", "중앙 운영진"),
  CHAPTER_LEAD("ROLE_CHAPTER_LEAD", "지부장"),
  SCHOOL_LEAD("ROLE_SCHOOL_LEAD", "학교 대표"),
  SCHOOL_VICE_LEAD("ROLE_SCHOOL_VICE_LEAD", "학교 부대표"),
  SCHOOL_ADMIN("ROLE_SCHOOL_ADMIN", "학교 운영진");

  private final String authority;
  private final String description;

  /**
   * ChapterAdminRole을 SecurityRole로 변환
   */
  public static SecurityRole from(ChapterAdminRole role) {
    return switch (role) {
      case CENTRAL_LEAD -> CENTRAL_LEAD;
      case CENTRAL_DEPARTMENT_LEAD -> CENTRAL_DEPARTMENT_LEAD;
      case CENTRAL_ADMIN -> CENTRAL_ADMIN;
      case CHAPTER_LEAD -> CHAPTER_LEAD;
      case SCHOOL_LEAD -> SCHOOL_LEAD;
      case SCHOOL_VICE_LEAD -> SCHOOL_VICE_LEAD;
      case SCHOOL_ADMIN -> SCHOOL_ADMIN;
    };
  }
}

