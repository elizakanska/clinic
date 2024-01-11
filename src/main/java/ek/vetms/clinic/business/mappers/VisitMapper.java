package ek.vetms.clinic.business.mappers;

import ek.vetms.clinic.business.repository.model.VisitDAO;
import ek.vetms.clinic.model.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VisitMapper {
    @Mapping(source = "id", target = "id")
    VisitDAO visitToVisitDAO(Visit visit);

    @Mapping(source = "id", target = "id")
    Visit visitDAOtoVisit(VisitDAO visitDAO);
}

