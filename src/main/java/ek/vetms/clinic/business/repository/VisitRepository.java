package ek.vetms.clinic.business.repository;

import ek.vetms.clinic.business.repository.model.VisitDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<VisitDAO, Long> {
    List<VisitDAO> findVisitById(Long id);
}
