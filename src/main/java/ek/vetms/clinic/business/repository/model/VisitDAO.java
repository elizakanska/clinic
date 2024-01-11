package ek.vetms.clinic.business.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "visit")
public class VisitDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id", nullable = false)
    private Long id;

    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "reason")
    private String reason;
}
