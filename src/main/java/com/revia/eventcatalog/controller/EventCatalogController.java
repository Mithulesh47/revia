package com.revia.eventcatalog.controller;

import com.revia.eventcatalog.entity.EventCatalogEntry;
import com.revia.eventcatalog.service.EventCatalogService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/events")
public class EventCatalogController {

    private final EventCatalogService eventCatalogService;

    public EventCatalogController(
            EventCatalogService eventCatalogService) {

        this.eventCatalogService = eventCatalogService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventCatalogEntry indexEvent(
            @Valid @RequestBody IndexEventRequest request) {

        return eventCatalogService.indexEvent(
                request.topic(),
                request.partition(),
                request.offset(),
                request.key(),
                request.eventType(),
                request.eventTimestamp(),
                request.traceId()
        );
    }

    @GetMapping
    public Page<EventCatalogEntry> searchEvents(
            @RequestParam(required = false)
            String topic,

            @RequestParam(required = false)
            Integer partition,

            @RequestParam(required = false)
            Instant from,

            @RequestParam(required = false)
            Instant to,

            @RequestParam(required = false)
            String eventType,

            @RequestParam(required = false)
            String key,

            @RequestParam(required = false)
            String traceId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "50")
            int size) {

        return eventCatalogService.search(
                topic,
                partition,
                from,
                to,
                eventType,
                key,
                traceId,
                page,
                size
        );
    }

    public record IndexEventRequest(

            @NotBlank
            String topic,

            @Min(0)
            int partition,

            @Min(0)
            long offset,

            String key,

            String eventType,

            @NotNull
            Instant eventTimestamp,

            String traceId
    ) {}
}