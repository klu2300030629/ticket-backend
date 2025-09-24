package OnlineTicket.dto.booking;

import java.time.Instant;
import java.util.Set;

public class BookingResponse {
    private Long id;
    private Long userId;
    private Long eventId;
    private Set<Long> seatIds;
    private String status;
    private Double totalAmount;
    private Instant createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public Set<Long> getSeatIds() { return seatIds; }
    public void setSeatIds(Set<Long> seatIds) { this.seatIds = seatIds; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}


