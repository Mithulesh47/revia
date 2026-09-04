package com.revia.eventcatalog.indexer;

import com.revia.eventcatalog.service.EventCatalogService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Component
public class EventCatalogKafkaListener {

    private final EventCatalogService eventCatalogService;

    public EventCatalogKafkaListener(
            EventCatalogService eventCatalogService) {

        this.eventCatalogService = eventCatalogService;
    }

    @KafkaListener(
            topics = "orders",
            groupId = "${revia.kafka.indexer.group-id}"
    )
    public void consume(ConsumerRecord<String, String> record) {

        String eventType = extractHeader(
                record,
                "event-type"
        );

        String traceId = extractHeader(
                record,
                "trace-id"
        );

        Instant eventTimestamp =
                record.timestamp() >= 0
                        ? Instant.ofEpochMilli(record.timestamp())
                        : Instant.now();

        eventCatalogService.indexEvent(
                record.topic(),
                record.partition(),
                record.offset(),
                record.key(),
                eventType,
                eventTimestamp,
                traceId
        );
    }

    private String extractHeader(
            ConsumerRecord<String, String> record,
            String headerName) {

        var header = record.headers().lastHeader(headerName);

        if (header == null) {
            return null;
        }

        return new String(
                header.value(),
                StandardCharsets.UTF_8
        );
    }
}