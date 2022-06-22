package org.hse.torrent;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedFileMetadataRepository extends JpaRepository<SharedFileMetadata, Integer> {}
