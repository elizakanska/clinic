package ek.vetms.clinic.repository;


import ek.vetms.clinic.entity.Pet;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByOrderByNameAsc();
    List<Pet> findAllByOwnerUsername(@NotBlank String ownerUsername);
}
