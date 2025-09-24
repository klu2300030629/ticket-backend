package OnlineTicket.repository;

import OnlineTicket.model.entity.Booking;
import OnlineTicket.model.entity.User;
import OnlineTicket.model.entity.Event;
import OnlineTicket.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByEvent(Event event);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByCreatedAtAfter(Instant after);
}


