package ek.vetms.clinic.business.mappers;

import ek.vetms.clinic.business.repository.model.PetDAO;
import ek.vetms.clinic.model.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetMapper {
    @Mapping(source = "id", target = "id")
    PetDAO petToPetDAO(Pet pet);

    @Mapping(source = "id", target = "id")
    Pet petDAOtoPet(PetDAO petDAO);
}
