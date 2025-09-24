package OnlineTicket.controller;

import OnlineTicket.model.entity.User;
import OnlineTicket.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserDetailsController {
    private final UserRepository userRepository;

    public UserDetailsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/details")
    public ResponseEntity<?> details(Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = userRepository.findByEmail(auth.getName()).orElse(null);
        if (u == null) return ResponseEntity.status(404).build();
        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "email", u.getEmail(),
                "fullName", u.getFullName(),
                "phone", u.getPhone(),
                "role", u.getRole().name()
        ));
    }
}


