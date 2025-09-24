package OnlineTicket.controller;

import OnlineTicket.dto.booking.BookingResponse;
import OnlineTicket.model.entity.User;
import OnlineTicket.model.enums.BookingStatus;
import OnlineTicket.repository.BookingRepository;
import OnlineTicket.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/bookings")
public class UserBookingController {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public UserBookingController(BookingRepository bookingRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<BookingResponse> list(@PathVariable Long userId,
                                      @RequestParam(required = false) BookingStatus status,
                                      @RequestParam(required = false) Instant after,
                                      @RequestParam(required = false) Instant before) {
        User user = userRepository.findById(userId).orElseThrow();
        return bookingRepository.findByUser(user).stream()
                .filter(b -> status == null || b.getStatus() == status)
                .filter(b -> after == null || b.getCreatedAt().isAfter(after))
                .filter(b -> before == null || b.getCreatedAt().isBefore(before))
                .map(b -> {
                    BookingResponse r = new BookingResponse();
                    r.setId(b.getId());
                    r.setUserId(b.getUser().getId());
                    r.setEventId(b.getEvent().getId());
                    r.setSeatIds(b.getSeats().stream().map(s -> s.getId()).collect(java.util.stream.Collectors.toSet()));
                    r.setStatus(b.getStatus().name());
                    r.setTotalAmount(b.getTotalAmount());
                    r.setCreatedAt(b.getCreatedAt());
                    return r;
                })
                .collect(Collectors.toList());
    }
}


