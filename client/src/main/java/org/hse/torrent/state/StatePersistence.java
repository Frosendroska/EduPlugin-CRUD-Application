package org.hse.torrent.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StatePersistence {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${config.stateFilePath}") // From resources/application.properties
    private String stateFilePath;

    @Autowired
    State state;

    /**
     * Загружаем все FileState из файла
     */
    public void load() {
        try {
            state.loadSnapshot(
                    objectMapper.readValue(Files.readString(Path.of(stateFilePath)), PersistedState.class).state);
        } catch (IOException e) {
            // File is missing or corrupted, re-write it.
            e.printStackTrace();
        }
    }

    /**
     * Сохраняем все FileState в файл
     */
    public void persist() {
        try {
            List<FileState> snapshot = state.getSnapshot();
            if (snapshot.isEmpty()) {
                // To avoid remaking before loading.
                return;
            }
            Files.writeString(Path.of(stateFilePath), objectMapper.writeValueAsString(new PersistedState(snapshot)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Can not call .class from generic */
    private static class PersistedState {

        private List<FileState> state = new ArrayList<>();

        public PersistedState() {}

        public PersistedState(List<FileState> state) {
            this.state = state;
        }

        public List<FileState> getState() {
            return state;
        }
    }
}
