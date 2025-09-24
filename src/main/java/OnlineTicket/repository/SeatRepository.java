package OnlineTicket.repository;

import OnlineTicket.model.entity.Seat;
import OnlineTicket.model.entity.Event;
import OnlineTicket.model.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByEvent(Event event);
    List<Seat> findByEventAndStatus(Event event, SeatStatus status);
    List<Seat> findByIdIn(Set<Long> ids);
}


