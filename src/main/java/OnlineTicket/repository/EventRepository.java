package OnlineTicket.repository;

import OnlineTicket.model.entity.Event;
import OnlineTicket.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCategory(Category category);
    List<Event> findByTrendingTrue();
    List<Event> findByStartTimeAfterOrderByStartTimeAsc(Instant after);

    @Query("select e from Event e where " +
            "(:q is null or lower(e.title) like lower(concat('%', :q, '%')) " +
            " or lower(e.description) like lower(concat('%', :q, '%')) " +
            " or lower(e.venue) like lower(concat('%', :q, '%'))) " +
            " and (:category is null or e.category.name = :category)")
    List<Event> search(@Param("q") String q, @Param("category") String category);
}


