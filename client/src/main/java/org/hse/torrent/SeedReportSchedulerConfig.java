package org.hse.torrent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class SeedReportSchedulerConfig {

    private static final int TASK_RUN_INTERVAL_MS = 2000;
    private static final int TASK_POOL_SIZE = 5;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(TASK_POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix("reportSeedScheduler");

        threadPoolTaskScheduler.initialize();
        threadPoolTaskScheduler.scheduleAtFixedRate(
                () -> new reportSeed(), TASK_RUN_INTERVAL_MS);

        return threadPoolTaskScheduler;
    }
}
