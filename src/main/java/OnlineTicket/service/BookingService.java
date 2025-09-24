package OnlineTicket.service;

import OnlineTicket.dto.booking.BookingCreateRequest;
import OnlineTicket.dto.booking.BookingResponse;
import OnlineTicket.exception.ResourceNotFoundException;
import OnlineTicket.model.entity.*;
import OnlineTicket.model.enums.BookingStatus;
import OnlineTicket.model.enums.SeatStatus;
import OnlineTicket.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, EventRepository eventRepository, SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.seatRepository = seatRepository;
    }

    public BookingResponse create(BookingCreateRequest req) {
        User user = userRepository.findById(req.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Event event = eventRepository.findById(req.getEventId()).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        Set<Seat> seats = seatRepository.findByIdIn(req.getSeatIds()).stream().collect(Collectors.toSet());
        if (seats.size() != req.getSeatIds().size()) throw new IllegalStateException("Some seats do not exist");
        for (Seat s : seats) {
            if (!s.getEvent().getId().equals(event.getId())) throw new IllegalStateException("Seat does not belong to event");
            if (s.getStatus() != SeatStatus.AVAILABLE) throw new IllegalStateException("Seat not available");
        }
        double total = seats.stream().mapToDouble(Seat::getPrice).sum();
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setSeats(seats);
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalAmount(total);
        Booking saved = bookingRepository.save(booking);
        // Reserve seats
        for (Seat s : seats) {
            s.setStatus(SeatStatus.RESERVED);
        }
        seatRepository.saveAll(seats);
        return toResponse(saved);
    }

    public BookingResponse confirm(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(BookingStatus.CONFIRMED);
        // Mark seats booked
        for (Seat s : booking.getSeats()) {
            s.setStatus(SeatStatus.BOOKED);
        }
        seatRepository.saveAll(booking.getSeats());
        return toResponse(bookingRepository.save(booking));
    }

    public BookingResponse cancel(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(BookingStatus.CANCELLED);
        // Release seats
        for (Seat s : booking.getSeats()) {
            s.setStatus(SeatStatus.AVAILABLE);
        }
        seatRepository.saveAll(booking.getSeats());
        return toResponse(bookingRepository.save(booking));
    }

    private BookingResponse toResponse(Booking b) {
        BookingResponse r = new BookingResponse();
        r.setId(b.getId());
        r.setUserId(b.getUser().getId());
        r.setEventId(b.getEvent().getId());
        r.setSeatIds(b.getSeats().stream().map(Seat::getId).collect(Collectors.toSet()));
        r.setStatus(b.getStatus().name());
        r.setTotalAmount(b.getTotalAmount());
        r.setCreatedAt(b.getCreatedAt());
        return r;
    }
}


