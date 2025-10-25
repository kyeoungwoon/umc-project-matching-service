package kr.kyeoungwoon.upms.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * 지부장 이상만 접근 가능 (CENTRAL_*, CHAPTER_LEAD)
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('CENTRAL_LEAD', 'CENTRAL_DEPARTMENT_LEAD', 'CENTRAL_ADMIN', 'CHAPTER_LEAD')")
public @interface ChapterLeadOnly {

}

