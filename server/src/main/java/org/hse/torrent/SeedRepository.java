package org.hse.torrent;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SeedRepository extends JpaRepository<Seed, String> {
}
