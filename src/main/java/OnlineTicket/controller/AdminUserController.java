package OnlineTicket.controller;

import OnlineTicket.model.entity.User;
import OnlineTicket.model.enums.Role;
import OnlineTicket.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final UserRepository userRepository;

    public AdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> list() {
        return userRepository.findAll();
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<User> changeRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String roleName = body.get("role");
        if (roleName == null) return ResponseEntity.badRequest().build();
        Role role = Role.valueOf(roleName);
        return userRepository.findById(id)
                .map(u -> {
                    u.setRole(role);
                    return ResponseEntity.ok(userRepository.save(u));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/active")
    public ResponseEntity<User> setActive(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean active = body.get("active");
        if (active == null) return ResponseEntity.badRequest().build();
        return userRepository.findById(id)
                .map(u -> {
                    u.setActive(active);
                    return ResponseEntity.ok(userRepository.save(u));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}


