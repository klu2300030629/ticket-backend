package OnlineTicket.service;

import OnlineTicket.exception.ResourceNotFoundException;
import OnlineTicket.model.entity.User;
import OnlineTicket.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User update(Long id, String fullName) {
        User u = get(id);
        u.setFullName(fullName);
        return userRepository.save(u);
    }
}


