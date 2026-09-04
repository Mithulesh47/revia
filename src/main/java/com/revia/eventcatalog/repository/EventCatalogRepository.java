package com.revia.eventcatalog.repository;

import com.revia.eventcatalog.entity.EventCatalogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface EventCatalogRepository
        extends JpaRepository<EventCatalogEntry, UUID>,
        JpaSpecificationExecutor<EventCatalogEntry> {

    boolean existsByTopicAndPartitionAndOffset(
            String topic,
            int partition,
            long offset
    );

    Optional<EventCatalogEntry> findByTopicAndPartitionAndOffset(
            String topic,
            int partition,
            long offset
    );
}