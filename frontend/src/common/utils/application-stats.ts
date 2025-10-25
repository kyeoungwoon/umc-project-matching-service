/**
 * 지원서 통계 계산 유틸리티 함수
 */
import type { ProjectApplication } from '@api/types/application';
import { ApplicationStatusEnum, type ChallengerPart } from '@api/types/common';
import type { ProjectTo } from '@api/types/project';

export interface ProjectApplicationStatusByMatchingRound {
  part: ChallengerPart;
  currentTo: number;
  maxTo: number;
  matchingRoundId: string;
  matchingRoundName: string;
  totalApplicants: number;
  confirmedApplicants: number;
  rejectedApplicants: number;
}

/**
 * ProjectApplication 배열을 파트/매칭라운드별로 그룹화하여 통계 계산
 * @param applications - 프로젝트 지원서 배열
 * @param projectTos - 프로젝트 TO 정보 배열 (optional)
 * @returns 파트 및 매칭라운드별 지원 현황 통계
 */
export function calculateApplicationStatsByPartAndRound(
  applications: ProjectApplication[],
  projectTos?: ProjectTo[],
): ProjectApplicationStatusByMatchingRound[] {
  if (!applications || applications.length === 0) {
    return [];
  }

  // 파트 + 매칭라운드 조합으로 그룹화
  const groupedMap = new Map<string, ProjectApplication[]>();

  applications.forEach((app) => {
    const key = `${app.applicantPart}_${app.matchingRoundId}`;
    if (!groupedMap.has(key)) {
      groupedMap.set(key, []);
    }
    groupedMap.get(key)!.push(app);
  });

  // 각 그룹별로 통계 계산
  const stats: ProjectApplicationStatusByMatchingRound[] = [];

  groupedMap.forEach((apps) => {
    const firstApp = apps[0];
    const part = firstApp.applicantPart;
    const matchingRoundId = firstApp.matchingRoundId;
    const matchingRoundName = firstApp.matchingRoundName;

    // 해당 파트의 TO 정보 찾기
    const projectTo = projectTos?.find((to) => to.part === part);
    const maxTo = projectTo?.toCount ?? 0;

    // 상태별 카운트
    const confirmedApplicants = apps.filter(
      (app) => app.status === ApplicationStatusEnum.CONFIRMED,
    ).length;
    const rejectedApplicants = apps.filter(
      (app) => app.status === ApplicationStatusEnum.REJECTED,
    ).length;
    const totalApplicants = apps.length;

    stats.push({
      part,
      currentTo: confirmedApplicants, // 합격자 수 = 현재 채워진 TO
      maxTo,
      matchingRoundId,
      matchingRoundName,
      totalApplicants,
      confirmedApplicants,
      rejectedApplicants,
    });
  });

  // 매칭라운드, 파트 순으로 정렬
  return stats.sort((a, b) => {
    const roundCompare = a.matchingRoundName.localeCompare(b.matchingRoundName);
    if (roundCompare !== 0) return roundCompare;
    return a.part.localeCompare(b.part);
  });
}

/**
 * 특정 파트로 필터링된 통계 반환
 * @param stats - 통계 배열
 * @param parts - 필터링할 파트 배열
 * @returns 필터링된 통계
 */
export function filterStatsByParts(
  stats: ProjectApplicationStatusByMatchingRound[],
  parts: ChallengerPart[],
): ProjectApplicationStatusByMatchingRound[] {
  if (!parts || parts.length === 0) {
    return stats;
  }
  return stats.filter((stat) => parts.includes(stat.part));
}

/**
 * 특정 매칭라운드로 필터링된 통계 반환
 * @param stats - 통계 배열
 * @param matchingRoundId - 필터링할 매칭라운드 ID
 * @returns 필터링된 통계
 */
export function filterStatsByMatchingRound(
  stats: ProjectApplicationStatusByMatchingRound[],
  matchingRoundId: string,
): ProjectApplicationStatusByMatchingRound[] {
  if (!matchingRoundId) {
    return stats;
  }
  return stats.filter((stat) => stat.matchingRoundId === matchingRoundId);
}
