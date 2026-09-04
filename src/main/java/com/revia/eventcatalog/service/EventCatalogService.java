package com.revia.eventcatalog.service;

import com.revia.eventcatalog.entity.EventCatalogEntry;
import com.revia.eventcatalog.repository.EventCatalogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class EventCatalogService {

    private final EventCatalogRepository eventCatalogRepository;

    public EventCatalogService(
            EventCatalogRepository eventCatalogRepository) {

        this.eventCatalogRepository = eventCatalogRepository;
    }

    @Transactional
    public EventCatalogEntry indexEvent(
            String topic,
            int partition,
            long offset,
            String key,
            String eventType,
            Instant eventTimestamp,
            String traceId) {

        return eventCatalogRepository
                .findByTopicAndPartitionAndOffset(
                        topic,
                        partition,
                        offset
                )
                .orElseGet(() -> {

                    EventCatalogEntry entry =
                            new EventCatalogEntry(
                                    topic,
                                    partition,
                                    offset,
                                    key,
                                    eventType,
                                    eventTimestamp,
                                    Instant.now(),
                                    traceId
                            );

                    return eventCatalogRepository.save(entry);
                });
    }

    @Transactional(readOnly = true)
    public Page<EventCatalogEntry> search(
            String topic,
            Integer partition,
            Instant from,
            Instant to,
            String eventType,
            String key,
            String traceId,
            int page,
            int size) {

        validateSearchParameters(
                partition,
                from,
                to,
                page,
                size
        );

        Specification<EventCatalogEntry> specification =
                buildSpecification(
                        topic,
                        partition,
                        from,
                        to,
                        eventType,
                        key,
                        traceId
                );

        Sort sort = Sort.by(
                Sort.Order.asc("partition"),
                Sort.Order.asc("offset")
        );

        return eventCatalogRepository.findAll(
                specification,
                PageRequest.of(page, size, sort)
        );
    }

    private Specification<EventCatalogEntry> buildSpecification(
            String topic,
            Integer partition,
            Instant from,
            Instant to,
            String eventType,
            String key,
            String traceId) {

        Specification<EventCatalogEntry> specification = null;

        if (topic != null && !topic.isBlank()) {
            specification = addPredicate(
                    specification,
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("topic"),
                                    topic
                            )
            );
        }

        if (partition != null) {
            specification = addPredicate(
                    specification,
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("partition"),
                                    partition
                            )
            );
        }

        if (from != null) {
            specification = addPredicate(
                    specification,
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("eventTimestamp"),
                                    from
                            )
            );
        }

        if (to != null) {
            specification = addPredicate(
                    specification,
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("eventTimestamp"),
                                    to
                            )
            );
        }

        if (eventType != null && !eventType.isBlank()) {
            specification = addPredicate(
                    specification,
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("eventType"),
                                    eventType
                            )
            );
        }

        if (key != null && !key.isBlank()) {
            specification = addPredicate(
                    specification,
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("key"),
                                    key
                            )
            );
        }

        if (traceId != null && !traceId.isBlank()) {
            specification = addPredicate(
                    specification,
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("traceId"),
                                    traceId
                            )
            );
        }

        return specification;
    }

    private Specification<EventCatalogEntry> addPredicate(
            Specification<EventCatalogEntry> current,
            Specification<EventCatalogEntry> next) {

        if (current == null) {
            return next;
        }

        return current.and(next);
    }

    private void validateSearchParameters(
            Integer partition,
            Instant from,
            Instant to,
            int page,
            int size) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page must be greater than or equal to 0"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Size must be between 1 and 100"
            );
        }

        if (partition != null && partition < 0) {
            throw new IllegalArgumentException(
                    "Partition must be greater than or equal to 0"
            );
        }

        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "From timestamp must be before or equal to to timestamp"
            );
        }
    }
}   