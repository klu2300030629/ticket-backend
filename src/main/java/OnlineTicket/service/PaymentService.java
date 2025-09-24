package OnlineTicket.service;

import OnlineTicket.dto.payment.PaymentRequest;
import OnlineTicket.dto.payment.PaymentResponse;
import OnlineTicket.exception.ResourceNotFoundException;
import OnlineTicket.model.entity.Booking;
import OnlineTicket.model.entity.Payment;
import OnlineTicket.model.enums.PaymentStatus;
import OnlineTicket.repository.BookingRepository;
import OnlineTicket.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentService(PaymentRepository paymentRepository, BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    public PaymentResponse initiate(PaymentRequest req) {
        Booking booking = bookingRepository.findById(req.getBookingId()).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setMethod(req.getMethod());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(UUID.randomUUID().toString());
        Payment saved = paymentRepository.save(payment);
        PaymentResponse r = new PaymentResponse();
        r.setId(saved.getId());
        r.setBookingId(saved.getBooking().getId());
        r.setAmount(saved.getAmount());
        r.setMethod(saved.getMethod());
        r.setStatus(saved.getStatus().name());
        r.setTransactionId(saved.getTransactionId());
        r.setCreatedAt(saved.getCreatedAt());
        return r;
    }
}


