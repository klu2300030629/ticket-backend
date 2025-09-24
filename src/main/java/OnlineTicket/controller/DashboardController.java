package OnlineTicket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DashboardController {

    @GetMapping("/user/dashboard")
    public ResponseEntity<Map<String, Object>> userDashboard(Authentication auth) {
        return ResponseEntity.ok(Map.of(
                "message", "Welcome, user!",
                "user", auth != null ? auth.getName() : null
        ));
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<Map<String, Object>> adminDashboard(Authentication auth) {
        return ResponseEntity.ok(Map.of(
                "message", "Welcome, admin!",
                "user", auth != null ? auth.getName() : null
        ));
    }
}


