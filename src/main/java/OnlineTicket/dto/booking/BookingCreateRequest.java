package OnlineTicket.dto.booking;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public class BookingCreateRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long eventId;
    @NotEmpty
    private Set<Long> seatIds;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public Set<Long> getSeatIds() { return seatIds; }
    public void setSeatIds(Set<Long> seatIds) { this.seatIds = seatIds; }
}


