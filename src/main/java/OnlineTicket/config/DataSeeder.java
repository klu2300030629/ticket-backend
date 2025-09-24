package OnlineTicket.config;

import OnlineTicket.model.entity.*;
import OnlineTicket.model.enums.Role;
import OnlineTicket.model.enums.SeatStatus;
import OnlineTicket.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements ApplicationRunner {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

    public DataSeeder(CategoryRepository categoryRepository, EventRepository eventRepository, SeatRepository seatRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (categoryRepository.count() == 0) {
            Category movies = new Category(); movies.setName("Movies"); movies.setDescription("Movie screenings");
            Category concerts = new Category(); concerts.setName("Concerts"); concerts.setDescription("Live music concerts");
            Category sports = new Category(); sports.setName("Sports"); sports.setDescription("Sports events");
            categoryRepository.save(movies);
            categoryRepository.save(concerts);
            categoryRepository.save(sports);

            Event e1 = new Event();
            e1.setTitle("Blockbuster Premiere");
            e1.setDescription("Premiere night");
            e1.setVenue("City Cinema Hall");
            e1.setStartTime(Instant.now().plus(3, ChronoUnit.DAYS));
            e1.setEndTime(Instant.now().plus(3, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS));
            e1.setTrending(true);
            e1.setCategory(movies);
            eventRepository.save(e1);

            Event e2 = new Event();
            e2.setTitle("Rock Fest");
            e2.setDescription("Rock night");
            e2.setVenue("Open Arena");
            e2.setStartTime(Instant.now().plus(10, ChronoUnit.DAYS));
            e2.setEndTime(Instant.now().plus(10, ChronoUnit.DAYS).plus(3, ChronoUnit.HOURS));
            e2.setTrending(true);
            e2.setCategory(concerts);
            eventRepository.save(e2);

            Event e3 = new Event();
            e3.setTitle("Championship Final");
            e3.setDescription("The big game");
            e3.setVenue("Stadium");
            e3.setStartTime(Instant.now().plus(20, ChronoUnit.DAYS));
            e3.setEndTime(Instant.now().plus(20, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS));
            e3.setTrending(false);
            e3.setCategory(sports);
            eventRepository.save(e3);

            // seats for each event
            for (Event e : List.of(e1, e2, e3)) {
                List<Seat> batch = new ArrayList<>();
                for (char row = 'A'; row <= 'C'; row++) {
                    for (int num = 1; num <= 10; num++) {
                        Seat s = new Seat();
                        s.setEvent(e);
                        s.setRowLabel(String.valueOf(row));
                        s.setSeatNumber(num);
                        s.setStatus(SeatStatus.AVAILABLE);
                        s.setPrice(row == 'A' ? 500.0 : row == 'B' ? 350.0 : 250.0);
                        batch.add(s);
                    }
                }
                seatRepository.saveAll(batch);
            }
        }

        if (userRepository.count() == 0) {
            BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setFullName("Admin User");
            admin.setPasswordHash(enc.encode("Admin@123"));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);

            User user = new User();
            user.setEmail("user@example.com");
            user.setFullName("Normal User");
            user.setPasswordHash(enc.encode("User@123"));
            user.setRole(Role.USER);
            user.setActive(true);
            userRepository.save(user);
        }
    }
}


