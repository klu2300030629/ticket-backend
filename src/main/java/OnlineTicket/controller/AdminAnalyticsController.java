package OnlineTicket.controller;

import OnlineTicket.repository.BookingRepository;
import OnlineTicket.repository.EventRepository;
import OnlineTicket.repository.PaymentRepository;
import OnlineTicket.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/analytics")
public class AdminAnalyticsController {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    public AdminAnalyticsController(EventRepository eventRepository, UserRepository userRepository, BookingRepository bookingRepository, PaymentRepository paymentRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        long events = eventRepository.count();
        long users = userRepository.count();
        long bookings = bookingRepository.count();
        long payments = paymentRepository.count();
        return Map.of(
                "events", events,
                "users", users,
                "bookings", bookings,
                "payments", payments
        );
    }
}


