package kr.kyeoungwoon.upms.domain.projectMatchingRound.scheduler;

import java.time.Instant;
import kr.kyeoungwoon.upms.domain.projectMatchingRound.service.ProjectMatchingRoundAutoDecisionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectMatchingRoundAutoDecisionScheduler {

  private final ProjectMatchingRoundAutoDecisionService autoDecisionService;

  /**
   * 10분 간격으로 자동 합/불 처리를 실행합니다.
   */
//  @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
  @Scheduled(cron = "0 5 * * * *")
  public void executeAutoDecisionJob() {
    Instant now = Instant.now();
    log.info("[매칭 차수 종료 Scheduler] 매칭 차수가 끝났는지 검증합니다. {}", now);
    autoDecisionService.processExpiredRounds(now);
  }
}
