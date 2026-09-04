package com.revia.eventcatalog.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "event_catalog",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_event_catalog_topic_partition_offset",
                        columnNames = {
                                "topic",
                                "partition_number",
                                "kafka_offset"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_event_catalog_topic",
                        columnList = "topic"
                ),
                @Index(
                        name = "idx_event_catalog_event_timestamp",
                        columnList = "event_timestamp"
                ),
                @Index(
                        name = "idx_event_catalog_trace_id",
                        columnList = "trace_id"
                ),
                @Index(
                        name = "idx_event_catalog_key",
                        columnList = "event_key"
                )
        }
)
public class EventCatalogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "topic", nullable = false, length = 255)
    private String topic;

    @Column(name = "partition_number", nullable = false)
    private int partition;

    @Column(name = "kafka_offset", nullable = false)
    private long offset;

    @Column(name = "event_key", length = 1000)
    private String key;

    @Column(name = "event_type", length = 255)
    private String eventType;

    @Column(name = "event_timestamp", nullable = false)
    private Instant eventTimestamp;

    @Column(name = "indexed_at", nullable = false)
    private Instant indexedAt;

    @Column(name = "trace_id", length = 255)
    private String traceId;

    protected EventCatalogEntry() {
    }

    public EventCatalogEntry(
            String topic,
            int partition,
            long offset,
            String key,
            String eventType,
            Instant eventTimestamp,
            Instant indexedAt,
            String traceId) {

        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
        this.key = key;
        this.eventType = eventType;
        this.eventTimestamp = eventTimestamp;
        this.indexedAt = indexedAt;
        this.traceId = traceId;
    }

    public UUID getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public int getPartition() {
        return partition;
    }

    public long getOffset() {
        return offset;
    }

    public String getKey() {
        return key;
    }

    public String getEventType() {
        return eventType;
    }

    public Instant getEventTimestamp() {
        return eventTimestamp;
    }

    public Instant getIndexedAt() {
        return indexedAt;
    }

    public String getTraceId() {
        return traceId;
    }
}