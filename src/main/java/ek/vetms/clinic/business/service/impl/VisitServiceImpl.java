package ek.vetms.clinic.business.service.impl;

import ek.vetms.clinic.business.mappers.VisitMapper;
import ek.vetms.clinic.business.repository.VisitRepository;
import ek.vetms.clinic.business.repository.model.VisitDAO;
import ek.vetms.clinic.business.service.VisitService;
import ek.vetms.clinic.model.Visit;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Log4j2
@Service
@Data
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {
    private final VisitRepository repository;
    private final VisitMapper mapper;

    @Override
    public Optional<Visit> findVisitById(Long id) {
        log.info("Looking for visit with id {}.", id);
        return repository.findById(id)
                .flatMap((x -> Optional.ofNullable(mapper.visitDAOtoVisit(x))));
    }

    @Override
    public Visit saveVisit(Visit visit) throws IllegalArgumentException {
        log.info("Saving new visit.");
        VisitDAO visitToSave = repository.save(mapper.visitToVisitDAO(visit));

        log.info("New visit saved with id {}.", visitToSave.getId());
        return mapper.visitDAOtoVisit(visitToSave);
    }

    @Override
    public Optional<Visit> editVisitById(Long id, Visit visit) {
        log.info("Updating entry for visit with given id {}.", id);
        Optional<VisitDAO> visitToEdit = repository.findById(id);

        if (visitToEdit.isPresent()) {
            VisitDAO existingVisit = visitToEdit.get();

            existingVisit.setPetId(visit.getPet().getId());
            existingVisit.setTime(LocalDateTime.parse(visit.getTime()));
            existingVisit.setReason(visit.getReason());

            VisitDAO updatedVisit = repository.save(existingVisit);
            return Optional.ofNullable(mapper.visitDAOtoVisit(updatedVisit));
        } else {
            log.info("Visit with id {} not found.", id);
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Visit> deleteVisitById(Long id) {
        log.info("Deleting visit with given id {}.", id);
        Optional<VisitDAO> visitToDelete = repository.findById(id);

        if (visitToDelete.isPresent()) {
            VisitDAO visitDAO = visitToDelete.get();
            repository.delete(visitDAO);
            return Optional.ofNullable(mapper.visitDAOtoVisit(visitDAO));
        } else {
            log.info("Visit with id {} not found.", id);
            return Optional.empty();
        }
    }
}
