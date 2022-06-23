package org.hse.torrent;

import java.time.Duration;
import org.hse.torrent.state.StatePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class ClientSchedulerConfig {

    private static final Duration LIVENESS_RUN_INTERVAL = Duration.ofMinutes(1);
    private static final Duration STATE_PERSISTENCE_INTERVAL = Duration.ofMinutes(1);
    private static final int TASK_POOL_SIZE = 5;

    @Autowired
    LivenessReporter livenessReporter;

    @Autowired
    StatePersistence statePersistence;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(TASK_POOL_SIZE);

        threadPoolTaskScheduler.initialize();

        /* Must not throw an exception */
        threadPoolTaskScheduler.scheduleAtFixedRate(livenessReporter::reportSeeds, LIVENESS_RUN_INTERVAL);
        threadPoolTaskScheduler.scheduleAtFixedRate(statePersistence::persist, STATE_PERSISTENCE_INTERVAL);

        return threadPoolTaskScheduler;
    }
}
