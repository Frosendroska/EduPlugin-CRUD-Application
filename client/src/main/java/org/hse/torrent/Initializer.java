package org.hse.torrent;

import java.util.concurrent.atomic.AtomicReference;
import org.hse.torrent.state.FileState;
import org.hse.torrent.state.State;
import org.hse.torrent.state.StatePersistence;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Как только запустили клиента, создаем bean, который дозагрузит файл
 */
@Component
public class Initializer {

    @Bean
    public InitializerRunner getInitializerRunner() {
        return new InitializerRunner();
    }

    /* Allows to make a bean */
    private static class InitializerRunner implements InitializingBean {

        @Autowired
        private StatePersistence statePersistence;

        @Autowired
        private FileSeedUpdater fileSeedUpdater;

        @Autowired
        private FileDownloader fileDownloader;

        @Autowired
        State state;

        @Override
        public void afterPropertiesSet() {
            statePersistence.load();

            // Download every file from state
            state.getAllFileIds().forEach(fileId -> {
                AtomicReference<FileState> fileStateReference =
                        state.getFileStateReference(fileId).orElseThrow();

                // Start 2 threads for downloading that we haven't downloaded yet
                fileSeedUpdater.start(fileStateReference);
                fileDownloader.start(fileStateReference);
            });
        }
    }
}
