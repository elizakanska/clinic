package ek.vetms.clinic.repository;

import ek.vetms.clinic.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}

