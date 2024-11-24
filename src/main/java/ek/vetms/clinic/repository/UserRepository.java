package ek.vetms.clinic.repository;
import ek.vetms.clinic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}

