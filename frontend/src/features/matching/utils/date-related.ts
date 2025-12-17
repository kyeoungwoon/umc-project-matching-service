export function getTimeRemaining(endDatetime: string, now: Date = new Date()) {
  const end = new Date(endDatetime);
  const diff = end.getTime() - now.getTime();

  if (diff <= 0) {
    return `마감되었습니다.`;
    // return { days: 0, hours: 0, minutes: 0, seconds: 0, totalSeconds: 0 };
  }

  const totalSeconds = Math.floor(diff / 1000);
  const days = Math.floor(totalSeconds / (60 * 60 * 24)); // 24시간으로 나누면 됨
  const hours = Math.floor((totalSeconds % (60 * 60 * 24)) / (60 * 60));
  const minutes = Math.floor((totalSeconds % (60 * 60)) / 60);
  const seconds = totalSeconds % 60;

  return `${String(days).padStart(2, '0')}일 ${String(hours).padStart(2, '0')}시간 ${String(minutes).padStart(2, '0')}분 ${String(seconds).padStart(2, '0')}초`;

  // return { days, hours, minutes, seconds, totalSeconds };
}

export function getProgress(
  startDatetime: string,
  endDatetime: string,
  now: Date = new Date(),
): number {
  const start = new Date(startDatetime);
  const end = new Date(endDatetime);

  if (now < start) {
    return 0; // 아직 시작 전
  }
  if (now > end) {
    return 1; // 이미 끝난 상태
  }
  const totalDuration = end.getTime() - start.getTime();
  const elapsed = now.getTime() - start.getTime();
  return elapsed / totalDuration;
}
