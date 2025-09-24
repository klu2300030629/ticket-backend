package OnlineTicket.service;

import OnlineTicket.dto.events.EventCreateUpdateRequest;
import OnlineTicket.dto.events.EventResponse;
import OnlineTicket.exception.ResourceNotFoundException;
import OnlineTicket.model.entity.Category;
import OnlineTicket.model.entity.Event;
import OnlineTicket.repository.CategoryRepository;
import OnlineTicket.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    public EventService(EventRepository eventRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<EventResponse> getAll() {
        return eventRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<EventResponse> getByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryName));
        return eventRepository.findByCategory(category).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<EventResponse> getTrending() {
        return eventRepository.findByTrendingTrue().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<EventResponse> getUpcoming() {
        return eventRepository.findByStartTimeAfterOrderByStartTimeAsc(Instant.now())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<EventResponse> search(String q, String category) {
        String query = (q == null || q.isBlank()) ? null : q;
        String cat = (category == null || category.isBlank()) ? null : category;
        return eventRepository.search(query, cat).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public EventResponse getOne(Long id) {
        Event e = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + id));
        return toResponse(e);
    }

    public EventResponse create(EventCreateUpdateRequest req) {
        Category category = categoryRepository.findByName(req.getCategory())
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setName(req.getCategory());
                    return categoryRepository.save(c);
                });
        Event e = new Event();
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setVenue(req.getVenue());
        e.setStartTime(req.getStartTime());
        e.setEndTime(req.getEndTime());
        e.setTrending(req.isTrending());
        e.setCategory(category);
        return toResponse(eventRepository.save(e));
    }

    public EventResponse update(Long id, EventCreateUpdateRequest req) {
        Event e = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + id));
        Category category = categoryRepository.findByName(req.getCategory())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + req.getCategory()));
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setVenue(req.getVenue());
        e.setStartTime(req.getStartTime());
        e.setEndTime(req.getEndTime());
        e.setTrending(req.isTrending());
        e.setCategory(category);
        return toResponse(eventRepository.save(e));
    }

    public void delete(Long id) {
        if (!eventRepository.existsById(id)) throw new ResourceNotFoundException("Event not found: " + id);
        eventRepository.deleteById(id);
    }

    private EventResponse toResponse(Event e) {
        EventResponse r = new EventResponse();
        r.setId(e.getId());
        r.setTitle(e.getTitle());
        r.setDescription(e.getDescription());
        r.setVenue(e.getVenue());
        r.setStartTime(e.getStartTime());
        r.setEndTime(e.getEndTime());
        r.setTrending(e.isTrending());
        r.setCategory(e.getCategory() != null ? e.getCategory().getName() : null);
        return r;
    }
}


