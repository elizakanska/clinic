package ek.vetms.clinic.business.mappers;

import ek.vetms.clinic.business.repository.model.VisitDAO;
import ek.vetms.clinic.model.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VisitMapper {
    @Mapping(source = "pet.id", target = "petId")
    VisitDAO visitToVisitDAO(Visit visit);

    @Mapping(source = "petId", target = "pet.id")
    Visit visitDAOtoVisit(VisitDAO visitDAO);
}