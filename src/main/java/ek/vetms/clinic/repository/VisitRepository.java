package ek.vetms.clinic.repository;

import ek.vetms.clinic.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByTime(LocalDateTime time);
    List<Visit> findAllByOrderByTimeAsc();
}
