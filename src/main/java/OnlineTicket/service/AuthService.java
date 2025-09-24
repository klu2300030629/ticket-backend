package OnlineTicket.service;

import OnlineTicket.dto.auth.LoginRequest;
import OnlineTicket.dto.auth.RegisterRequest;
import OnlineTicket.model.entity.User;
import OnlineTicket.model.enums.Role;
import OnlineTicket.repository.UserRepository;
import OnlineTicket.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public Map<String, Object> register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) throw new IllegalStateException("Email already registered");
        User u = new User();
        u.setEmail(req.getEmail());
        u.setFullName(req.getFullName());
        u.setPhone(req.getPhone());
        u.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        if (req.getRole() != null) {
            try {
                u.setRole(Role.valueOf(req.getRole().toUpperCase()));
            } catch (Exception ignored) {
                u.setRole(Role.USER);
            }
        } else {
            u.setRole(Role.USER);
        }
        userRepository.save(u);
        String token = jwtService.generateToken(u.getEmail(), Map.of("role", u.getRole().name(), "id", u.getId()));
        return Map.of("id", u.getId(), "email", u.getEmail(), "fullName", u.getFullName(), "phone", u.getPhone(), "role", u.getRole().name(), "token", token);
    }

    public Map<String, Object> login(LoginRequest req) {
        User u = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new IllegalStateException("Invalid credentials"));
        if (!u.isActive()) throw new IllegalStateException("Account is inactive");
        if (!passwordEncoder.matches(req.getPassword(), u.getPasswordHash())) throw new IllegalStateException("Invalid credentials");
        String token = jwtService.generateToken(u.getEmail(), Map.of("role", u.getRole().name(), "id", u.getId()));
        return Map.of("id", u.getId(), "email", u.getEmail(), "fullName", u.getFullName(), "phone", u.getPhone(), "role", u.getRole().name(), "token", token);
    }
}


