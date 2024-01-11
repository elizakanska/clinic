package ek.vetms.clinic.business.repository;

import ek.vetms.clinic.business.repository.model.VisitDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<VisitDAO, Long> {
}
