package OnlineTicket.controller;

import OnlineTicket.dto.seats.SeatResponse;
import OnlineTicket.exception.ResourceNotFoundException;
import OnlineTicket.model.entity.Event;
import OnlineTicket.model.entity.Seat;
import OnlineTicket.repository.EventRepository;
import OnlineTicket.repository.SeatRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/seats")
public class SeatController {
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    public SeatController(EventRepository eventRepository, SeatRepository seatRepository) {
        this.eventRepository = eventRepository;
        this.seatRepository = seatRepository;
    }

    @GetMapping("/event/{eventId}")
    public List<SeatResponse> byEvent(@PathVariable Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        return seatRepository.findByEvent(event).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private SeatResponse toResponse(Seat s) {
        SeatResponse r = new SeatResponse();
        r.setId(s.getId());
        r.setRowLabel(s.getRowLabel());
        r.setSeatNumber(s.getSeatNumber());
        r.setStatus(s.getStatus().name());
        r.setPrice(s.getPrice());
        return r;
    }
}


