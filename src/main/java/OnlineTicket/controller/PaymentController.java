package OnlineTicket.controller;

import OnlineTicket.dto.payment.PaymentRequest;
import OnlineTicket.dto.payment.PaymentResponse;
import OnlineTicket.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(@Valid @RequestBody PaymentRequest req) {
        return ResponseEntity.ok(paymentService.initiate(req));
    }
}


