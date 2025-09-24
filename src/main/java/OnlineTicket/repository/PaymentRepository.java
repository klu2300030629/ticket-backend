package OnlineTicket.repository;

import OnlineTicket.model.entity.Payment;
import OnlineTicket.model.entity.Booking;
import OnlineTicket.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBooking(Booking booking);
    long countByStatus(PaymentStatus status);
}


