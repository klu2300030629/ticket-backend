package OnlineTicket.controller;

import OnlineTicket.repository.BookingRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminRevenueController {
    private final BookingRepository bookingRepository;

    public AdminRevenueController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/revenue")
    public Map<String, Object> revenue() {
        BigDecimal total = bookingRepository.findAll().stream()
                .map(b -> {
                    Object amt = b.getTotalAmount();
                    if (amt == null) return BigDecimal.ZERO;
                    if (amt instanceof BigDecimal) return (BigDecimal) amt;
                    if (amt instanceof Number) return BigDecimal.valueOf(((Number) amt).doubleValue());
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Map.of("totalRevenue", total);
    }
}


