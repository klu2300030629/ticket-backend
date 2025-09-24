package OnlineTicket.controller;

import OnlineTicket.dto.events.EventCreateUpdateRequest;
import OnlineTicket.dto.events.EventResponse;
import OnlineTicket.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventResponse> all() { return eventService.getAll(); }

    @GetMapping("/category/{name}")
    public List<EventResponse> byCategory(@PathVariable String name) { return eventService.getByCategory(name); }

    @GetMapping("/trending")
    public List<EventResponse> trending() { return eventService.getTrending(); }

    @GetMapping("/upcoming")
    public List<EventResponse> upcoming() { return eventService.getUpcoming(); }

    @GetMapping("/search")
    public List<EventResponse> search(@RequestParam(required = false) String q,
                                      @RequestParam(required = false) String category) {
        return eventService.search(q, category);
    }

    @GetMapping("/{id}")
    public EventResponse one(@PathVariable Long id) { return eventService.getOne(id); }

    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventCreateUpdateRequest req) {
        return ResponseEntity.ok(eventService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(@PathVariable Long id, @Valid @RequestBody EventCreateUpdateRequest req) {
        return ResponseEntity.ok(eventService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


