package ek.vetms.clinic.business.repository;

import ek.vetms.clinic.business.repository.model.PetDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<PetDAO, Long> {
    List<PetDAO> findPetById(Long id);
}
