package org.hse.torrent;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryStorage {
    @Autowired
    private SeedRepository seedRepository;

    @Autowired
    private SharedFileMetadataRepository sharedFileMetadataRepository;

    @Transactional
    public void saveSeedAndFile(List<SharedFileMetadata> files, Seed seed){
        seed.setFiles(files);
        for (var f : files) {
            f.getSeeders().add(seed);
            sharedFileMetadataRepository.save(f);
        }
        seedRepository.save(seed);
    }
}
