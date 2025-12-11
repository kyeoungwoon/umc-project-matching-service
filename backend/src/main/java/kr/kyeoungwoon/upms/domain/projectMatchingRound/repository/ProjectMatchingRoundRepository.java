package kr.kyeoungwoon.upms.domain.projectMatchingRound.repository;

import java.time.Instant;
import java.util.Optional;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.entity.ProjectMatchingRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMatchingRoundRepository extends JpaRepository<ProjectMatchingRound, Long> {

  /**
   * 현재 진행 중인 매칭 라운드 조회 (현재 시간이 startAt과 endAt 사이)
   */
  @Query("SELECT mr FROM ProjectMatchingRound mr "
      + "WHERE mr.chapter.id = :chapterId "
      + "AND :now BETWEEN mr.startAt AND mr.endAt "
      + "ORDER BY mr.startAt ASC "
      + "LIMIT 1")
  Optional<ProjectMatchingRound> findCurrentMatchingRound(
      @Param("chapterId") Long chapterId,
      @Param("now") Instant now);

  /**
   * 특정 Chapter의 현재 진행 중이거나 가장 가까운 미래의 매칭 라운드 조회 1순위: 현재 진행 중인 라운드 2순위: 아직 시작하지 않은 가장 가까운 라운드
   */
  @Query("SELECT mr FROM ProjectMatchingRound mr "
      + "WHERE mr.chapter.id = :chapterId AND mr.endAt >= :now "
      + "ORDER BY CASE WHEN :now BETWEEN mr.startAt AND mr.endAt THEN 0 ELSE 1 END, "
      + "mr.startAt ASC "
      + "LIMIT 1")
  Optional<ProjectMatchingRound> findCurrentOrUpcomingByChapterId(
      @Param("chapterId") Long chapterId,
      @Param("now") Instant now);

  /**
   * 특정 Chapter의 특정 기간 내 매칭 라운드 목록 조회 (startAt이 검색 기간 내에 있거나, endAt이 검색 기간 내에 있거나, 검색 기간을 포함하는 경우)
   */
  @Query("SELECT mr FROM ProjectMatchingRound mr "
      + "WHERE mr.chapter.id = :chapterId "
      + "AND (mr.startAt BETWEEN :startTime AND :endTime "
      + "OR mr.endAt BETWEEN :startTime AND :endTime "
      + "OR (mr.startAt <= :startTime AND mr.endAt >= :endTime)) "
      + "ORDER BY mr.startAt ASC")
  java.util.List<ProjectMatchingRound> findByChapterIdAndTimeBetween(
      @Param("chapterId") Long chapterId,
      @Param("startTime") Instant startTime,
      @Param("endTime") Instant endTime);

  /**
   * 특정 Chapter에서 기간이 중복되는 매칭 라운드가 있는지 확인 (생성 시 사용)
   */
  @Query("SELECT CASE WHEN COUNT(mr) > 0 THEN true ELSE false END FROM ProjectMatchingRound mr "
      + "WHERE mr.chapter.id = :chapterId "
      + "AND (mr.startAt < :endTime AND mr.endAt > :startTime)")
  boolean existsOverlappingRound(
      @Param("chapterId") Long chapterId,
      @Param("startTime") Instant startTime,
      @Param("endTime") Instant endTime);

  /**
   * 특정 Chapter에서 기간이 중복되는 매칭 라운드가 있는지 확인 (수정 시 사용 - 자기 자신 제외)
   */
  @Query("SELECT CASE WHEN COUNT(mr) > 0 THEN true ELSE false END FROM ProjectMatchingRound mr "
      + "WHERE mr.chapter.id = :chapterId "
      + "AND mr.id != :excludeId "
      + "AND (mr.startAt < :endTime AND mr.endAt > :startTime)")
  boolean existsOverlappingRoundExcludingSelf(
      @Param("chapterId") Long chapterId,
      @Param("excludeId") Long excludeId,
      @Param("startTime") Instant startTime,
      @Param("endTime") Instant endTime);

  /**
   * 특정 Chapter의 모든 매칭 라운드 조회
   */
  java.util.List<ProjectMatchingRound> findByChapterId(Long chapterId);

  /**
   * 자동 합/불 처리가 필요한 매칭 라운드 조회 (결정 마감이 지났고, 아직 처리되지 않은 경우)
   */
  @Query("SELECT mr FROM ProjectMatchingRound mr "
      + "WHERE mr.decisionDeadlineAt <= :now "
      + "AND mr.isAutoDecisionExecuted = false")
  java.util.List<ProjectMatchingRound> findRoundsToAutoDecide(@Param("now") Instant now);
}
