package ek.vetms.clinic.business.mappers;

import ek.vetms.clinic.business.repository.model.VisitDAO;
import ek.vetms.clinic.model.Visit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VisitMapper {
    VisitDAO visitToVisitDAO(Visit visit);

    Visit visitDAOtoVisit(VisitDAO visitDAO);
}

