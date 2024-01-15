package ek.vetms.clinic.business.mappers;

import ek.vetms.clinic.business.repository.model.PetDAO;
import ek.vetms.clinic.model.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetMapper {
    PetDAO petToPetDAO(Pet pet);

    Pet petDAOtoPet(PetDAO petDAO);
}
